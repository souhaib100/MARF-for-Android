package marf.Classification.Distance;

import marf.FeatureExtraction.IFeatureExtraction;


/**
 * <p>Diff-Distance Classifier.</p>
 * <p>
 * <p>$Id: DiffDistance.java,v 1.10 2005/08/11 00:44:50 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.10 $
 * @since 0.3.0
 */
public class DiffDistance
        extends Distance {
    /**
     * Default allowed difference between two features of 0.0001.
     */
    public static final double DEFAULT_ALLOWED_ERROR = 0.0001;

    /**
     * Default distance factor to add to the difference
     * to make the subject more distant.
     */
    public static final double DISTANCE_FACTOR = 1.0;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 4805074823902537333L;

    /**
     * DiffDistance Constructor.
     *
     * @param poFeatureExtraction FeatureExtraction module reference
     */
    public DiffDistance(IFeatureExtraction poFeatureExtraction) {
        super(poFeatureExtraction);
    }

    /**
     * Diff-Distance implementation.
     *
     * @param padVector1 first vector to compare
     * @param padVector2 second vector to compare
     * @return diff-distance between two feature vectors
     */
    public final double distance(final double[] padVector1, final double[] padVector2) {
        double dDistance = 0;

        int i = 0;
        int j = 0;

        while (i != padVector1.length - 1 || j != padVector2.length - 1) {
            //Debug.debug("DiffDistance (1): i = " + i + ", j = " + j + ", v[i] = " + adVector1[i] + ", u[j] = " + adVector2[j]);

            if (Math.abs(padVector1[i] - padVector2[j]) > DEFAULT_ALLOWED_ERROR) {
                // Penalty
                dDistance +=
                        (Math.abs(padVector1[i] - padVector2[j])) + DISTANCE_FACTOR;
            } else {
                // Bonus
                dDistance -= DEFAULT_ALLOWED_ERROR;
            }

            i++;
            j++;

            if (i > padVector1.length - 1) {
                i = padVector1.length - 1;
            }

            if (j > padVector2.length - 1) {
                j = padVector2.length - 1;
            }

            //Debug.debug("DiffDistance (2): i = " + i + ", j = " + j + ", v[i] = " + adVector1[i] + ", u[j] = " + adVector2[j] + ", distance = " + dDistance);
        }

        return dDistance;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.10 $";
    }
}

// EOF
