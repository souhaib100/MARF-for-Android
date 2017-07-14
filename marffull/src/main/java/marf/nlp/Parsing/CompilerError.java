/*
 * CompilerError Class.
 * (C) 2001 - 2006 Serguei Mokhov, <mailto:mokhov@cs.concordia.ca>
 */

package marf.nlp.Parsing;

import marf.nlp.NLPException;


/**
 * <p>Generic Compiler Error.
 * Normally subclassed to differentiate
 * between various error types like
 * lexical, syntax, semantic and such.</p>
 * <p>
 * $Id: CompilerError.java,v 1.16 2006/01/15 20:51:55 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.16 $
 * @since 0.3.0.2
 */
public class CompilerError
        extends NLPException {
    /**
     * Error code signifying "no error".
     *
     * @since October 2, 2001
     */
    public static final int OK = 0;

    /**
     * Error code of the last error occured.
     *
     * @since October 2, 2001
     */
    protected int iCurrentErrorCode = OK;

    /**
     * Line number where the given error occurred.
     * The default value of (-1) means it was not yet initialized.
     *
     * @since October 2, 2001
     */
    protected int iLineNo = -1;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -6425511198047596019L;

    /**
     * Default Constructor.
     *
     * @since October 2, 2001
     */
    public CompilerError() {
        super("CompilerError");
    }

    /**
     * Wraps the parameter exception into CompilerError.
     *
     * @param poException the exception object to wrap
     */
    public CompilerError(Exception poException) {
        super(poException);
    }

    /**
     * Creates a CompilerError with a custom error message string.
     *
     * @param pstrMessage the custom error message
     */
    public CompilerError(String pstrMessage) {
        super(pstrMessage);
    }

    /**
     * Wraps the parameter exception into CompilerError
     * with a custom error message string.
     *
     * @param pstrMessage the custom error message
     * @param poException the exception object to wrap
     */
    public CompilerError(String pstrMessage, Exception poException) {
        super(pstrMessage, poException);
    }

    /**
     * Access method for the current error code property.
     *
     * @return the current value of the iCurrentErrorCode property
     */
    public int getCurrentErrorCode() {
        return this.iCurrentErrorCode;
    }

    /**
     * Access method for the line number property.
     *
     * @return the current value of the iLineNo property
     */
    public int getLineNo() {
        return this.iLineNo;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.16 $";
    }
}
