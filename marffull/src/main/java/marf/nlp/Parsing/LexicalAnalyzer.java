/*
 * LexicalAnalyzer Class
 * (C) 2001 - 2006 Serguei Mokhov, <mailto:mokhov@cs.concordia.ca>
 */

package marf.nlp.Parsing;

import net.windward.android.awt.Point;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Enumeration;
import java.util.Vector;

import marf.util.Debug;


/**
 * <p>LexicalAnalyzer class encapsulates the functionality
 * required for lexical analysis of a MARF source program.</p>
 * <p>
 * $Id: LexicalAnalyzer.java,v 1.18 2006/01/19 04:13:16 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.18 $
 * @since 0.3.0.2
 */
public class LexicalAnalyzer
        extends GenericLexicalAnalyzer {
    /**
     * Just a constant used to return an error
     * code "Not Applicable" when no error code
     * applicable in a given situation.
     */
    protected static final int NA = -1;

    /**
     * This flag is used for backtracking
     * when recognizing numbers.
     */
    protected boolean bNumberMode = false;

    /**
     * This flag is used for backtracking
     * when recognizing 'ambiguous' operators
     * such as '<>' and '<' and friends.
     */
    protected boolean bOperatorMode = false;

    /**
     * Constructor with the symbol table as a reference.
     *
     * @param poSymTab the symbol table to construct this lexical analyzer
     */
    public LexicalAnalyzer(SymbolTable poSymTab) {
        super(poSymTab);
    }

    /**
     * Initialization routine.
     *
     * @see marf.nlp.Parsing.GenericLexicalAnalyzer#init()
     */
    public boolean init() {
        if (super.init() == true) {
            // Make all characters ordinary
            this.oStreamTokenizer.resetSyntax();

            // EOL is not a token
            this.oStreamTokenizer.eolIsSignificant(false);

            // /**/ Are C-type coments
            // Disable them, do it manually
            // for unerminated comment detectiobn
            this.oStreamTokenizer.slashStarComments(false);

            // No C++ '//' comments
            this.oStreamTokenizer.slashSlashComments(false);

            // Strip whitespaces
            this.oStreamTokenizer.whitespaceChars('\u0000', '\u0020');

            // Case-sensitive
            this.oStreamTokenizer.lowerCaseMode(false);

            // Range of chars treated as token
            // chars, with some
            this.oStreamTokenizer.wordChars('0', '\u00A0');

            // Valid ordinary characters
            this.oStreamTokenizer.ordinaryChar('.');
            this.oStreamTokenizer.ordinaryChar(';');
            this.oStreamTokenizer.ordinaryChar('>');
            this.oStreamTokenizer.ordinaryChar('<');
            this.oStreamTokenizer.ordinaryChar('=');
            this.oStreamTokenizer.ordinaryChar('{');
            this.oStreamTokenizer.ordinaryChar('}');
            this.oStreamTokenizer.ordinaryChar('[');
            this.oStreamTokenizer.ordinaryChar(']');

            // Invalid ordinary characters
            this.oStreamTokenizer.ordinaryChar('@');

            // Made these word chars to distinguish comments
            this.oStreamTokenizer.wordChars('/', '/');
            this.oStreamTokenizer.wordChars('*', '*');

            return true;
        } else {
            System.err.println("MARF Lexer init failed...");
            return false;
        }
    }

    /**
     * Load/Save the textual contents of such as Token list and Error list.
     * 0 means load, 1 means save.
     * Load currently is not implemented.
     * TODO: migrate to MARF's dump/restore machinery.
     *
     * @see marf.nlp.Parsing.GenericLexicalAnalyzer#serialize(int)
     */
    public boolean serialize(int piOperation) {
        boolean bSuccess = true;

        if (piOperation == 0) {
            // TODO Load
            System.err.println("LexicalAnalyzer::serialize(LOAD) - unimplemented");
            return false;
        } else {
            // Write down scan results
            try {
                FileWriter oFileWriterScan = new FileWriter(this.strOutputFilename);

                oFileWriterScan.write
                        (
                                "-----------------------------------\n" +
                                        "MARF Lexical Analysis Results\n" +
                                        "Source file : \"" + this.strSourceFilename + "\"\n" +
                                        "Total tokens: " + this.oTokenList.size() + "\n" +
                                        "Total errors: " + this.oLexicalErrors.size() + "\n" +
                                        "-----------------------------------\n\n"
                        );

                oFileWriterScan.write
                        (
                                "Synopsis:\n" +
                                        "  LINE#: TOKEN TYPE, [ lexeme ], TOKEN SUBTYPE\n\n"
                        );

                Enumeration oTokenListEnum = this.oTokenList.elements();

                while (oTokenListEnum.hasMoreElements()) {
                    Token oNextToken = (Token) oTokenListEnum.nextElement();
                    oNextToken.serialize(piOperation, oFileWriterScan);
                }

                oFileWriterScan.flush();
            } catch (IOException e) {
                System.err.println("LexicalAnalyzer::serialize() - " + e.getMessage());
                e.printStackTrace(System.err);
                bSuccess = false;
            }

            // Write down errors (if any)
            try {
                if (this.oLexicalErrors.size() > 0) {
                    FileWriter oFileWriterError = new FileWriter(this.strErrorLogFilename);

                    oFileWriterError.write
                            (
                                    "-----------------------------------\n" +
                                            "MARF Lexical Analysis Errors\n" +
                                            "Source file: \"" + this.strSourceFilename + "\"\n" +
                                            "Lexical errors: " + this.oLexicalErrors.size() + "\n" +
                                            "-----------------------------------\n\n"
                            );

                    oFileWriterError.write
                            (
                                    "Synopsis:\n" +
                                            "   Complier Component (line#): msg# - Message Description[, faulting token: [lexeme]]\n\n"
                            );

                    Enumeration oErrorList = this.oLexicalErrors.elements();

                    while (oErrorList.hasMoreElements()) {
                        LexicalError oError = (LexicalError) oErrorList.nextElement();
                        oError.serialize(piOperation, oFileWriterError);
                    }

                    oFileWriterError.flush();
                }
            } catch (IOException e) {
                System.err.println("LexicalAnalyzer::serialize() - " + e.getMessage());
                e.printStackTrace(System.err);
                bSuccess = false;
            }
        }

        return bSuccess;
    }


    /**
     * Core method of the MARF LexicalAnalyzer.
     *
     * @see marf.nlp.Parsing.GenericLexicalAnalyzer#getNextToken()
     */
    public Token getNextToken()
            throws LexicalError {
        Token oCurrentToken = null;

        // To check numerical errors
        int iNumCheckCode;

        try {
            int iTokenType = this.oStreamTokenizer.nextToken();

            TokenSubType oTokenSubType = new TokenSubType();

            switch (iTokenType) {
                case StreamTokenizer.TT_EOF: {
                    // Signal EOF
                    oTokenSubType.iType = TokenType.EOF;
                    oTokenSubType.iSubtype = TokenType.UNKNOWN;
                    oCurrentToken = createToken("$", oTokenSubType);
                    break;
                }

                case StreamTokenizer.TT_WORD: {
                    Debug.debug("LexicalAnalyzer [TT_WORD]: " + this.oStreamTokenizer.sval);

                    // Stripping out comments manually

                    if (this.oStreamTokenizer.sval.compareTo("/**/") == 0) {
                        return getNextToken();
                    }

                    if (this.oStreamTokenizer.sval.length() >= 2) {
                        if (this.oStreamTokenizer.sval.charAt(0) == '/' && this.oStreamTokenizer.sval.charAt(1) == '*') {
                            // Remember line no if comment
                            // does not match, report it in the error message
                            int iLineNo = this.oStreamTokenizer.lineno();

                            boolean bLookingForEndComment = true;
                            boolean bUnexpectedEOF = false;

                            while (bLookingForEndComment && !bUnexpectedEOF) {
                                // Let's waste some tokens...
                                int iCommentToken = this.oStreamTokenizer.nextToken();

                                // Oops, unexpected EOF
                                if (iCommentToken == StreamTokenizer.TT_EOF) {
                                    bUnexpectedEOF = true;
                                }

                                // OK, comments end
                                if (iCommentToken == StreamTokenizer.TT_WORD) {
                                    //if((oStreamTokenizer.sval.compareTo("*/") == 0))
                                    if (this.oStreamTokenizer.sval.length() >= 2) {
                                        if (this.oStreamTokenizer.sval.charAt(0) == '*' && this.oStreamTokenizer.sval.charAt(1) == '/') {
                                            bLookingForEndComment = false;
                                        }
                                    }
                                }
                            }

                            if (bUnexpectedEOF) {
                                oTokenSubType.iType = TokenType.ERROR;
                                oTokenSubType.iSubtype = TokenType.UNKNOWN;
                                oCurrentToken = createToken("$", oTokenSubType);
                                oCurrentToken.setPosition(new Point(0, iLineNo));
                                this.bErrorsPresent = true;
                                this.oLexicalErrors.addElement(new LexicalError(LexicalError.ERR_UNEXPECTED_EOF, oCurrentToken));
                            }

                            return getNextToken();
                        }

                        //else if(oStreamTokenizer.sval.compareTo("*/") == 0)
                        else if (this.oStreamTokenizer.sval.charAt(0) == '/' && this.oStreamTokenizer.sval.charAt(1) == '*') {
                            oTokenSubType.iType = TokenType.ERROR;
                            oTokenSubType.iSubtype = TokenType.UNKNOWN;
                            oCurrentToken = createToken("*/", oTokenSubType);
                            this.bErrorsPresent = true;
                            this.oLexicalErrors.addElement(new LexicalError(LexicalError.ERR_EXTRA_CLOSING_COMMENT, oCurrentToken));
                            return getNextToken();
                        }
                    }

                    // Here TT_WORD means all alpha-numerical
                    // characters
                    if (TokenSubType.soKeywords.containsKey(this.oStreamTokenizer.sval)) {
                        oTokenSubType.iType = TokenType.KEYWORD;
                        oTokenSubType.iSubtype = ((Integer) TokenSubType.soKeywords.get(this.oStreamTokenizer.sval)).intValue();
                        oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                    }

                    // Operators
                    else if (TokenSubType.soOperators.containsKey(this.oStreamTokenizer.sval)) {
                        oTokenSubType.iType = TokenType.OPERATOR;
                        oTokenSubType.iSubtype = ((Integer) TokenSubType.soOperators.get(this.oStreamTokenizer.sval)).intValue();
                        oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                    }

                    // Punctuation
                    else if (TokenSubType.soValidPunctuation.containsKey(this.oStreamTokenizer.sval)) {
                        oTokenSubType.iType = TokenType.PUNCT;
                        oTokenSubType.iSubtype = ((Integer) TokenSubType.soValidPunctuation.get(this.oStreamTokenizer.sval)).intValue();
                        oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                    }

                    // Bracketing
                    else if (TokenSubType.soBrackets.containsKey(this.oStreamTokenizer.sval)) {
                        oTokenSubType.iType = TokenType.BRACKET;
                        oTokenSubType.iSubtype = ((Integer) TokenSubType.soBrackets.get(this.oStreamTokenizer.sval)).intValue();
                        oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                    }

                    // Numbers
                    else if ((iNumCheckCode = validNum(this.oStreamTokenizer.sval)) != NA) {
                        if (this.bOperatorMode == true) {
                            this.bNumberMode = false;
                            this.oStreamTokenizer.pushBack();
                            oTokenSubType.iType = TokenType.BACKTRACK;
                            oTokenSubType.iSubtype = TokenType.UNKNOWN;
                            oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                        }

                        switch (iNumCheckCode) {
                            case LexicalError.OK: {
                                // Some step forward to look ahead if
                                // in case the number is real

                                int iNextTokenType = 0;
                                String strBackup = this.oStreamTokenizer.sval;

                                if (this.bNumberMode == false) {
                                    iNextTokenType = this.oStreamTokenizer.nextToken();
                                }

                                if ((char) iNextTokenType == '.') {
                                    if (this.bNumberMode == false) {
                                        this.bNumberMode = true;
                                        Token oSecondPartToken = getNextToken();

                                        if (oSecondPartToken.getTokenType().iType == TokenType.NUM) {
                                            oTokenSubType.iType = TokenType.NUM;
                                            oTokenSubType.iSubtype = TokenSubType.REAL;
                                            this.oStreamTokenizer.sval = strBackup + "." + oSecondPartToken.getLexeme();
                                            this.bNumberMode = false;
                                            oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                                        } else {
                                            oTokenSubType.iType = TokenType.ERROR;
                                            oTokenSubType.iSubtype = TokenType.UNKNOWN;
                                            this.bErrorsPresent = true;
                                            oCurrentToken = createToken(strBackup + "." + oSecondPartToken.getLexeme(), oTokenSubType);
                                            this.oLexicalErrors.addElement(new LexicalError(LexicalError.ERR_BAD_REAL, oCurrentToken));
                                            this.bNumberMode = false;
                                            break;
                                        }
                                    }
                                } else {
                                    if (this.bNumberMode == false) {
                                        this.oStreamTokenizer.pushBack();
                                    }

                                    this.oStreamTokenizer.sval = strBackup;

                                    oTokenSubType.iType = TokenType.NUM;

                                    oTokenSubType.iSubtype = this.bNumberMode ? TokenSubType.REAL : TokenSubType.INTEGER;

                                    oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                                }

                                break;
                            }

							/*
                             * Lexical Error handling for numbers
							 */
                            case LexicalError.ERR_INVALID_NUMBER_FORMAT:
                            case LexicalError.ERR_LEADING_ZEROS:
                            case LexicalError.ERR_TRAILING_ZEROS: {
                                oTokenSubType.iType = TokenType.ERROR;
                                oTokenSubType.iSubtype = TokenType.UNKNOWN;
                                this.bErrorsPresent = true;
                                oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                                this.oLexicalErrors.addElement(new LexicalError(iNumCheckCode, oCurrentToken));
                                this.bNumberMode = false;
                                break;
                            }

                            default: {
                                throw new LexicalError
                                        (
                                                "LexicalAnalyzer::getNextToken() - Unknown lexical error type encountered: " + iNumCheckCode
                                        );
                            }
                        }

                        break;
                    }

					/*
                     * TODO: Comments (manually)
					 * Check for unterminated comments
					 */

                    // User's IDs
                    else {
                        oTokenSubType.iType = TokenType.ID;
                        oTokenSubType.iSubtype = TokenType.UNKNOWN;
                        oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                    }

                    break;
                }

                default: {
                    Debug.debug("LexicalAnalyzer [default]: " + (char) this.oStreamTokenizer.ttype);

                    // Punctuation
                    if (TokenSubType.soValidPunctuation.containsKey(String.valueOf((char) this.oStreamTokenizer.ttype))) {
                        oTokenSubType.iType = TokenType.PUNCT;
                        oTokenSubType.iSubtype = ((Integer) TokenSubType.soValidPunctuation.get(String.valueOf((char) this.oStreamTokenizer.ttype))).intValue();
                        oCurrentToken = createToken(String.valueOf((char) this.oStreamTokenizer.ttype), oTokenSubType);
                    }

                    // Operators
                    else if (TokenSubType.soOperators.containsKey(String.valueOf((char) this.oStreamTokenizer.ttype))) {
                        if (this.bNumberMode == true) {
                            this.bOperatorMode = false;
                            this.oStreamTokenizer.pushBack();
                            oTokenSubType.iType = TokenType.BACKTRACK;
                            oTokenSubType.iSubtype = TokenType.UNKNOWN;
                            oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                        }

                        // Check for double-character operators
                        // such as '<>', '<=', and '=>' so that
                        // we don't get them as separate tokens

                        String strBackup = new String((char) this.oStreamTokenizer.ttype + "");

                        // Operator mode is only needed for
                        // the abmiguouus operators, no need
                        // for the rest

                        if
                                (
                                (bOperatorMode == false)
                                        &&
                                        (
                                                (strBackup.compareTo("<") == 0)
                                                        ||
                                                        (strBackup.compareTo(">") == 0)
                                                        ||
                                                        (strBackup.compareTo("=") == 0)
                                        )
                                ) {
                            this.bOperatorMode = true;
                            Token oSecondPartToken = getNextToken();
                            this.bNumberMode = false;

                            // It it's an operator
                            if (oSecondPartToken.getTokenType().iType == TokenType.OPERATOR) {
                                // Common ambiguities:
                                // ===================
                                // | <  | >  | =  |[+]
                                // | <> | >= | == |[+]
                                // | <= |    |    |[+]
                                // ===================

                                // <>
                                if
                                        (
                                        (strBackup.compareTo("<") == 0)
                                                &&
                                                (oSecondPartToken.getTokenType().iSubtype == TokenSubType.OP_GT)
                                        ) {
                                    System.out.println("[" + strBackup + oSecondPartToken.getLexeme() + "]");
                                    oTokenSubType.iType = TokenType.OPERATOR;
                                    oTokenSubType.iSubtype = TokenSubType.OP_NE;
                                    this.oStreamTokenizer.sval = strBackup + oSecondPartToken.getLexeme();
                                    this.bOperatorMode = false;
                                    oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                                }

                                // <=
                                else if
                                        (
                                        (strBackup.compareTo("<") == 0)
                                                &&
                                                (oSecondPartToken.getTokenType().iSubtype == TokenSubType.OP_ASIGN)
                                        ) {
                                    System.out.println("[" + strBackup + oSecondPartToken.getLexeme() + "]");
                                    oTokenSubType.iType = TokenType.OPERATOR;
                                    oTokenSubType.iSubtype = TokenSubType.OP_LE;
                                    this.oStreamTokenizer.sval = strBackup + oSecondPartToken.getLexeme();
                                    this.bOperatorMode = false;
                                    oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                                }

                                // >=
                                else if
                                        (
                                        (strBackup.compareTo(">") == 0)
                                                &&
                                                (oSecondPartToken.getTokenType().iSubtype == TokenSubType.OP_ASIGN)
                                        ) {
                                    System.out.println("[" + strBackup + oSecondPartToken.getLexeme() + "]");
                                    oTokenSubType.iType = TokenType.OPERATOR;
                                    oTokenSubType.iSubtype = TokenSubType.OP_GE;
                                    this.oStreamTokenizer.sval = strBackup + oSecondPartToken.getLexeme();
                                    this.bOperatorMode = false;
                                    oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                                }

                                // ==
                                else if
                                        (
                                        (strBackup.compareTo("=") == 0)
                                                &&
                                                (oSecondPartToken.getTokenType().iSubtype == TokenSubType.OP_ASIGN)
                                        ) {
                                    System.out.println("[" + strBackup + oSecondPartToken.getLexeme() + "]");
                                    oTokenSubType.iType = TokenType.OPERATOR;
                                    oTokenSubType.iSubtype = TokenSubType.OP_EQ;
                                    this.oStreamTokenizer.sval = strBackup + oSecondPartToken.getLexeme();
                                    this.bOperatorMode = false;
                                    //loToken = createToken(String.valueOf((char)oStreamTokenizer.ttype), loTokenSubType);
                                    oCurrentToken = createToken(this.oStreamTokenizer.sval, oTokenSubType);
                                }

                                // Operator, but other than '>', '='
                                else {
                                    this.bOperatorMode = false;

                                    // Push back that operator
                                    this.oStreamTokenizer.pushBack();

                                    oTokenSubType.iType = TokenType.OPERATOR;
                                    oTokenSubType.iSubtype = ((Integer) TokenSubType.soOperators.get(strBackup)).intValue();
                                    oCurrentToken = createToken(strBackup, oTokenSubType);
                                }
                            } else {
                                // Push back if our guess was wrong...
                                this.bOperatorMode = false;
                                this.oStreamTokenizer.pushBack();

                                oTokenSubType.iType = TokenType.OPERATOR;
                                oTokenSubType.iSubtype = ((Integer) TokenSubType.soOperators.get(strBackup)).intValue();
                                oCurrentToken = createToken(strBackup, oTokenSubType);
                            }
                        }

                        // Other than ambiguous operator
                        else {
                            // Push back if our guess was wrong...
                            if (this.bOperatorMode == true) {
                                this.bOperatorMode = false;
                            }

                            oTokenSubType.iType = TokenType.OPERATOR;
                            oTokenSubType.iSubtype = ((Integer) TokenSubType.soOperators.get(String.valueOf((char) this.oStreamTokenizer.ttype))).intValue();

                            oCurrentToken = createToken(String.valueOf((char) this.oStreamTokenizer.ttype), oTokenSubType);
                        }
                    }

                    // Bracketing
                    else if (TokenSubType.soBrackets.containsKey(String.valueOf((char) this.oStreamTokenizer.ttype))) {
                        oTokenSubType.iType = TokenType.BRACKET;
                        oTokenSubType.iSubtype = ((Integer) TokenSubType.soBrackets.get(String.valueOf((char) this.oStreamTokenizer.ttype))).intValue();
                        oCurrentToken = createToken(String.valueOf((char) this.oStreamTokenizer.ttype), oTokenSubType);
                    }

                    // Invalid Character
                    else {
                        oTokenSubType.iType = TokenType.ERROR;
                        oTokenSubType.iSubtype = TokenType.UNKNOWN;
                        oCurrentToken = createToken(String.valueOf((char) this.oStreamTokenizer.ttype), oTokenSubType);

                        this.bErrorsPresent = true;
                        this.oLexicalErrors.addElement(new LexicalError(LexicalError.ERR_INVALID_CHAR, oCurrentToken));
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("LexicalAnalyzer::getNextToken(): I/O Exception - " + e.getMessage());
            e.printStackTrace(System.err);
            oCurrentToken = null;
        }

        // Add token to a token list for future reference
        if (oCurrentToken != null) {
            // If the token is user-defined ID
            // insert it into the SymbolTables
            if (oCurrentToken.getTokenType().iType == TokenType.ID) {
                this.oSymTab.addSymbol(oCurrentToken);
            }

            if
                    (
                    oCurrentToken.getTokenType().iType != TokenType.BACKTRACK
                            &&
                            oCurrentToken.getTokenType().iType != TokenType.EOF
                            &&
                            oCurrentToken.getTokenType().iType != TokenType.ERROR
                    ) {
                this.oTokenList.addElement(oCurrentToken);
            }
        }

        return oCurrentToken;
    }

    /**
     * Validates that an incoming string is a valid number or not.
     *
     * @param pstrNum a string constating valid numerical chars
     * @return int, LexicalError Error code
     */
    protected int validNum(String pstrNum) {
        if (this.bNumberMode == false) {
            // Do not allow leading zeros
            if (pstrNum.charAt(0) == '0' && pstrNum.length() > 1) {
                // Error
                return LexicalError.ERR_LEADING_ZEROS;
            } else if (pstrNum.charAt(0) == '0' && pstrNum.length() == 1) {
                return LexicalError.OK;
            }

            // An ID starting with the letter?
            if
                    (
                    (pstrNum.charAt(0) >= 'a' && pstrNum.charAt(0) <= 'z')
                            ||
                            (pstrNum.charAt(0) >= 'A' && pstrNum.charAt(0) <= 'Z')
                    ) {
                // Not applicable
                return NA;
            }

            // Try to validate the remaining part
            // (so that there are no non-numerical
            // symbols in the middle)
            try {
                new Integer(pstrNum);
            } catch (NumberFormatException e) {
                return LexicalError.ERR_INVALID_NUMBER_FORMAT;
            }

            return LexicalError.OK;
        } else {
            // An ID starting with the letter? Then
            // we are at the wrong place, and here we should
            // signal an error
            if
                    (
                    (pstrNum.charAt(0) >= 'a' && pstrNum.charAt(0) <= 'z')
                            ||
                            (pstrNum.charAt(0) >= 'A' && pstrNum.charAt(0) <= 'Z')
                    ) {
                return LexicalError.ERR_INVALID_NUMBER_FORMAT;
            }

            // Do not allow trailing zeros
            if (pstrNum.charAt(pstrNum.length() - 1) == '0' && pstrNum.length() > 1) {
                return LexicalError.ERR_TRAILING_ZEROS;
            }

            if (pstrNum.charAt(0) == '0') {
                // If fractional part start from
                // zero then we break string into
                // individual characters and make
                // sure that all of them are digits

                char[] acDigits = pstrNum.toCharArray();

                for (int i = 0; i < acDigits.length; i++) {
                    if (acDigits[i] < '0' || acDigits[i] > '9') {
                        return LexicalError.ERR_INVALID_NUMBER_FORMAT;
                    }
                }
            } else {
                // Ensure, there is no anything but digits in
                // the fractional part, if it doesn't start
                // from zero
                try {
                    new Integer(pstrNum);
                } catch (NumberFormatException e) {
                    return LexicalError.ERR_INVALID_NUMBER_FORMAT;
                }
            }

            return LexicalError.OK;
        }
    }

    /**
     * Returns the list of lexical errors.
     *
     * @return Vector LexicalErrors
     */
    public Vector getLexicalErrors() {
        return this.oLexicalErrors;
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
