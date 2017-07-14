/*
 * SyntaxError Class
 * (C) 2001 - 2006 Serguei Mokhov, <mailto:mokhov@cs.concordia.ca>
 */

package marf.nlp.Parsing;

import java.io.FileWriter;
import java.io.IOException;


/**
 * <p>Represents a Syntax Error.
 * </p>
 * <p>
 * $Id: SyntaxError.java,v 1.18 2006/01/15 20:51:55 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.18 $
 * @since 0.3.0.2
 */
public class SyntaxError
        extends CompilerError {
    /**
     * General Syntax Error.
     */
    public static final int ERR_GENERAL_SYNTAX_ERROR = 1;

    /**
     * Mismatched Parenthesis Error.
     */
    public static final int ERR_MISMATCHED_PARENS = 2;

    /**
     * No "main()" program Error.
     */
    public static final int ERR_NO_PROGRAM = 3;

    /**
     * Unterminated statement or declaration.
     */
    public static final int ERR_MISSING_SEMICOLON = 4;

    /**
     * Custom error message as alternative to 'unknown'.
     */
    public static final int ERR_CUSTOM = 5;

    /**
     * Typical syntax error messages.
     *
     * @since October 2, 2001
     */
    private static final String SYNTAX_ERROR_MESSAGES[] =
            {
                    "OK",
                    "Syntax Error",
                    "Mismatched parenthesis",
                    "No main 'program' has been found",
                    "Missing semicolon at the end of a stament",
                    "Custom error message: "
            };

    /**
     * Token information at which Parser
     * encountered the error.
     *
     * @since October 2, 2001
     */
    protected Token oFaultingToken = null;

    /**
     * Recovery action code, specifying
     * that to recover from an error one needs
     * to pop tokens from the stack.
     */
    protected static final int POP = 1;

    /**
     * Recovery action code specifying
     * that one needs to keep scanning to
     * recover.
     */
    protected static final int SCAN = 2;

    /**
     * Recovery action code specifies which action
     * to take to recover from this error.
     */
    private int iRecoveryActionCode = SCAN;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -7701158080244875322L;

    /**
     * Constructor with the error code.
     *
     * @param piErrorCode type of syntax error
     */
    public SyntaxError(int piErrorCode) {
        super(SYNTAX_ERROR_MESSAGES[piErrorCode]);
        this.iCurrentErrorCode = piErrorCode;
    }

    /**
     * Constructor with the error code and its cause.
     *
     * @param piErrorCode     type of syntax error
     * @param poFaultingToken the token that caused it
     */
    public SyntaxError(int piErrorCode, Token poFaultingToken) {
        super(SYNTAX_ERROR_MESSAGES[piErrorCode]);
        this.iCurrentErrorCode = piErrorCode;
        this.iLineNo = poFaultingToken.getPosition().y;
        this.oFaultingToken = poFaultingToken;
    }

    /**
     * Constructor 2 - An Alterantive to Unknown Error.
     *
     * @param poFaultingToken        the token that caused the error
     * @param pstrCustomErrorMessage user-defined error message
     */
    public SyntaxError(Token poFaultingToken, String pstrCustomErrorMessage) {
        super(SYNTAX_ERROR_MESSAGES[ERR_CUSTOM] + pstrCustomErrorMessage);
        this.iCurrentErrorCode = ERR_CUSTOM;
        this.iLineNo = poFaultingToken.getPosition().y;
        this.oFaultingToken = poFaultingToken;
    }

    /**
     * Constructs a syntax error with the encoded code.
     *
     * @param pstrEncodedCode the string code.
     */
    public SyntaxError(String pstrEncodedCode) {
        setErrorCode(pstrEncodedCode);
    }

    /**
     * Default constructor that merely calls <code>super()</code>.
     *
     * @since 0.3.0.5
     */
    public SyntaxError() {
        super();
    }

    /**
     * Constructs SyntaxError with wrapped Exception object.
     *
     * @param poException the exception to wrap
     * @since 0.3.0.5
     */
    public SyntaxError(Exception poException) {
        super(poException);
    }

    /**
     * Constructs SyntaxError with wrapped Exception object
     * and a custom error message.
     *
     * @param pstrMessage the custom error message
     * @param poException the exception to wrap
     * @since 0.3.0.5
     */
    public SyntaxError(String pstrMessage, Exception poException) {
        super(pstrMessage, poException);
    }

    /**
     * Sets error code given its string representation (as in token's lexeme).
     *
     * @param pstrCode the string code to convert to numeric
     */
    public void setErrorCode(final String pstrCode) {
        if (pstrCode.equals("$GENERAL_SYNTAX_ERROR")) {
            this.iCurrentErrorCode = ERR_GENERAL_SYNTAX_ERROR;
        } else if (pstrCode.equals("$MISMATCHED_PARENS")) {
            this.iCurrentErrorCode = ERR_MISMATCHED_PARENS;
        } else if (pstrCode.equals("$NO_PROGRAM")) {
            this.iCurrentErrorCode = ERR_NO_PROGRAM;
        } else if (pstrCode.equals("$MISSING_SEMICOLON")) {
            this.iCurrentErrorCode = ERR_MISSING_SEMICOLON;
        } else if (pstrCode.equals("$CUSTOM")) {
            this.iCurrentErrorCode = ERR_CUSTOM;
        } else {
            this.iCurrentErrorCode = ERR_CUSTOM;
            this.strMessage = " Undetermined error.";
        }
    }

    /**
     * Allows updating the state of an error,
     * since the info might not be initially available
     * (as in the TT, for example).
     *
     * @param poFaultingToken the faulting token that caused the error
     */
    public void updateTokenInfo(Token poFaultingToken) {
        this.iLineNo = poFaultingToken.getPosition().y;
        this.oFaultingToken = poFaultingToken;
    }

    /**
     * Updates location.
     *
     * @param piLineNo the line number of the error
     */
    public void setLineNo(int piLineNo) {
        this.iLineNo = piLineNo;
    }

    /**
     * Serialization routine.
     * TODO: migrate to MARF dump/restore mechanism.
     *
     * @param piOperation  0 for load (not implemented), 1 for save as text
     * @param poFileWriter writer to write the error message to
     * @return <code>true</code> if the operation was successful
     */
    public boolean serialize(int piOperation, FileWriter poFileWriter) {
        if (piOperation == 0) {
            // TODO load
            System.err.println("LexicalError::serialize(LOAD) - unimplemented");
            return false;
        } else {
            try {
                poFileWriter.write
                        (
                                "Syntax Error (line " + this.iLineNo + "): " + this.iCurrentErrorCode +
                                        " - " + this.strMessage + ", " +
                                        "faulting token: [" + this.oFaultingToken.getLexeme() + "]\n"
                        );

                return true;
            } catch (IOException e) {
                System.err.println("SyntaxError::serialize() - " + e.getMessage());
                e.printStackTrace(System.err);
                return false;
            }
        }
    }

    /**
     * Checks whether the recovery action to follow this syntax error
     * is to "keep scanning".
     *
     * @return <code>true</code> if the action is SCAN
     * @see #SCAN
     */
    public boolean isScanRecoveryAction() {
        return this.iRecoveryActionCode == SCAN;
    }

    /**
     * Checks whether the recovery action to follow this syntax error
     * is to "pop last token from the stack".
     *
     * @return <code>true</code> if the action is POP
     * @see #POP
     */
    public boolean isPopRecoveryAction() {
        return this.iRecoveryActionCode == POP;
    }

    /**
     * Sets current error recovery action to SCAN.
     *
     * @see #SCAN
     */
    public void setScanRecoveryAction() {
        this.iRecoveryActionCode = SCAN;
    }

    /**
     * Sets current error recovery action to SCAN.
     *
     * @see #POP
     */
    public void setPopRecoveryAction() {
        this.iRecoveryActionCode = POP;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.18 $";
    }
}

// EOF
