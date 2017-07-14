package marf.Classification.Distance;

import marf.FeatureExtraction.IFeatureExtraction;


/**
 * <p>Class EuclideanDistance.</p>
 * <p>
 * <p>$Id: EuclideanDistance.java,v 1.20 2005/08/11 00:44:50 mokhov Exp $</p>
 *
 * @author Stephen Sinclair
 * @author Serguei Mokhov
 * @version $Revision: 1.20 $
 * @since 0.0.1
 */
public class EuclideanDistance
        extends Distance {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 6805036936942102456L;

    /**
     * EuclideanDistance Constructor.
     *
     * @param poFeatureExtraction FeatureExtraction module reference
     */
    public EuclideanDistance(IFeatureExtraction poFeatureExtraction) {
        super(poFeatureExtraction);
    }

    /**
     * EuclideanDistance implementation.
     *
     * @param paVector1 first vector to compare
     * @param paVector2 second vector to compare
     * @return Euclidean distance between two feature vectors
     */
    public final double distance(final double[] paVector1, final double[] paVector2) {
        double dDistance = 0;

        for (int f = 0; f < paVector1.length; f++)
            dDistance += (paVector1[f] - paVector2[f]) * (paVector1[f] - paVector2[f]);

        return dDistance;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.20 $";
    }
}

// EOF
