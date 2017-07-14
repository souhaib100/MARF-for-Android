package marf.nlp.Parsing;

import net.windward.android.awt.Point;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StreamTokenizer;
import java.util.Vector;


/**
 * <p>Generic Lexical Analyzer.</p>
 * <p>
 * <p>(C) 2001 Serguei A. Mokhov</p>
 * <p>(C) 2002 - 2006 The MARF Research and Development Group</p>
 * <p>
 * $Id: GenericLexicalAnalyzer.java,v 1.16 2006/01/19 04:13:16 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.16 $
 * @since 0.3.0.2
 */
public abstract class GenericLexicalAnalyzer {
    /**
     * Default filename for the output.
     */
    public static final String DEFAULT_OUTPUT_FILE = "lex.scan.log";

    /**
     * Default filename for the error log.
     */
    public static final String DEFAULT_ERROR_FILE = "lex.error.log";

    /**
     * Internal reference to the file reader of a file
     * to perform lexical analysis of.
     */
    protected FileReader oFileReader = null;

    /**
     * A tokenizer used to split the stream of characters
     * into a stream of tokens.
     */
    protected StreamTokenizer oStreamTokenizer = null;

    /**
     * File name of a file which serves as an input
     * of the Lexical Analyzer.
     */
    protected String strSourceFilename = "";

    /**
     * File name of a file which serves as an output
     * of the Lexical Analyzer.
     */
    protected String strOutputFilename = DEFAULT_OUTPUT_FILE;

    /**
     * File name of a file which serves as an
     * lexical errors log.
     *
     * @since September 2001
     */
    protected String strErrorLogFilename = DEFAULT_ERROR_FILE;

    /**
     * An indicator of presence of lexical errors.
     *
     * @since October 2, 2001
     */
    protected boolean bErrorsPresent = false;

    /**
     * A list of tokens extracted so far.
     */
    protected Vector oTokenList = null;

    /**
     * A reference to local symbol table.
     */
    protected SymbolTable oSymTab = null;

    /**
     * A collection of lexical errors (if any).
     */
    protected Vector oLexicalErrors = null;

    /**
     * Current token being processed.
     */
    protected Token oToken = null;

    /**
     * Constructor with symbol table.
     *
     * @param poSymTab symbol table to use.
     */
    public GenericLexicalAnalyzer(SymbolTable poSymTab) {
        this.oSymTab = poSymTab;
        this.oTokenList = new Vector();
        this.oLexicalErrors = new Vector();
    }

    /**
     * Default initialization routine.
     * Should be overridden by derivatives
     * because it is language-specific, and default
     * initialization will not always suffice.
     *
     * @return <code>true</code> of initialization is successful
     */
    public boolean init() {
        try {
            this.oFileReader = new FileReader(strSourceFilename);
            this.oStreamTokenizer = new StreamTokenizer(oFileReader);

            return true;
        } catch (FileNotFoundException e) {
            System.err.println("Lexer Error: source file not found - " + e.getMessage());
            e.printStackTrace(System.err);
            return false;
        }
    }

    /**
     * Scan for tokens through the input stream.
     *
     * @throws LexicalError as a notification there were one more more lexical errors;
     *                      the actual error messages can be queried via <code>getLexicalErrors()</code>.
     * @see #getLexicalErrors()
     */
    public void scan()
            throws LexicalError {
        // Very first token
        try {
            this.oToken = getNextToken();
        } catch (LexicalError e) {
            this.oLexicalErrors.addElement(e);
        }

        // All subsequent tokens
        while (this.oToken.getTokenType().iType != TokenType.EOF) {
            try {
                if (this.oToken.getTokenType().iType != TokenType.BACKTRACK) {
                    this.oTokenList.addElement(this.oToken);
                }

                this.oToken = getNextToken();
            } catch (LexicalError e) {
                this.oLexicalErrors.addElement(e);
            }
        }

        System.out.println("Tokens found: " + this.oTokenList.size());
        System.out.println("Errors found: " + this.oLexicalErrors.size());

        if (this.oLexicalErrors.size() > 0) {
            this.bErrorsPresent = true;

            throw new LexicalError
                    (
                            "There were errors (" + this.oLexicalErrors.size() + ") while scanning."
                                    + "Please query for them for details through getLexicalErrors()."
                    );
        }
    }

