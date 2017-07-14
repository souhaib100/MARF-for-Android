package marf.Stats.StatisticalEstimators;

import java.util.Vector;

import marf.Stats.Observation;
import marf.Stats.StatisticalEstimators.Smoothing.AddDelta;

/**
 * <p>Maximum-Likelihood Statistical Estimator (MLE).</p>
 * <p>
 * $Id: MLE.java,v 1.16 2006/01/15 01:50:57 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.16 $
 * @since 0.3.0.2
 */
public class MLE
        extends AddDelta {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -2490823052719390594L;

    /**
     * Default construct with the call <code>super(0)</code>.
     *
     * @see AddDelta
     */
    public MLE() {
        // delta = 0 for MLE
        super(0);
    }

    /**
     * Not implemented. TODO: implement.
     *
     * @param poEvent
     * @return 0
     */
    public double p(Observation poEvent) {
        return 0;
    }

    /**
     * Not implemented. TODO: implement.
     *
     * @param poEventList
     * @return 0
     */
    public double p(Vector poEventList) {
        return 0;
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
