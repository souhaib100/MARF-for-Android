package marf.Classification.RandomClassification;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import marf.Classification.Classification;
import marf.Classification.ClassificationException;
import marf.FeatureExtraction.IFeatureExtraction;
import marf.MARF;
import marf.Storage.Result;
import marf.Storage.StorageException;
import marf.util.Debug;


/**
 * <p>Random Classification Module is for testing purposes.</p>
 * <p>
 * <p>This represents the bottomline of the classification results.
 * All the other modules should be better than this 99% of the time.
 * If they are not, debug them.</p>
 * <p>
 * <p>$Id: RandomClassification.java,v 1.17 2006/01/02 19:26:44 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.17 $
 * @since 0.2.0
 */
public class RandomClassification
        extends Classification {
    /**
     * Vector of integer IDs.
     */
    private Vector oIDs = new Vector();

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -6770780209979417110L;

    /**
     * RandomClassification Constructor.
     *
     * @param poFeatureExtraction FeatureExtraction module reference
     */
    public RandomClassification(IFeatureExtraction poFeatureExtraction) {
        super(poFeatureExtraction);

        this.strFilename =
                getClass().getName() + "." +
                        MARF.getPreprocessingMethod() + "." +
                        MARF.getFeatureExtractionMethod() + "." +
                        getDefaultExtension();
    }

    /**
     * Picks an ID at random.
     *
     * @return <code>true</code>
     * @throws ClassificationException
     */
    public final boolean classify()
            throws ClassificationException {
        try {
            int iFirstID = 0;
            int iSecondID = 0;

            restore();

            if (this.oIDs.size() == 0) {
                Debug.debug("RandomClassification.classify() --- ID set is of 0 length.");

                this.oIDs.add(new Integer(iFirstID));

                this.oResultSet.addResult
                        (
                                iFirstID,
                                0,
                                getClass().getName() +
                                        " out of the blue"
                        );
            } else {
                // Collect for stats
                // XXX: Move to StatsCollector
                iFirstID = ((Integer) this.oIDs.elementAt((int) (this.oIDs.size() * (new Random().nextDouble())))).intValue();
                iSecondID = iFirstID;

                // Pick a different second best ID if there are > 1 IDs in there
                while (iSecondID == iFirstID && this.oIDs.size() > 1) {
                    iSecondID = ((Integer) this.oIDs.elementAt((int) (this.oIDs.size() * (new Random().nextDouble())))).intValue();
                }

                // If they are still equal (in case of one ID in oIDs), just add one.
                if (iSecondID == iFirstID) {
                    iSecondID++;
                }

                Debug.debug("RandomClassification.classify() --- ID1 = " + iFirstID + ", ID2 = " + iSecondID);

                this.oResultSet.addResult(iFirstID, 0, getClass().getName());
                this.oResultSet.addResult(iSecondID, 1, getClass().getName());
            }

            return true;
        } catch (StorageException e) {
            throw new ClassificationException(e);
        }
    }

    /**
     * Simply stores incoming ID's to later pick one at random.
     *
     * @return <code>true</code> if training was successful
     * @throws ClassificationException
     */
    public final boolean train()
            throws ClassificationException {
        try {
            Integer oIntegerID = new Integer(MARF.getCurrentSubject());

            restore();

            // Avoid adding duplicates.
            if (this.oIDs.contains(oIntegerID) == false) {
                this.oIDs.add(oIntegerID);

                dump();

                Debug.debug
                        (
                                "RandomClassification.train() --- added ID: " + MARF.getCurrentSubject() + ",\n" +
                                        "all IDs: " + this.oIDs
                        );
            }

            return true;
        } catch (StorageException e) {
            throw new ClassificationException("Exception in RandomClassification.train() --- " + e.getMessage());
        }
    }

	/* From Storage Manager */

    /**
     * Dumps "training set" of IDs.
     *
     * @throws StorageException
     */
    public final void dump()
            throws StorageException {
        try {
            FileOutputStream oFOS = new FileOutputStream(this.strFilename);
            GZIPOutputStream oGZOS = new GZIPOutputStream(oFOS);
            ObjectOutputStream oOOS = new ObjectOutputStream(oGZOS);

            oOOS.writeObject(this.oIDs);
            oOOS.flush();
            oOOS.close();
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Restores "training set" of IDs.
     *
     * @throws StorageException if there was an error loading the data file.
     */
    public final void restore()
            throws StorageException {
        try {
            FileInputStream oFIS = new FileInputStream(this.strFilename);
            GZIPInputStream oGZIS = new GZIPInputStream(oFIS);
            ObjectInputStream oOIS = new ObjectInputStream(oGZIS);

            this.oIDs = (Vector) oOIS.readObject();

            oOIS.close();
        } catch (FileNotFoundException e) {
            this.oIDs = new Vector();
            dump();
        } catch (ClassNotFoundException e) {
            throw new StorageException
                    (
                            "RandomClassification.restore() --- ClassNotFoundException: " +
                                    e.getMessage()
                    );
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Retrieves the classification result.
     *
     * @return Result object
     * @since 0.3.0
     */
    public Result getResult() {
        return this.oResultSet.getMinimumResult();
    }

    /**
     * Returns string representation of the internals of this object.
     *
     * @return String
     * @since 0.3.0
     */
    public String toString() {
        StringBuffer oBuffer = new StringBuffer();

        oBuffer
                .append("Dump mode: ").append(this.iCurrentDumpMode).append("\n")
                .append("Dump file: ").append(this.strFilename).append("\n")
                .append("ID data: ").append(this.oIDs);

        return oBuffer.toString();
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.17 $";
    }
}

// EOF
