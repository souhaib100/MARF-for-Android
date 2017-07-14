package marf.util;


/**
 * <p>Class InvalidSampleFormatException typically signals
 * a mismatch of a loader and file being loader or sample type
 * and its data.</p>
 * <p>
 * <p>$Id: InvalidSampleFormatException.java,v 1.11 2005/12/28 03:21:12 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.11 $
 * @since 0.0.1
 */
public class InvalidSampleFormatException
        extends MARFException {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -4262507209933509360L;

    /**
     * Exception for specific sample format.
     *
     * @param piFormat Format number, caused the exception to be thrown
     */
    public InvalidSampleFormatException(int piFormat) {
        super("Invalid sample file format: " + piFormat);
    }

    /**
     * Generic exception.
     *
     * @param pstrMsg error message only
     */
    public InvalidSampleFormatException(String pstrMsg) {
        super(pstrMsg);
    }

    /**
     * Default InvalidSampleFormat exception with empty message.
     *
     * @since 0.3.0.5
     */
    public InvalidSampleFormatException() {
        super();
    }

    /**
     * InvalidSampleFormat exception with wrapped exception of another type.
     *
     * @param poException the exception to wrap
     * @since 0.3.0.5
     */
    public InvalidSampleFormatException(Exception poException) {
        super(poException);
    }

    /**
     * InvalidSampleFormat exception with wrapped exception of another type
     * and a customized error message.
     *
     * @param pstrMessage the customized message
     * @param poException the exception to wrap
     * @since 0.3.0.5
     */
    public InvalidSampleFormatException(String pstrMessage, Exception poException) {
        super(pstrMessage, poException);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.11 $";
    }
}

// EOF
