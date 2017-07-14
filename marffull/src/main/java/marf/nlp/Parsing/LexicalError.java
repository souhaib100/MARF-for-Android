/*
 * LexicalError Class
 * (C) 2001 - 2006 Serguei Mokhov, <mailto:mokhov@cs.concordia.ca>
 */

package marf.nlp.Parsing;

import java.io.FileWriter;
import java.io.IOException;


/**
 * <p>This class denotes a lexical error type.</p>
 * <p>
 * $Id: LexicalError.java,v 1.17 2006/01/15 20:51:55 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.17 $
 * @since 0.3.0.2 (in MARF)
 */
public class LexicalError
        extends CompilerError {
    /**
     * Character encountered is not part of the
     * MARF's alphabet.
     *
     * @since October 2, 2001
     */
    public static final int ERR_INVALID_CHAR = 1;

    /**
     * Leading zeros are not allowed in numbers.
     *
     * @since October 2, 2001
     */
    public static final int ERR_LEADING_ZEROS = 2;

    /**
     * Not meaningful trailing zeros aren't allowed.
     *
     * @since October 2, 2001
     */
    public static final int ERR_TRAILING_ZEROS = 3;

    /**
     * Badly formed real number.
     *
     * @since October 2, 2001
     */
    public static final int ERR_BAD_REAL = 4;

    /**
     * Invalid number format.
     *
     * @since October 2, 2001
     */
    public static final int ERR_INVALID_NUMBER_FORMAT = 5;

    /**
     * Unxepected EOF and no ending comment.
     *
     * @since December 20, 2001
     */
    public static final int ERR_UNEXPECTED_EOF = 6;

    /**
     * Mismatched comment.
     *
     * @since December 21, 2001
     */
    public static final int ERR_EXTRA_CLOSING_COMMENT = 7;

    /**
     * Custom error message as alterbntive to 'unknown'.
     *
     * @since October 2, 2001
     */
    public static final int ERR_CUSTOM = 8;

    /**
     * Standard set of lexical error messages.
     * Must correspond to the <code>ERR_</code> indexes above.
     *
     * @since October 2, 2001
     */
    private static final String LEXICAL_ERROR_MESSAGES[] =
            {
                    "OK",
                    "Invalid character encountered",
                    "Illegal leading zero(s) encountered",
                    "Illegal trailing zero(s) encountered",
                    "Badly formed real number",
                    "Invalid number format (number either too big or has non-digits)",
                    "Unexpected end of file. Please, seek for unterminated comment",
                    "Unmatched termintating comment",
                    "Custom error message: "
            };

    /**
     * Token information at which Lexer
     * encountered the error.
     *
     * @since October 2, 2001
     */
    protected Token oFaultingToken = null;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -3570759402876617546L;

    /**
     * Constructor with an error code and a faulting token.
     *
     * @param piErrorCode     the error code to indicate the message
     * @param poFaultingToken the token that caused the error
     */
    public LexicalError(int piErrorCode, Token poFaultingToken) {
        super(LEXICAL_ERROR_MESSAGES[piErrorCode]);
        this.iCurrentErrorCode = piErrorCode;
        this.iLineNo = poFaultingToken.getPosition().y;
        this.oFaultingToken = poFaultingToken;
    }

    /**
     * Constructor alternative to Unknown error given token and a custom message.
     *
     * @param poFaultingToken        the token that caused the error
     * @param pstrCustomErrorMessage custom error message string
     */
    public LexicalError(Token poFaultingToken, String pstrCustomErrorMessage) {
        super(LEXICAL_ERROR_MESSAGES[LexicalError.ERR_CUSTOM] + pstrCustomErrorMessage);
        this.iCurrentErrorCode = LexicalError.ERR_CUSTOM;
        this.iLineNo = poFaultingToken.getPosition().y;
        this.oFaultingToken = poFaultingToken;
    }

    /**
     * Constructs default generic lexical error.
     *
     * @since 0.3.0.5
     */
    public LexicalError() {
        super();
    }

    /**
     * Contructs a lexical error with wrapped exception.
     *
     * @param poException the wrapped exception
     * @since 0.3.0.5
     */
    public LexicalError(Exception poException) {
        super(poException);
    }

    /**
     * Constructs a lexical error with a custom error message string.
     *
     * @param pstrMessage the custom error message
     * @since 0.3.0.5
     */
    public LexicalError(String pstrMessage) {
        super(pstrMessage);
    }

    /**
     * Contructs a lexical error with wrapped exception and
     * a custom error message string.
     *
     * @param pstrMessage the custom error message
     * @param poException the wrapped exception
     * @since 0.3.0.5
     */
    public LexicalError(String pstrMessage, Exception poException) {
        super(pstrMessage, poException);
    }

    /**
     * Serialization routine.
     * Loading is not implemented.
     *
     * @param piOperation 0 for load and 1 for save
     * @param poWriter    writer object to use to write to
     * @return <code>true</code> if serialization was successful
     */
    public boolean serialize(int piOperation, FileWriter poWriter) {
        if (piOperation == 0) {
            // TODO load
            System.err.println("LexicalError::serialize(LOAD) - unimplemented");
            return false;
        } else {
            try {
                poWriter.write
                        (
                                "Lexical Error (line " + this.iLineNo + "): " + this.iCurrentErrorCode +
                                        " - " + this.strMessage + ", " +
                                        "faulting token: [" + this.oFaultingToken.getLexeme() + "]\n"
                        );

                return true;
            } catch (IOException e) {
                System.err.println("LexicalError::serialize() - " + e.getMessage());
                return false;
            }
        }
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.17 $";
    }
}

// EOF
