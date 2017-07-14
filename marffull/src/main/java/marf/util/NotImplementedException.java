package marf.util;


/**
 * <p>This class extends RuntimeException for MARF unimplemented parts.</p>
 * <p>
 * <p>$Id: NotImplementedException.java,v 1.14 2005/08/11 00:44:50 mokhov Exp $</p>
 *
 * @author Serguei A. Mokhov
 * @version $Revision: 1.14 $
 * @since 0.0.1
 */
public class NotImplementedException
        extends RuntimeException {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -52979270946708931L;

    /**
     * Generic exception.
     *
     * @since 0.3.0.3
     */
    public NotImplementedException() {
        super("not implemented");
    }

    /**
     * Generic exception.
     *
     * @param pstrMessage Error message string
     */
    public NotImplementedException(String pstrMessage) {
        super("Not implemented: " + pstrMessage);
    }

    /**
     * Generates Class.Method exception message.
     *
     * @param poObject   object to query for class name that has something unimplemented
     * @param pstrMethod method name that is not implemented
     */
    public NotImplementedException(final Object poObject, String pstrMethod) {
        this(poObject.getClass().getName() + "." + pstrMethod);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.14 $";
    }
}

// EOF
