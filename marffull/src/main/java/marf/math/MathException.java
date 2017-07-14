package marf.math;

import marf.util.MARFException;

/**
 * <p>Indicates exceptional situations in MARF's math.</p>
 * <p>
 * <p>$Id: MathException.java,v 1.5 2005/08/13 23:09:39 susan_fan Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.5 $
 * @since 0.3.0
 */
public class MathException
        extends MARFException {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -5874565130017306067L;

    /**
     * Encapsulation of another Exception object.
     *
     * @param poException Exception to wrap around
     */
    public MathException(Exception poException) {
        super(poException);
    }

    /**
     * Encapsulation of another Exception object and a new message.
     *
     * @param pstrMessage additional information to add
     * @param poException Exception to wrap around
     */
    public MathException(String pstrMessage, Exception poException) {
        super(pstrMessage, poException);
    }

    /**
     * Generic exception.
     *
     * @param pstrMessage Error message string
     */
    public MathException(String pstrMessage) {
        super(pstrMessage);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.5 $";
    }
}

// EOF
