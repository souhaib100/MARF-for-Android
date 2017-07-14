package marf.Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import marf.util.Arrays;
import marf.util.Debug;
import marf.util.NotImplementedException;


/**
 * <p>Cluster contains a cluster information per subject.</p>
 * <p>
 * <p>$Id: Cluster.java,v 1.14 2006/01/06 22:18:59 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.14 $
 * @since 0.3.0.1
 */
public class Cluster
        extends TrainingSample {
    /**
     * How many times mean was computed.
     * Used in recomputation of it.
     */
    protected int iMeanCount = 0;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -8773915807751754560L;

    /**
     * Default cluster constructor.
     * Explicitly appeared in 0.3.0.5.
     *
     * @since 0.3.0.5
     */
    public Cluster() {
    }

    /**
     * Copy-constructor.
     *
     * @param poCluster the Cluster object to copy properties of
     * @since 0.3.0.5
     */
    public Cluster(final Cluster poCluster) {
        super(poCluster);
        this.iMeanCount = poCluster.iMeanCount;
    }

    /**
     * Adds a filename to the training set.
     *
     * @param pstrFilename filename to add
     * @return <code>false</code> if the filename is already there; <code>true</code> otherwise
     * @see #existsFilename(String)
     */
    public final boolean addFilename(String pstrFilename) {
        if (existsFilename(pstrFilename)) {
            return false;
        }

        this.oFilenames.add(pstrFilename);

        return true;
    }

    /**
     * Checks existance of the file in the training set.
     * Serves as an indication that we already trained on the given file.
     *
     * @param pstrFilename filename to check
     * @return <code>true</code> if the filename is there; <code>false</code> if not
     */
    public final boolean existsFilename(String pstrFilename) {
        if (oFilenames.contains(pstrFilename)) {
            return true;
        }

        return false;
    }

    /**
     * Retrieves current mean count.
     *
     * @return mean count
     */
    public final int getMeanCount() {
        return this.iMeanCount;
    }

    /**
     * Increases mean count by one.
     *
     * @return new mean count
     */
    public final int incMeanCount() {
        return (++this.iMeanCount);
    }

    /**
     * Retrieves the mean vector.
     *
     * @return array of doubles representing the mean for that cluster
     */
    public final double[] getMeanVector() {
        return getDataVector();
    }

    /**
     * Sets new mean vector.
     *
     * @param padMeanVector double array representing the mean vector
     */
    public final void setMeanVector(double[] padMeanVector) {
        setDataVector(padMeanVector);
    }

    /**
     * Adds new feature vector to the mean and recomputes the mean.
     *
     * @param padFeatureVector vector to add
     * @param pstrFilename     filename to add
     * @param piSubjectID      for which subject that vector is
     * @return <code>true</code> if the vector was added; <code>false</code> otherwise
     */
    public final boolean addFeatureVector
    (
            double[] padFeatureVector,
            String pstrFilename,
            int piSubjectID
    ) {
        boolean bNewSample = getDataVector() == null ? true : false;

        if (bNewSample == true) {
            setSubjectID(piSubjectID);
            setMeanVector((double[]) padFeatureVector.clone());
        } else {
            /*
             * If this file has been trained on already, no retraining
			 * required. Return false to indicate that no changes are made.
			 */
            if (addFilename(pstrFilename) == false) {
                return false;
            }

            // What if piSubjectID is different from the one in this.iSubjectID?
            assert this.iSubjectID == piSubjectID;

            // Recompute the mean
            for (int f = 0; f < adDataVector.length; f++) {
                this.adDataVector[f] =
                        (this.adDataVector[f] * this.iMeanCount + padFeatureVector[f]) /
                                (this.iMeanCount + 1);
            }
        }

        incMeanCount();

        if (bNewSample == true) {
            Debug.debug("Added feature vector for subject: " + piSubjectID);
        } else {
            Debug.debug("Updated mean vector for subject: " + piSubjectID);
        }

        return true;
    }

    /**
     * Write one training cluster as a CSV text.
     *
     * @param poWriter BufferedWriter to write to
     * @throws StorageException in case of any error while dumping
     * @since 0.3.0.5
     */
    public void dumpCSV(BufferedWriter poWriter)
            throws StorageException {
        try {
            StringBuffer oDump = new StringBuffer();

            oDump
                    .append(this.iSubjectID).append(",")
                    .append(this.adDataVector.length).append(",")
                    .append(this.iMeanCount).append(",")
                    .append(Arrays.arrayToCSV(this.adDataVector));

            poWriter.write(oDump.toString());
            poWriter.newLine();
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Retrieve one training cluster from the specified reader as a CSV text.
     * Not implemented.
     *
     * @param poReader BufferedReader to read from
     * @throws StorageException        never thrown
     * @throws NotImplementedException
     * @since 0.3.0.5
     */
    public void restoreCSV(BufferedReader poReader)
            throws StorageException {
        throw new NotImplementedException();
    }

    /**
     * Implements Cloneable interface for the Cluster object.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        return new Cluster(this);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.14 $";
    }
} // class Cluster

// EOF
