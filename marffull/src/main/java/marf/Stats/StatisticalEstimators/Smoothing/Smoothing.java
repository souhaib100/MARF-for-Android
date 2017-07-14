package marf.Stats.StatisticalEstimators.Smoothing;

import marf.Stats.StatisticalEstimators.StatisticalEstimator;


/**
 * <p>Generic Smoothing Estimator implementation.
 * Most statistical smoothing classes should extend
 * this class. If they can't, they must implement
 * the <code>ISmoothing</code> interface.
 * TODO: complete exception handling.
 * </p>
 * <p>
 * $Id: Smoothing.java,v 1.15 2006/01/15 06:58:08 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.15 $
 * @see ISmoothing
 * @since 0.3.0.2
 */
public abstract class Smoothing
        extends StatisticalEstimator
        implements ISmoothing {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.5
     */
    private static final long serialVersionUID = 3404406955334414583L;

    /**
     * Default constructor with a call to the parent.
     */
    public Smoothing() {
        super();
    }

    /**
     * This generic implementation first calls <code>super.train()</code>,
     * then <code>smooth()</code>, and then <code>dump()</code>.
     *
     * @see marf.Stats.StatisticalEstimators.IStatisticalEstimator#train()
     */
    public boolean train() {
        try {
            super.train();
            smooth();
            dump();

            //this.oProbabilityTable.dumpCSV();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);

            return false;
        }

        return true;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.15 $";
    }
}

// EOF
