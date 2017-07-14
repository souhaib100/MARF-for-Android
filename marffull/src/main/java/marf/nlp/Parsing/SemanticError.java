package marf.nlp.Parsing;

import java.io.FileWriter;
import java.io.IOException;


/**
 * <p>Class SemanticError
 * Represents an instance of a sematic error
 * in the semantic analysis process.</p>
 * <p>
 * $Id: SemanticError.java,v 1.15 2006/01/15 20:37:41 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.15 $
 * @since 0.3.0.2
 */
public class SemanticError
        extends CompilerError {
    /**
     * Undefined Symbol.
     *
     * @since December 19, 2001
     */
    public static final int ERR_UNDEF_SYM = 1;

    /**
     * Symbol defined multiple times.
     *
     * @since December 19, 2001
     */
    public static final int ERR_MULT_DEF_SYM = 2;

    /**
     * Type mismatch in an expression.
     *
     * @since December 19, 2001
     */
    public static final int ERR_EXPR_TYPE_MISMATCH = 3;

    /**
     * Type mismatch in the assignment.
     *
     * @since December 19, 2001
     */
    public static final int ERR_ASSIGN_TYPE_MISMATCH = 4;

    /**
     * Mismatch of types of parameters in a function call.
     *
     * @since December 19, 2001
     */
    public static final int ERR_PARAM_TYPE_MISMATCH = 5;

    /**
     * Custom error message as alternative to 'unknown'.
     *
     * @since December 19, 2001
     */
    public static final int ERR_CUSTOM = 6;

    /**
     * Custom error messages.
     *
     * @since December 19, 2001
     */
    private static final String SEMANTIC_ERROR_MESSAGES[] =
            {
                    "OK",
                    "Unefined symbol",
                    "Redefined symbol",
                    "Type mismatch in expression",
                    "Type mismatch in assignment operation",
                    "Parameters type mismatch in a function call",
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
    private static final long serialVersionUID = 2985322185168851077L;

    /**
     * Constructor with an error code and a faulting token.
     *
     * @param piErrorCode     one of the defined error codes
     * @param poFaultingToken the semantic token that caused the error.
     */
    public SemanticError(int piErrorCode, Token poFaultingToken) {
        super(SEMANTIC_ERROR_MESSAGES[piErrorCode]);
        this.iCurrentErrorCode = piErrorCode;
        this.iLineNo = poFaultingToken.getPosition().y;
        this.oFaultingToken = poFaultingToken;
    }

    /**
     * Constructor alterantive to Unknown error with the custom error message.
     *
     * @param poFaultingToken        the semantic token that caused the error.
     * @param pstrCustomErrorMessage custom error message
     */
    public SemanticError(Token poFaultingToken, String pstrCustomErrorMessage) {
        super(SEMANTIC_ERROR_MESSAGES[SemanticError.ERR_CUSTOM] + pstrCustomErrorMessage);
        this.iCurrentErrorCode = SemanticError.ERR_CUSTOM;
        this.iLineNo = poFaultingToken.getPosition().y;
        this.oFaultingToken = poFaultingToken;
    }

    /**
     * Default constructor.
     *
     * @since 0.3.0.5
     */
    public SemanticError() {
        super();
    }

    /**
     * Wraps an exception.
     *
     * @param poException exception to wrap
     * @since 0.3.0.5
     */
    public SemanticError(Exception poException) {
        super(poException);
    }

    /**
     * Construct just given the error message
     *
     * @param pstrMessage custom error message
     * @since 0.3.0.5
     */
    public SemanticError(String pstrMessage) {
        super(pstrMessage);
    }

    /**
     * Wraps an exception with a custom error message
     *
     * @param pstrMessage custom error message
     * @param poException exception to wrap
     * @since 0.3.0.5
     */
    public SemanticError(String pstrMessage, Exception poException) {
        super(pstrMessage, poException);
    }

    /**
     * Serialization routine.
     * TODO: migrate to MARF's dump/restore mechanism
     *
     * @param piOperation  0 means load (not implemented) and 1 means save
     * @param poFileWriter writer object to write the error message to
     * @return <code>true</code> if the operation was successful
     */
    public boolean serialize(int piOperation, FileWriter poFileWriter) {
        if (piOperation == 0) {
            // TODO load
            System.err.println("SemanticalError::serialize(LOAD) - unimplemented");
            return false;
        } else {
            try {
                poFileWriter.write
                        (
                                "Semantic Error (line " + this.iLineNo + "): " + this.iCurrentErrorCode +
                                        " - " + this.strMessage + ", " +
                                        "faulting token: [" + this.oFaultingToken.getLexeme() + "]\n"
                        );

                return true;
            } catch (IOException e) {
                System.err.println("SemanticalError::serialize() - " + e.getMessage());
                e.printStackTrace(System.err);
                return false;
            }
        }
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