    /**
     * Load/Save the contents of lists
     * such as Token list and Error list.
     * Must be overridden by the derivatives.
     *
     * @param piOperation 0 means load, 1 means save
     * @return <code>true</code> if the serialization was successful
     */
    public abstract boolean serialize(int piOperation);

    /**
     * Core method of the LexicalAnalyzer.
     * Should know how to return the next token
     * according to language specification.
     * Must be overridden by the derivatives.
     *
     * @return newly recognized lexical token
     * @throws LexicalError in case of invalid character stream (alphabet) entries found
     */
    public abstract Token getNextToken()
            throws LexicalError;

    /**
     * Creates an instance of a <code>Token</code> data structure
     * provided its type and lexeme, and location is calculated
     * dynamically by the <code>StreamTokenizer</code>.
     * <p>
     * TODO: reliably get a character position within a line.
     *
     * @param pstrLexeme     token's spelling
     * @param poTokenSubType token's data type
     * @return the Token data structure instance; null of either of parameters is empty
     */
    public Token createToken(String pstrLexeme, TokenSubType poTokenSubType) {
        Token oNewToken = null;

        if (poTokenSubType != null && pstrLexeme.equals("") == false) {
            // NOTE: I hardcode 0 for position in the line for
            // now because I haven't figured out how to obtain it yet,
            // but line number is guaranteed.
            oNewToken = new Token(pstrLexeme, new Point(0, this.oStreamTokenizer.lineno()), poTokenSubType);
        }

        return oNewToken;
    }

    /**
     * Access method for the SourceFilename property.
     *
     * @return the current value of the SourceFilename property
     */
    public String getSourceFilename() {
        return this.strSourceFilename;
    }

    /**
     * Sets the value of the SourceFilename property.
     *
     * @param pstrSourceFilename the new value of the SourceFilename property
     */
    public void setSourceFilename(String pstrSourceFilename) {
        this.strSourceFilename = pstrSourceFilename;
        setOutputFilename(pstrSourceFilename + "." + strOutputFilename);
        setErrorLogFilename(pstrSourceFilename + "." + strErrorLogFilename);
    }

    /**
     * Access method for the OutputFilename property.
     *
     * @return the current value of the OutputFilename property
     */
    public String getOutputFilename() {
        return this.strOutputFilename;
    }

    /**
     * Sets the value of the OutputFilename property.
     *
     * @param pstrOutputFilename the new value of the OutputFilename property
     */
    public void setOutputFilename(String pstrOutputFilename) {
        this.strOutputFilename = pstrOutputFilename;
    }

    /**
     * Access method for the ErrorLogFilename property.
     *
     * @return the current value of the ErrorLogFilename property
     */
    public String getErrorLogFilename() {
        return this.strErrorLogFilename;
    }

    /**
     * Sets the value of the ErrorLogFilename property.
     *
     * @param pstrErrorLogFilename the new value of the ErrorLogFilename property
     */
    public void setErrorLogFilename(String pstrErrorLogFilename) {
        this.strErrorLogFilename = pstrErrorLogFilename;
    }

    /**
     * Determines if the ErrorsPresent property is true.
     *
     * @return <code>true</code> if the ErrorsPresent property is true
     */
    public boolean getErrorsPresent() {
        return this.bErrorsPresent;
    }

    /**
     * Access method for the TonkenList property.
     *
     * @return the current value of the TonkenList property
     */
    public Vector getTokenList() {
        return this.oTokenList;
    }

    /**
     * Access method for the SymTab property.
     *
     * @return the current value of the SymTab property
     */
    public SymbolTable getSymTab() {
        return this.oSymTab;
    }

    /**
     * Allows querying for actual lexical errors happened
     * during scanning.
     *
     * @return a collection of caught lexical errors
     * @since 0.3.0.5
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
        return "$Revision: 1.16 $";
    }
}

// EOF
