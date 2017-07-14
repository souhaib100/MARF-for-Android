package marf.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;


/**
 * <p>Class MARFException.</p>
 * <p>This class extends Exception for MARF specifics.</p>
 * <p>
 * <p>$Id: MARFException.java,v 1.19 2005/08/11 00:44:50 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.19 $
 * @since 0.0.1
 */
public class MARFException
        extends Exception {
    /**
     * Our own error message container.
     * Needed as we don't have access to the parent's.
     * Initially an empty string.
     */
    protected String strMessage = "";

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -36051376447916491L;

    /**
     * Default MARF exception.
     * Better be overridden for normal internal message.
     *
     * @since 0.3.0.3
     */
    public MARFException() {
        this("Just a MARF Exception");
    }

    /**
     * Generic exception.
     *
     * @param pstrMessage Error message string
     */
    public MARFException(String pstrMessage) {
        super(pstrMessage);
        this.strMessage = pstrMessage;
    }

    /**
     * This is used for debug purposes only with some unusual Exception's.
     * It allows the originiating Exceptions stack trace to be returned.
     *
     * @param pstrMessage Error message string
     * @param poException Exception object to dump
     */
    public MARFException(String pstrMessage, Exception poException) {
        super(pstrMessage);

        // Based on PostgreSQL JDBC driver's PGSQLException.
        try {
            ByteArrayOutputStream oByteArrayOutputStream = new ByteArrayOutputStream();
            PrintWriter oPrintWriter = new PrintWriter(oByteArrayOutputStream);

            oPrintWriter.println("Exception: " + poException + "\nStack Trace:\n");
            poException.printStackTrace(oPrintWriter);
            oPrintWriter.println("End of Stack Trace");

            this.strMessage += pstrMessage + " " + oByteArrayOutputStream;

            oPrintWriter.println(this.strMessage);

            oPrintWriter.flush();
            oPrintWriter.close();

            oByteArrayOutputStream.close();
        } catch (Exception ioe) {
            this.strMessage +=
                    pstrMessage + " " +
                            poException +
                            "\nIO Error on stack trace generation! " +
                            ioe;
        }
    }

    /**
     * Wraps Exception object around.
     *
     * @param poException Exception to wrap around
     * @since 0.3.0.2
     */
    public MARFException(Exception poException) {
        this(poException.getMessage(), poException);
    }

    /**
     * Returns string representation of the error message.
     *
     * @return error string
     */
    public final String getMessage() {
        return this.strMessage;
    }

    /**
     * Override <code>toString()</code> to display our message.
     *
     * @return string representation of this exception
     * @since 0.3.0.3
     */
    public String toString() {
        return getMessage();
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.19 $";
    }
}

// EOF
