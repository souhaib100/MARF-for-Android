package marf.Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;

import marf.util.Arrays;


/**
 * <p>TrainingSample contains one item in the training set.
 * Each training sample consists of the feature vector plus
 * information describing that feature vector.
 * Has been extracted from TrainingSet in 0.3.0.
 * TODO: fix CSV dumps
 * </p>
 * <p>
 * $Id: TrainingSample.java,v 1.10 2006/01/02 22:24:00 mokhov Exp $
 *
 * @author Stephen Sinclair
 * @author Serguei Mokhov
 * @version $Revision: 1.10 $
 * @since 0.0.1
 */
public class TrainingSample
        implements Serializable, Cloneable {
    /**
     * Which subject this feature vector is associated with.
     */
    protected int iSubjectID;

    /**
     * Array represinting either a feature or mean vector describing the cluster.
     */
    protected double[] adDataVector = null;

    /**
     * A list of filenames that were used in training for this cluster.
     * Used to avoid duplicate training on the same filename.
     */
    protected Vector oFilenames = new Vector();

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 440451144821982021L;

    /**
     * Default training sample constructor.
     * Explicitly appeared in 0.3.0.5.
     *
     * @since 0.3.0.5
     */
    public TrainingSample() {
    }

    /**
     * Copy-constructor.
     *
     * @param poTrainingSample TrainingSample object to copy
     * @since 0.3.0.5
     */
    public TrainingSample(final TrainingSample poTrainingSample) {
        super();

        this.iSubjectID = poTrainingSample.iSubjectID;
        this.oFilenames = (Vector) poTrainingSample.oFilenames.clone();

        if (poTrainingSample.adDataVector != null) {
            this.adDataVector = (double[]) poTrainingSample.adDataVector.clone();
        }
    }

    /**
     * Sets a filename of the training sample.
     * Allways set the first element of the list of filenames.
     *
     * @param pstrFilename filename to set
     */
    public final void setFilename(String pstrFilename) {
        if (this.oFilenames.size() == 0) {
            this.oFilenames.add(pstrFilename);
        } else {
            this.oFilenames.set(0, pstrFilename);
        }
    }

    /**
     * Retrieves Subject ID of a particular training sample.
     *
     * @return int ID
     */
    public final int getSubjectID() {
        return this.iSubjectID;
    }

    /**
     * Retrieves the data vector.
     *
     * @return array of doubles representing the data for this subject
     */
    public final double[] getDataVector() {
        return this.adDataVector;
    }

    /**
     * Sets new Subject ID.
     *
     * @param piSubjectID integer ID
     */
    public final void setSubjectID(final int piSubjectID) {
        this.iSubjectID = piSubjectID;
    }

    /**
     * Sets new mean vector.
     *
     * @param padDataVector double array representing the mean vector
     */
    public final void setDataVector(double[] padDataVector) {
        this.adDataVector = padDataVector;
    }

    /**
     * Writes one training sample to a CSV file.
     *
     * @param poBufferedWriter BufferedWriter
     * @throws StorageException in case of I/O or otherwise error
     */
    public void dumpCSV(BufferedWriter poBufferedWriter)
            throws StorageException {
        try {
            StringBuffer oDump = new StringBuffer();

            oDump
                    .append(this.iSubjectID).append(",").append(this.adDataVector.length).append(",")
                    .append(Arrays.arrayToCSV(this.adDataVector));

            poBufferedWriter.write(oDump.toString());
            poBufferedWriter.newLine();
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Retrieve one training sample from a CSV file.
     * TODO: FIX, for it's BROKEN.
     *
     * @param poBufferedReader BufferedReader
     * @throws StorageException in case of I/O or otherwise error
     */
    public void restoreCSV(BufferedReader poBufferedReader)
            throws StorageException {
        try {
            StringTokenizer oTokenizer = new StringTokenizer(poBufferedReader.readLine(), ",");

            int iLen = 0;

            if (oTokenizer.hasMoreTokens()) {
                this.iSubjectID = Integer.parseInt(oTokenizer.nextToken());
            }

            //		if(oTokenizer.hasMoreTokens())
            //			iFeatureExtractionMethod = Integer.parseInt(oTokenizer.nextToken());

            //		if(oTokenizer.hasMoreTokens())
            //			iPreprocessingMethod = Integer.parseInt(oTokenizer.nextToken());

            if (oTokenizer.hasMoreTokens()) {
                iLen = Integer.parseInt(oTokenizer.nextToken());
            }

            this.adDataVector = new double[iLen];

            int i = 0;

            while (oTokenizer.hasMoreTokens()) {
                this.adDataVector[i++] = Double.parseDouble(oTokenizer.nextToken());
            }
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Implements Cloneable interface for the TrainingSample object.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        return new TrainingSample(this);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.10 $";
    }
}

// EOF
