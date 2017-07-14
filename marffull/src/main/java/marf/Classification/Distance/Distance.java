package marf.Classification.Distance;

import java.util.Vector;

import marf.Classification.Classification;
import marf.Classification.ClassificationException;
import marf.FeatureExtraction.IFeatureExtraction;
import marf.Storage.Cluster;
import marf.Storage.Result;
import marf.Storage.StorageException;
import marf.util.Debug;


/**
 * <p>Abstract Distance Classifier.</p>
 * <p>
 * <p>$Id: Distance.java,v 1.30 2005/12/24 19:47:37 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.30 $
 * @since 0.0.1
 */
public abstract class Distance
        extends Classification {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.5
     */
    private static final long serialVersionUID = -6142163714569438592L;

    /**
     * Distance Constructor.
     *
     * @param poFeatureExtraction FeatureExtraction module reference
     */
    public Distance(IFeatureExtraction poFeatureExtraction) {
        super(poFeatureExtraction);
    }

    /**
     * Classify the feature vector based on whatever
     * <code>distance()</code> derivatives implement.
     *
     * @return true if classification successful whatever that means
     * @throws ClassificationException if sanity checks fail. The checks include
     *                                 verifying nullness of the mean vector and its length compared to the
     *                                 feature vector or encapsulation of StorageException when dumping/restoring.
     * @see #distance(double[], double[])
     */
    public final boolean classify()
            throws ClassificationException {
        try {
            // Features of the incoming sample
            double[] adIncomingFeatures = oFeatureExtraction.getFeaturesArray();

            // Restore training model from the disk
            restore();

            // Features in the training set
            Vector oTrainingSamples = this.oTrainingSet.getClusters();

            // Our minimum distance.
            double dMinDistance = Double.MAX_VALUE;

			/*
             * Run through the stored training samples set (mean vetors)
			 * and determine the two closest subjects to the incoming features sample
			 */
            for (int i = 0; i < oTrainingSamples.size(); i++) {
                Cluster oTrainingSample = (Cluster) oTrainingSamples.get(i);

                double[] adMeanVector = oTrainingSample.getMeanVector();

                // Sanity check: stored mean vector must never be null
                if (adMeanVector == null) {
                    throw new ClassificationException
                            (
                                    "Distance.classify() - Stored mean vector is null for subject (" + oTrainingSample.getSubjectID() +
                                            ", preprocessing method: " + this.oTrainingSet.getPreprocessingMethod() +
                                            ", feature extraction methods: " + this.oTrainingSet.getFeatureExtractionMethod()
                            );
                }

                // Sanity check: vectors must be of the same length
                if (adMeanVector.length != adIncomingFeatures.length) {
                    throw new ClassificationException
                            (
                                    "Distance.classify() - Mean vector length (" + adMeanVector.length +
                                            ") is not same as of incoming feature vector (" + adIncomingFeatures.length + ")"
                            );
                }

				/*
                 * We have a mean vector of the samples for this iCurrentSubjectID
				 * Compare using whatever distance classifier it is...
				 */
                double dCurrentDistance = distance(adMeanVector, adIncomingFeatures);

                Debug.debug("Distance for subject " + oTrainingSample.getSubjectID() + " = " + dCurrentDistance);

                // XXX: What should we do in this (very rare and subtle) case?
                if (dCurrentDistance == dMinDistance) {
                    Debug.debug("This distance had happened before!");
                }

                if (dCurrentDistance < dMinDistance) {
                    dMinDistance = dCurrentDistance;
                }

                // Collect for stats
                // XXX: Move to StatsCollector
                this.oResultSet.addResult
                        (
                                oTrainingSample.getSubjectID(),
                                dCurrentDistance
                        );
            }

            return true;
        } catch (StorageException e) {
            throw new ClassificationException(e);
        }
    }

    /**
     * Generic distance routine. To be overridden.
     *
     * @param paVector1 first vector for distance calculation
     * @param paVector2 second vector for distance calculation
     * @return distance between the two vectors
     */
    public abstract double distance(final double[] paVector1, final double[] paVector2);

    /**
     * Retrieves the minimum-distance classification result.
     *
     * @return Result object
     * @since 0.3.0
     */
    public Result getResult() {
        return this.oResultSet.getMinimumResult();
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.30 $";
    }
}

// EOF
