package marf.Classification.Distance;

import java.util.Vector;

import marf.FeatureExtraction.IFeatureExtraction;
import marf.MARF;


/**
 * <p>Class MinkowskiDistance.</p>
 * <p>
 * <p>$Id: MinkowskiDistance.java,v 1.11 2005/08/14 01:15:54 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.11 $
 * @since 0.2.0
 */
public class MinkowskiDistance
        extends Distance {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 5195894141171408028L;

    /**
     * Minkowski Factor.
     * Default is 3; r = 1 is Chebyshev distance, r = 2 is Euclidean distance.
     */
    private double r = 3;

    /**
     * MinkowskiDistance Constructor.
     *
     * @param poFeatureExtraction FeatureExtraction module reference
     */
    public MinkowskiDistance(IFeatureExtraction poFeatureExtraction) {
        super(poFeatureExtraction);

        // See if there is a request for another r
        if (MARF.getModuleParams() != null) {
            Vector oParams = MARF.getModuleParams().getClassificationParams();

            if (oParams.size() > 1)
                this.r = ((Double) oParams.elementAt(1)).doubleValue();
        }
    }

    /**
     * Minkowski Distance implementation.
     *
     * @param paVector1 first vector to compare
     * @param paVector2 second vector to compare
     * @return Minkowski distance between two feature vectors
     */
    public final double distance(final double[] paVector1, final double[] paVector2) {
        double dDistance = 0;

        for (int f = 0; f < paVector1.length; f++)
            dDistance += Math.pow(Math.abs(paVector1[f] - paVector2[f]), this.r);

        return Math.pow(dDistance, 1 / this.r);
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     * @since 0.3.0
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.11 $";
    }
}

// EOF
