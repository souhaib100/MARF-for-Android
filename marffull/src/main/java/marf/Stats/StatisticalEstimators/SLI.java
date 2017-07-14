package marf.Stats.StatisticalEstimators;

import marf.Stats.Ngram;

/**
 * <p>SLI Statistical Estimator.
 * TODO: complete.</p>
 * <p>
 * $Id: SLI.java,v 1.16 2006/01/15 01:50:57 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.16 $
 * @since 0.3.0.2
 */
public class SLI
        extends StatisticalEstimator {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -4349244415620171096L;

    /**
     * Default constructor as in parent.
     */
    public SLI() {
        super();
    }

/*
    public double p()
	{
		double dProbability = 0.0;
		return dProbability;
	}
*/

    /**
     * Not implemented.
     *
     * @param poNgram
     * @return 0.0
     */
    public double p(Ngram poNgram) {
        return 0.0;
    }

    /**
     * Not implemented.
     *
     * @see marf.Stats.StatisticalEstimators.IStatisticalEstimator#train()
     */
    public boolean train() {
        return false;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.16 $";
    }
}

// EOF
