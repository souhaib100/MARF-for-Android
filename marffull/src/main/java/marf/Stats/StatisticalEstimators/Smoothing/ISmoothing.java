package marf.Stats.StatisticalEstimators.Smoothing;


/**
 * <p>Generic Smoothing Interface. All smoothing statistical
 * estimators should extend the <code>Smoothing</code> class.
 * If they can't, they must implement this interface.
 * </p>
 * <p>
 * $Id: ISmoothing.java,v 1.2 2006/01/15 06:58:08 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.2 $
 * @see Smoothing
 * @since 0.3.0.3
 */
public interface ISmoothing {
    /**
     * Interface source code revision.
     */
    String MARF_INTERFACE_CODE_REVISION = "$Revision: 1.2 $";

    /**
     * General smoothing routine.
     *
     * @return <code>true</code> if any smoothing took place
     * and undelying data was altered
     */
    boolean smooth();
}

// EOF
