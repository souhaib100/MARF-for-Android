package marf.Stats.StatisticalEstimators.Smoothing;


/**
 * <p>Add-One Smoothing Estimator.</p>
 * <p>
 * $Id: AddOne.java,v 1.12 2006/01/15 06:58:08 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.12 $
 * @since 0.3.0.2
 */
public class AddOne
        extends AddDelta {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 3966754485295753364L;

    /**
     * Merely calls <code>super(1)</code>.
     *
     * @see AddDelta
     */
    public AddOne() {
        super(1);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.12 $";
    }
}

// EOF
