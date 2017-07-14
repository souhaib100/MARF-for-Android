package marf.Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import marf.util.Debug;
import marf.util.NotImplementedException;


/**
 * <p>TrainingSet -- Encapsulates Subject ID and subject's clusters or a feature set.</p>
 * <p>
 * <p>$Id: TrainingSet.java,v 1.44 2006/01/02 22:24:00 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.44 $
 * @since 0.0.1, December 5, 2002
 */
public class TrainingSet
        extends StorageManager {
    /*
     * NOTE: Be careful when you mess with this file. Any new fields
	 *       or structural changes will change the on-disk layout of
	 *       whole TrainingSet. Until an upgrade utility is available,
	 *       you will have to retrain ALL your models that use TrainingSet.
	 */

    /**
     * Default TrainingSet file name of <code>marf.training.set</code>.
     *
     * @since 0.3.0
     */
    public static final String DEFAULT_TRAINING_SET_FILENAME = "marf.training.set";

    //for android version
    public static String WORKING_PATH = "";

    /**
     * A Vector of Clusters.
     */
    protected Vector oClusters = new Vector();

    /**
     * Feature Set as opposed to the cluster.
     */
    protected FeatureSet oFeatureSet = null;

    /**
     * Which preprocessing method was applied to the sample before this feature vector was extracted.
     */
    protected int iPreprocessingMethod;

    /**
     * Which feature extraction method was used to determine this feature vector.
     */
    protected int iFeatureExtractionMethod;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 2733362635058973186L;

    /**
     * Construct a training set object.
     */
    public TrainingSet() {
        this.iCurrentDumpMode = DUMP_GZIP_BINARY;
        this.strFilename = DEFAULT_TRAINING_SET_FILENAME;
    }

    /**
     * Retrieves clusters of training samples.
     *
     * @return vector of training samples.
     */
    public final Vector getClusters() {
        return this.oClusters;
    }

    /**
     * Returns preprocessing method used on this training set.
     *
     * @return the method
     */
    public final int getPreprocessingMethod() {
        return this.iPreprocessingMethod;
    }

    /**
     * Returns preprocessing method used on this training set.
     *
     * @return piPreprocessingMethod the method
     */
    public final int getFeatureExtractionMethod() {
        return this.iFeatureExtractionMethod;
    }

    /**
     * Sets feature extraction method used on this training set.
     *
     * @param piPreprocessingMethod the method
     */
    public final void setPreprocessingMethod(int piPreprocessingMethod) {
        this.iPreprocessingMethod = piPreprocessingMethod;
    }

    /**
     * Sets feature extraction method used on this training set.
     *
     * @param piFeatureExtractionMethod the method
     */
    public final void setFeatureExtractionMethod(int piFeatureExtractionMethod) {
        this.iFeatureExtractionMethod = piFeatureExtractionMethod;
    }

    /**
     * Adds new feature vector to the mean and recomputes the mean.
     *
     * @param padFeatureVector          vector to add
     * @param pstrFilename              to check to avoid training on the same file
     * @param piSubjectID               for which subject that vector is
     * @param piPreprocessingMethod     preprocessing method used
     * @param piFeatureExtractionMethod feature extraction method used
     * @return <code>true</code> if the vector was added; <code>false</code> otherwise
     */
    public final boolean addFeatureVector
    (
            double[] padFeatureVector,
            String pstrFilename,
            int piSubjectID,
            int piPreprocessingMethod,
            int piFeatureExtractionMethod
    ) {
        /*
		 * check if this sample is already in the training set
		 * for these feature extraction & preprocessing methods
		 */
        Cluster oCluster = null;
        boolean bNewSample = true;

        double[] adMeanVector = null;

        for (int i = 0; (i < oClusters.size()) && (bNewSample); i++) {
            oCluster = (Cluster) oClusters.get(i);

            if (piSubjectID == oCluster.getSubjectID()) {
                // Disallow training on the same file twice
                if (oCluster.existsFilename(pstrFilename)) {
                    Debug.debug
                            (
                                    "TrainingSet.addFeatureVector() --- Attempt to train on the same file: " +
                                            pstrFilename
                            );

                    return false;
                }

                bNewSample = false;
                adMeanVector = oCluster.getMeanVector();
            }
        }

        if (bNewSample) {
            oCluster = new Cluster();
            adMeanVector = (double[]) padFeatureVector.clone();
        } else {
            int iMeanCount = oCluster.getMeanCount();

            // Recompute the mean
            for (int f = 0; f < adMeanVector.length; f++) {
                adMeanVector[f] = (adMeanVector[f] * iMeanCount + padFeatureVector[f]) / (iMeanCount + 1);
            }
        }

        oCluster.setMeanVector(adMeanVector);
        oCluster.setSubjectID(piSubjectID);
        oCluster.addFilename(pstrFilename);
        oCluster.incMeanCount();

        setFeatureExtractionMethod(piFeatureExtractionMethod);
        setPreprocessingMethod(piPreprocessingMethod);

        if (bNewSample) {
            this.oClusters.add(oCluster);

            Debug.debug
                    (
                            "Added feature vector for subject " + piSubjectID +
                                    ", preprocessing method " + piPreprocessingMethod +
                                    ", feature extraction method " + piFeatureExtractionMethod
                    );
        } else {
            Debug.debug
                    (
                            "Updated mean vector for subject " + piSubjectID +
                                    ", preprocessing method " + piPreprocessingMethod +
                                    ", feature extraction method " + piFeatureExtractionMethod
                    );
        }

        return true;
    }

    /**
     * Gets the size of the feature vectors set.
     *
     * @return number of training samples in the set
     */
    public final int size() {
        return this.oClusters.size();
    }

    /**
     * Retrieve the current training set from disk.
     *
     * @throws StorageException in a case of I/O or otherwise error
     */
    public void restore()
            throws StorageException {
        try {
            switch (this.iCurrentDumpMode) {
				/*
				 * TODO: check whether this code is still valid
				 */
                case DUMP_CSV_TEXT: {
                    BufferedReader oReader = new BufferedReader(new FileReader(WORKING_PATH + "/" + this.strFilename));

                    int iLength = Integer.parseInt(oReader.readLine());

                    for (int i = 0; i < iLength; i++) {
                        Cluster oCluster = new Cluster();

                        oCluster.restoreCSV(oReader);
                        this.oClusters.add(oCluster);
                    }

                    oReader.close();

                    break;
                }

                case DUMP_GZIP_BINARY: {
                    FileInputStream oFIS = new FileInputStream(WORKING_PATH + "/" + this.strFilename);
                    GZIPInputStream oGZIS = new GZIPInputStream(oFIS);
                    ObjectInputStream oOIS = new ObjectInputStream(oGZIS);

                    TrainingSet oTrainingSet = (TrainingSet) oOIS.readObject();

                    oOIS.close();

                    this.oClusters = oTrainingSet.getClusters();

                    break;
                }

				/*
				 * TODO: implement these
				 */
                case DUMP_BINARY:
                case DUMP_XML:
                case DUMP_HTML:
                case DUMP_SQL: {
                    throw new NotImplementedException
                            (
                                    "TrainingSet.restore() -- DUMP_BINARY, _XML, _HTML, and _SQL are not supported yet."
                            );
                }

                default: {
                    throw new StorageException
                            (
                                    "TrainingSet.restore() --- Invalid file format: " +
                                            this.iCurrentDumpMode
                            );
                }
            }

            Debug.debug("Training set loaded successfully: " + this.oClusters.size() + " mean vectors.");
        } catch (FileNotFoundException e) {
            Debug.debug
                    (
                            "TrainingSet.restore() --- FileNotFoundException for file: \"" +
                                    this.strFilename + "\", " +
                                    e.getMessage() + "\n" +
                                    "Creating one now..."
                    );

            dump();
        } catch (NumberFormatException e) {
            throw new StorageException
                    (
                            "TrainingSet.restore() --- NumberFormatException: " +
                                    e.getMessage()
                    );
        } catch (ClassNotFoundException e) {
            throw new StorageException
                    (
                            "TrainingSet.restore() --- ClassNotFoundException: " +
                                    e.getMessage()
                    );
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Dump the current training set to disk.
     *
     * @throws StorageException in a case of I/O or otherwise error
     */
    public void dump()
            throws StorageException {
        try {
            switch (this.iCurrentDumpMode) {
                // TODO: review and test for it's broken
                case DUMP_CSV_TEXT: {
                    BufferedWriter oWriter = new BufferedWriter(new FileWriter(WORKING_PATH + "/" + this.strFilename));

                    oWriter.write(Integer.toString(this.oClusters.size()));
                    oWriter.newLine();

                    Debug.debug
                            (
                                    "Wrote " + this.oClusters.size() + " clusters " +
                                            "to file " + this.strFilename
                            );

                    for (int i = 0; i < this.oClusters.size(); i++) {
                        ((Cluster) this.oClusters.get(i)).dumpCSV(oWriter);
                    }

                    oWriter.close();

                    break;
                }

                case DUMP_GZIP_BINARY: {
                    FileOutputStream oFOS = new FileOutputStream(WORKING_PATH + "/" + this.strFilename);
                    GZIPOutputStream oGZOS = new GZIPOutputStream(oFOS);
                    ObjectOutputStream oOOS = new ObjectOutputStream(oGZOS);

                    oOOS.writeObject(this);
                    oOOS.flush();
                    oOOS.close();
                    break;
                }

                case DUMP_BINARY:
                case DUMP_XML:
                case DUMP_HTML:
                case DUMP_SQL: {
                    throw new NotImplementedException
                            (
                                    "TrainingSet.dump() -- DUMP_BINARY, _XML, _HTML, and _SQL are not supported yet."
                            );
                }

                default: {
                    throw new StorageException
                            (
                                    "TrainingSet.dump() --- Invalid file format: "
                                            + this.iCurrentDumpMode
                            );
                }
            }
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    /**
     * Implementes Cloneable interface for the TrainingSet object.
     * Performs a "deep" copy of this object including all clusters
     * and the feature set.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        TrainingSet oClone = (TrainingSet) super.clone();

        oClone.oClusters =
                this.oClusters == null ?
                        null : (Vector) this.oClusters.clone();

        oClone.oFeatureSet =
                this.oFeatureSet == null ?
                        null : (FeatureSet) this.oFeatureSet.clone();

        return oClone;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.44 $";
    }
}

// EOF
