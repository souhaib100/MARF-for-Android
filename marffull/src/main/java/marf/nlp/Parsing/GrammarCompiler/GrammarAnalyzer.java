/*
 * GrammarAnalyzer Class
 * (C) 2001 - 2006 Serguei Mokhov, <mailto:mokhov@cs.concordia.ca>
 */

package marf.nlp.Parsing.GrammarCompiler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Enumeration;
import java.util.Vector;

import marf.nlp.Parsing.GenericLexicalAnalyzer;
import marf.nlp.Parsing.LexicalError;
import marf.nlp.Parsing.SymbolTable;
import marf.nlp.Parsing.Token;
import marf.nlp.Parsing.TokenSubType;
import marf.nlp.Parsing.TokenType;
import marf.util.Debug;
import marf.util.NotImplementedException;


/**
 * <p>GrammarAnalyzer class encapsulates functionality
 * required for lexical analysis of a the grammar definition file.</p>
 * <p>
 * $Id: GrammarAnalyzer.java,v 1.21 2006/01/19 04:13:17 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.21 $
 * @since 0.3.0.2
 */
public class GrammarAnalyzer
        extends GenericLexicalAnalyzer {
    /**
     * Default filename for a grammar file.
     */
    public static final String DEFAULT_GRAMMAR_FILE = "cmp.grammar.txt";

    /**
     * Default filename to log lexical errors.
     *
     * @since 0.3.0.5
     */
    public static final String DEFAULT_ERROR_LOG_FILE = "gram.lex.error.log";

    /**
     * Default filename to log scan output.
     *
     * @since 0.3.0.5
     */
    public static final String DEFAULT_SCAN_LOG_FILE = "gram.lex.scan.log";

    /**
     * Grammar token type.
     */
    protected GrammarTokenType oTokenType = null;

    /**
     * Constructs a grammar analyzer with the default filename.
     *
     * @see #DEFAULT_GRAMMAR_FILE
     */
    public GrammarAnalyzer() {
        this(DEFAULT_GRAMMAR_FILE);
    }

    /**
     * Constructs a grammar analyzer with the specified grammar filename.
     *
     * @param pstrGrammarFileName the filename of the grammar file to read from
     */
    public GrammarAnalyzer(String pstrGrammarFileName) {
        super(null);
        this.strSourceFilename = pstrGrammarFileName;
        Debug.debug("GrammarAnalyzer::GrammarAnalyzer(" + pstrGrammarFileName + ")");
    }

    /**
     * Constructs a grammar analyzer using a predefined symbol table.
     * Uses the default filename for grammar.
     *
     * @param poSymTab the symbol table reference
     * @see #DEFAULT_GRAMMAR_FILE
     */
    public GrammarAnalyzer(SymbolTable poSymTab) {
        super(poSymTab);
        this.strSourceFilename = DEFAULT_GRAMMAR_FILE;
    }

    /**
     * Customizes this analyzer for grammar files after calling
     * parent's <code>init()</code>. Allows C++, C, and script (#)
     * style comments; case-sensitive; ignores EOL; and treats
     * some puntuation a special. Sets default lex out filenames
     * for standard and error lexical output.
     *
     * @see marf.nlp.Parsing.GenericLexicalAnalyzer#init()
     */
    public boolean init() {
        if (super.init() == true) {
            // Make all characters ordinary
            this.oStreamTokenizer.resetSyntax();

            // EOL is not a token
            this.oStreamTokenizer.eolIsSignificant(false);

            // Allow C-type coments '/**/'
            this.oStreamTokenizer.slashStarComments(true);

            // Allow C++ '//' comments
            this.oStreamTokenizer.slashSlashComments(true);

            // Allow default script comment: '#'
            this.oStreamTokenizer.commentChar('#');

            // Strip whitespaces
            this.oStreamTokenizer.whitespaceChars('\u0000', '\u0020');

            // Case-sensitive
            this.oStreamTokenizer.lowerCaseMode(false);

            // Range of chars treated as token
            // chars
            this.oStreamTokenizer.wordChars('0', '\u00A0');
            this.oStreamTokenizer.wordChars('%', '%');
            this.oStreamTokenizer.wordChars('$', '$');

            // Valid ordinary characters
//			this.oStreamTokenizer.ordinaryChar('>');
//			this.oStreamTokenizer.ordinaryChar('<');
//			this.oStreamTokenizer.ordinaryChar('=');
//			this.oStreamTokenizer.ordinaryChar(':');
            this.oStreamTokenizer.ordinaryChar('&');
            this.oStreamTokenizer.ordinaryChar('|');
            this.oStreamTokenizer.ordinaryChar('*');

            this.oStreamTokenizer.ordinaryChar('.');
            this.oStreamTokenizer.ordinaryChar(';');
            this.oStreamTokenizer.ordinaryChar('{');
            this.oStreamTokenizer.ordinaryChar('}');
            this.oStreamTokenizer.ordinaryChar('[');
            this.oStreamTokenizer.ordinaryChar(']');

            // Invalid ordinary characters
//			this.oStreamTokenizer.ordinaryChar('@');

            this.strErrorLogFilename = DEFAULT_ERROR_LOG_FILE;
            this.strOutputFilename = DEFAULT_SCAN_LOG_FILE;

            return true;
        } else {
            System.err.println("MARF Grammar Lexer init failed...");
            return false;
        }
    }

    /**
     * Load/Save the contents of lists
     * such as Token list and Error list.
     * 0 means LOAD, 1 means SAVE.
     * Load currently is not implemented.
     *
     * @see marf.nlp.Parsing.GenericLexicalAnalyzer#serialize(int)
     */
    public boolean serialize(int piOperation) {
        boolean bSuccess = true;

        if (piOperation == 0) {
            // TODO Load
            System.err.println("GrammarAnalyzer::serialize(LOAD) - unimplemented");
            return false;
        } else {
            // Write down grammar scan results
            try {
                FileWriter oFileWriterScan = new FileWriter(this.strOutputFilename);

                oFileWriterScan.write
                        (
                                "-------------------------------------\n" +
                                        "MARF Grammar Lexical Analysis Results\n" +
                                        "Source file : \"" + this.strSourceFilename + "\"\n" +
                                        "Total tokens: " + this.oTokenList.size() + "\n" +
                                        "Total errors: " + this.oLexicalErrors.size() + "\n" +
                                        "-------------------------------------\n\n"
                        );

                Enumeration oTokenEnum = oTokenList.elements();

                while (oTokenEnum.hasMoreElements()) {
                    Token oNextToken = (Token) oTokenEnum.nextElement();
                    oNextToken.serialize(piOperation, oFileWriterScan);
                }

                oFileWriterScan.flush();
            } catch (IOException e) {
                System.err.println("GrammarAnalyzer::serialize() - " + e.getMessage());
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
                                            "MARF Grammar Lexical Analysis Errors\n" +
                                            "Source file: \"" + this.strSourceFilename + "\"\n" +
                                            "Lexical errors: " + this.oLexicalErrors.size() + "\n" +
                                            "-----------------------------------\n\n"
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
     * Creates an EOF token as the current token.
     * Its default lexeme is `$'.
     */
    protected void createEOFToken() {
        this.oTokenType.setType(TokenType.EOF);
        this.oTokenType.setSubtype(TokenType.UNKNOWN);
        this.oToken = createToken("$", this.oTokenType);
    }

    /**
     * Creates a general word token for keywords,
     * errors, semantic tokens, etc.
     *
     * @throws NotImplementedException for dictionary words
     */
    protected void createWordToken()
            throws NotImplementedException {
        // Keywords (including grammar ones)
        if (TokenSubType.soKeywords.containsKey(this.oStreamTokenizer.sval)) {
            // Sematic Token
            if (this.oStreamTokenizer.sval.charAt(0) == '@') {
                this.oTokenType.setType(GrammarTokenType.SEMANTIC_TOKEN);
            }

            // Ordinary grammar keyword or a syntax error token
            else {
                this.oTokenType.setType(TokenType.KEYWORD);
            }

            this.oTokenType.setSubtype(((Integer) TokenSubType.soKeywords.get(this.oStreamTokenizer.sval)).intValue());
        }

        // Operators
        else if (TokenSubType.soOperators.containsKey(this.oStreamTokenizer.sval)) {
            this.oTokenType.setType(TokenType.OPERATOR);
            this.oTokenType.setSubtype(((Integer) TokenSubType.soOperators.get(this.oStreamTokenizer.sval)).intValue());
        }

        // Punctuation
        else if (TokenSubType.soValidPunctuation.containsKey(this.oStreamTokenizer.sval)) {
            this.oTokenType.setType(TokenType.PUNCT);
            this.oTokenType.setSubtype(((Integer) TokenSubType.soValidPunctuation.get(this.oStreamTokenizer.sval)).intValue());
        }

        // Brackets (all types - [] () {})
        else if (TokenSubType.soBrackets.containsKey(this.oStreamTokenizer.sval)) {
            this.oTokenType.setType(TokenType.BRACKET);
            this.oTokenType.setSubtype(((Integer) TokenSubType.soBrackets.get(this.oStreamTokenizer.sval)).intValue());
        }

        // Numbers (not literally in this case, but tokens NUM and INTEGER)
        else if (this.oStreamTokenizer.sval.equals("NUM") || this.oStreamTokenizer.sval.equals("INTEGER")) {
            this.oTokenType.setType(TokenType.NUM);

            if (this.oStreamTokenizer.sval.equals("NUM")) {
                this.oTokenType.setSubtype(TokenType.UNKNOWN);
            } else if (this.oStreamTokenizer.sval.equals("INTEGER")) {
                this.oTokenType.setSubtype(TokenSubType.INTEGER);
            }
        }

        // IDs
        else {
            this.oTokenType.setType(TokenType.ID);

            // "ID"
            if (this.oStreamTokenizer.sval.equals("ID")) {
                this.oTokenType.setSubtype(GrammarTokenType.UNKNOWN);
            }

            // "<nonTerminal>"
            else if (this.oStreamTokenizer.sval.startsWith("<")) {
                this.oTokenType.setSubtype(GrammarTokenType.GRAMMAR_ID);
            }

            // Dictionary word
            else {
                throw new NotImplementedException("Unimplemented dictionary word handling!!!!!!!!!!");
                //this.oTokenType.subtype = GrammarTokenType.DICT_WORD;
            }
        }

        this.oToken = createToken(this.oStreamTokenizer.sval, this.oTokenType);
    }

    /**
     * Creates an ordinary keyword, punctuation, operator, bracket,
     * or lexical error tokens.
     */
    protected void createOrdinaryToken() {
        String strToken = String.valueOf((char) this.oStreamTokenizer.ttype);

        // Keywords
        if (TokenSubType.soKeywords.containsKey(strToken)) {
            this.oTokenType.setType(TokenType.KEYWORD);
            this.oTokenType.setSubtype(((Integer) TokenSubType.soKeywords.get(strToken)).intValue());
        }

        // Punctuation
        else if (TokenSubType.soValidPunctuation.containsKey(strToken)) {
            this.oTokenType.setType(TokenType.PUNCT);
            this.oTokenType.setSubtype(((Integer) TokenSubType.soValidPunctuation.get(strToken)).intValue());
        }

        // Operators
        else if (TokenSubType.soOperators.containsKey(strToken)) {
            this.oTokenType.setType(TokenType.OPERATOR);
            this.oTokenType.setSubtype(((Integer) TokenSubType.soOperators.get(strToken)).intValue());
        }

        // Bracketing
        else if (TokenSubType.soBrackets.containsKey(strToken)) {
            this.oTokenType.setType(TokenType.BRACKET);
            this.oTokenType.setSubtype(((Integer) TokenSubType.soBrackets.get(strToken)).intValue());
        }

        // Invalid Character
        else {
            this.oTokenType.setType(TokenType.ERROR);
            this.oTokenType.setSubtype(TokenType.UNKNOWN);

            this.bErrorsPresent = true;
            this.oLexicalErrors.addElement(new LexicalError(LexicalError.ERR_INVALID_CHAR, this.oToken));
        }

        this.oToken = createToken(strToken, this.oTokenType);
    }

    /**
     * Creates next grammar token from the stream of characters.
     *
     * @throws LexicalError if invalid characters encountered or there
     *                      was an I/O problem
     */
    protected void createNextToken()
            throws LexicalError {
        try {
            int iTokenType = oStreamTokenizer.nextToken();
            this.oTokenType = new GrammarTokenType();

            switch (iTokenType) {
                // Signal EOF
                case StreamTokenizer.TT_EOF: {
                    createEOFToken();
                    break;
                }

                case StreamTokenizer.TT_WORD: {
                    Debug.debug("GrammarLexer - [TT_WORD]: " + oStreamTokenizer.sval);
                    createWordToken();
                    break;
                } // TT_WORD

                default: {
                    Debug.debug("GrammarLexer - [default]: " + (char) oStreamTokenizer.ttype);
                    createOrdinaryToken();
                    break;
                } // default

            } // switch

			/*
            Debug.debug
			(
				this.oTokenType.type + "," +
				this.oTokenType.subtype + "," +
				this.oToken
			);
			*/

        } // try
        catch (IOException e) {
            System.err.println("GrammarAnalyzer::getNextToken(): I/O Exception - " + e.getMessage());
            e.printStackTrace(System.err);

            throw new LexicalError(e.getMessage(), e);
        }
    }

    /**
     * Returns next grammar token.
     *
     * @see marf.nlp.Parsing.GenericLexicalAnalyzer#getNextToken()
     */
    public Token getNextToken()
            throws LexicalError {
        createNextToken();
        addToTokenList();

        return this.oToken;
    }

    /**
     * Adds the current token to the token list if it
     * is not null and not of backtrack, EOF, or error type.
     *
     * @see TokenType#BACKTRACK
     * @see TokenType#EOF
     * @see TokenType#ERROR
     */
    protected void addToTokenList() {
        // Add token to a token list for future reference
        if (this.oToken != null) {
            if
                    (
                    this.oToken.getTokenType().getType() != TokenType.BACKTRACK
                            &&
                            this.oToken.getTokenType().getType() != TokenType.EOF
                            &&
                            this.oToken.getTokenType().getType() != TokenType.ERROR
                    ) {
                this.oTokenList.addElement(this.oToken);
            }
        } else {
            Debug.debug("GrammarAnalyzer::addToTokenList() -- WARNING: current token is null.");
        }
    }

    /**
     * Allows querying for the collection of lexical errors.
     *
     * @return the collection of lexical errors
     */
    public Vector getLexicalGrammarErrors() {
        return this.oLexicalErrors;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.21 $";
    }
}

// EOF
