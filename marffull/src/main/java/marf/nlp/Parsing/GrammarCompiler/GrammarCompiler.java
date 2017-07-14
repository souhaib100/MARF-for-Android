package marf.nlp.Parsing.GrammarCompiler;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import marf.Storage.StorageException;
import marf.Storage.StorageManager;
import marf.nlp.Parsing.CompilerError;
import marf.nlp.Parsing.LexicalError;
import marf.nlp.Parsing.SemanticError;
import marf.nlp.Parsing.SyntaxError;
import marf.nlp.Parsing.Token;
import marf.nlp.Parsing.TokenType;
import marf.nlp.Parsing.TransitionTable;
import marf.util.Debug;


/**
 * <p>Grammar compiler -- compiles source grammar file
 * and produces a corresponding transition table for
 * a given language denoted by the grammar.</p>
 * <p>
 * $Id: GrammarCompiler.java,v 1.29 2006/01/19 04:13:17 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.29 $
 * @since 0.3.0.2
 */
public class GrammarCompiler
        extends StorageManager {
    /**
     * Action for adding a next token to the RHS of
     * the curren rule signifing to stop.
     *
     * @since 0.3.0.5
     */
    public static final String TOKEN_ACTION_BREAK = "break";

    /**
     * Action for adding a next token to the RHS of
     * the curren rule signifing to continue and skip
     * to the next token.
     *
     * @since 0.3.0.5
     */
    public static final String TOKEN_ACTION_CONTINUE = "continue";

    /**
     * Action for adding a next token to the RHS of
     * the curren rule signifing to proceed to add the
     * current token to the RHS.
     *
     * @since 0.3.0.5
     */
    public static final String TOKEN_ACTION_PROCEED = "proceed";

    /**
     * Instance of the grammar as a set of
     * production Rules, First and Follow sets.
     */
    protected Grammar oGrammar = null;

    /**
     * Source grammar filename.
     */
    protected String strGrammarFileName = "";

    /**
     * Lexical Analyzer for the grammar.
     */
    protected GrammarAnalyzer oGrammarAnalyzer = null;

    /**
     * Instance of the TransitionTable, generated upon
     * the need from the source grammar file.
     */
    protected static TransitionTable soTransitionTable = null;

    /**
     * Current grammar element.
     */
    protected GrammarElement oGrammarElement;

    /**
     * Current lexical token.
     */
    protected Token oToken;

    /**
     * Current grammar rule.
     */
    protected Rule oRule;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -8821074737532287405L;

    /**
     * Default Constructor.
     * Assumes <code>grammar-original.txt</code> as the filename.
     *
     * @throws CompilerError if initialization failed
     */
    public GrammarCompiler()
            throws CompilerError {
        this("grammar-orignal.txt");
        Debug.debug("GrammarCompiler::GrammarCompiler()");
    }

    /**
     * Constructor with the grammar filename.
     *
     * @param pstrGrammarFileName the filename of the grammar to compile
     * @throws CompilerError if initialization failed
     */
    public GrammarCompiler(String pstrGrammarFileName)
            throws CompilerError {
        this.strGrammarFileName = pstrGrammarFileName;

        System.out.println("Instantiating Grammar.");
        this.oGrammar = new Grammar();

        createGrammarAnalyzer();

        System.out.println("Initializing Grammar Analyzer.");

        if (this.oGrammarAnalyzer.init() == false) {
            throw new CompilerError("Failed to initialize grammar analyzer.");
        }
    }

    /**
     * Instantiates grammar analyzer.
     */
    protected void createGrammarAnalyzer() {
        System.out.println("Instantiating Grammar Analyzer.");
        this.oGrammarAnalyzer = new GrammarAnalyzer(this.strGrammarFileName);
    }

    /**
     * Compiles grammar.
     * Compilation consitis of parsing source grammar file,
     * creating rules, Terminals, Non-Terminals; then computes
     * first and follow sets, and fills in a TransitionTable data structure.
     *
     * @throws CompilerError in case there was a lexical or syntax error
     */
    public void compileGrammar()
            throws CompilerError {
        parseGrammar();

        this.oGrammar.computeFirstSets();
        this.oGrammar.computeFollowSets();

        soTransitionTable = new TransitionTable();

        soTransitionTable.init
                (
                        this.oGrammar.getNonTerminalList().size(),
                        this.oGrammar.getTerminalList().size()
                );

        fillInTransitionTable();
        soTransitionTable.save();
    }

    /**
     * Creates an instance of an empty (epsilon) token.
     * By default this token is denoted by an ampersand &amp;.
     */
    protected void createEpsilonToken() {
        // Create the epsilon token;
        Terminal oEpsilon = new Terminal("&", 0);
        this.oGrammarElement = oEpsilon;
        this.oGrammar.addTeminal(oEpsilon);

        // Set the reference to epslion
        this.oGrammar.setEpsilonTerminal(oEpsilon);
    }

    /**
     * Creates the next non-terminal of a rule from the upcoming
     * token.
     *
     * @return <code>true</code> if the token was creater; and <code>false</code>
     * if the end of file was reached
     * @throws CompilerError -- either a <code>SyntaxError</code> or <code>LexicalError</code>
     * @see SyntaxError
     * @see LexicalError
     */
    protected boolean createNextNonTerminal()
            throws CompilerError {
        this.oToken = this.oGrammarAnalyzer.getNextToken();

        if (this.oToken.getTokenType().getType() == TokenType.EOF) {
            System.out.println("GrammarCompiler::createNextNonTerminal() - Done successfully.");
            return false;
        }

        if (this.oToken.getTokenType().getSubtype() != GrammarTokenType.GRAMMAR_ID) {
            throw new SyntaxError
                    (
                            "GrammarCompiler::createNextNonTerminal() - ERROR: The first token must be a non-terminal!\n" +
                                    "Got: [" + this.oToken.getLexeme() + "," + this.oToken.getTokenType().getSubtype() + "]"
                    );
        }

        this.oGrammarElement = getGrammarElement(this.oToken.getLexeme());

        if (this.oGrammarElement == null) {
            // If the non-terminal does not exist, create it.
            this.oGrammarElement = new NonTerminal(this.oToken, this.oGrammar.getNonTerminalList().size());
            this.oGrammar.addNonTeminal((NonTerminal) this.oGrammarElement);

            // Keep a reference to the starting symbol
            // if it didn't exist, assuming the very first non-terminal
            // encountered.
            if (this.oGrammar.getStartNonTerminal() == null) {
                this.oGrammar.setStartNonTerminal((NonTerminal) this.oGrammarElement);
            }
        } else {
            /*
             * TODO: Some more error checking
			 * Not crucial....
			 */
        }

        // All non-terminals on the LHS a automatically
        // defined (needed to check if all of them were defined
        // afterwards before proceeding to the first and follow sets)
        ((NonTerminal) this.oGrammarElement).setDefined();

        //Debug.debug("Defined: " + this.oGrammarElement.getName());

        return true;
    }

    /**
     * Creates an embryo of a rule given the LHS non-terminal and the rule
     * operator `::='.
     *
     * @throws CompilerError -- either a <code>SyntaxError</code> or <code>LexicalError</code>
     * @see SyntaxError
     * @see LexicalError
     */
    protected void createRule()
            throws CompilerError {
        // Create a new grammar rule with the first
        // non-terminal as its LHS, and empty RHS.
        this.oRule = new Rule((NonTerminal) this.oGrammarElement);

        // Pick up next grammar token from the token stream
        this.oToken = this.oGrammarAnalyzer.getNextToken();

        // Must be ::=
        if (this.oToken.getTokenType().getSubtype() != GrammarTokenType.RULE_OP) {
            throw new SyntaxError
                    (
                            "GrammarCompiler::createRule() - ERROR: Expected rule operator (::=), but got: " +
                                    this.oToken.getLexeme()
                    );
        }
    }

    /**
     * Outputs statistics by serializing the grammar
     * analyzer to a file as well as errors if any to the
     * the error log file.
     *
     * @see GrammarAnalyzer#serialize(int)
     * @see GrammarAnalyzer#getLexicalGrammarErrors()
     * @see marf.nlp.Parsing.GenericLexicalAnalyzer#getErrorsPresent()
     * @see marf.nlp.Parsing.GenericLexicalAnalyzer#getErrorLogFilename()
     */
    protected void outputStats() {
        // Output stats
        this.oGrammarAnalyzer.serialize(1);

        System.out.println
                (
                        "The lexical analysis output file is \"" +
                                this.oGrammarAnalyzer.getOutputFilename() +
                                "\"."
                );

        if (this.oGrammarAnalyzer.getErrorsPresent() == true) {
            System.err.println
                    (
                            "There were " + this.oGrammarAnalyzer.getLexicalGrammarErrors().size() +
                                    " lexical errors while scanning the source grammar file."
                    );

            System.err.println("The error log file is \"" + this.oGrammarAnalyzer.getErrorLogFilename() + "\".");
        }
    }

    /**
     * Acquires the next RHS token for a rule from the token stream.
     *
     * @return string literal indicating to "break", "continue", or "proceed" when
     * searching for next RHS tokens. "break" happens when the method encounters
     * %EOL or EOF tokens; "continue" when semantic token is read, and "proceed"
     * otherwise.
     * @throws CompilerError -- either a <code>SyntaxError</code> or <code>LexicalError</code>
     * @see SyntaxError
     * @see LexicalError
     */
    protected String getNextRHSToken()
            throws CompilerError {
        // Supposedly a RHS element or an %EOL
        this.oToken = this.oGrammarAnalyzer.getNextToken();

        //Debug.debug("Token:: " + this.oToken.getLexeme()+ ", type: " + this.oToken.getTokenType().subtype);

        // %EOL Token signals end of a grammar line
        // (in case it was spanned across several lines)
        if (((GrammarTokenType) this.oToken.getTokenType()).getSubtype() == GrammarTokenType.GRAMMAR_EOL) {
            System.out.println("Proceeding to next grammar sentence...");
            return TOKEN_ACTION_BREAK;
        }

        // EOF also can stop this thing...
        if (this.oToken.getTokenType().getType() == TokenType.EOF) {
            System.out.println("End of file... most likely last grammar sentence has been read.");
            return TOKEN_ACTION_BREAK;
        }

        // Semantic tokens...
        if (this.oToken.getTokenType().getType() == GrammarTokenType.SEMANTIC_TOKEN) {
            this.oRule.addRHSElement(new SemanticToken(this.oToken));
            return TOKEN_ACTION_CONTINUE;
        }

        // The last but not the least...
        // ERRORs are evil!
        if (this.oToken.getTokenType().getType() == TokenType.ERROR) {
            throw new SyntaxError
                    (
                            "An error has occurred while parsing the grammar.\n"
                                    + "Please fix the grammar and restart Grammar Compiler.\n"
                                    + "Faulting token: [" + this.oToken.getLexeme() + "], line: " + this.oToken.getPosition().y
                    );
        }

        return TOKEN_ACTION_PROCEED;
    }

    /**
     * Adds next element to the RHS of the current rule.
     *
     * @throws SyntaxError if unrecognized token type found
     */
    protected void addNextRHSElement()
            throws SyntaxError {
        /*
		 * TODO: |-distinction
		 */

        switch (this.oToken.getTokenType().getType()) {
            case GrammarTokenType.ID: {
                if (addIDToken() == true) {
                    break;
                }
                // else let it slide down...
            }

            // Keywords of the MARF as well as Grammar
            // languages (i.e., terminals)
            case GrammarTokenType.KEYWORD:
            case GrammarTokenType.PUNCT:
            case GrammarTokenType.OPERATOR:
            case GrammarTokenType.BRACKET:
            case GrammarTokenType.NUM: {
                addTerminalToken();
                break;
            }

            // We got busted right here...
            default: {
                getBusted();
            }

        } // switch
    }

    /**
     * Adds a non-terminal grammar ID token type to the RHS of the
     * current rule. If the token already present in the current
     * grammar, we fetch its reference from there, else we create
     * a new entry.
     *
     * @return <code>true</code> if the current token type is really
     * of type <code>GrammarTokenType.GRAMMAR_ID</code>
     * @see GrammarTokenType#GRAMMAR_ID
     */
    protected boolean addIDToken() {
        // Non-terminals...
        if (((GrammarTokenType) this.oToken.getTokenType()).getSubtype() == GrammarTokenType.GRAMMAR_ID) {
            NonTerminal oNonTerminal = null;

            // Check if this non-terminal is already in the
            // list of non-terminals

            int iNonTerminalIndex = this.oGrammar.contains(this.oToken.getLexeme());

            if (iNonTerminalIndex < 0) {
                // No, so create it and put it in
                oNonTerminal = new NonTerminal(this.oToken, this.oGrammar.getNonTerminalList().size());
                this.oGrammar.addNonTeminal(oNonTerminal);
            } else {
                // Get it from the list otherwise
                oNonTerminal = (NonTerminal) this.oGrammar.getNonTerminalList().elementAt(iNonTerminalIndex);
            }

            // Add the guy to rule's RHS
            this.oRule.addRHSElement(oNonTerminal);

            // finally...
            return true;
        }

        return false;
    }

    /**
     * Adds a terminal token type to the RHS of the
     * current rule. If the token already present in the current
     * grammar, we fetch its reference from there, else we create
     * a new entry.
     */
    protected void addTerminalToken() {
        Terminal oTerminal = null;

        // Check if this terminal is already in the
        // list of terminals

        int iTerminalIndex = this.oGrammar.contains(this.oToken.getLexeme());

        if (iTerminalIndex < 0) {
            // No, so create it and put it in
            oTerminal = new Terminal(this.oToken, this.oGrammar.getTerminalList().size());
            this.oGrammar.addTeminal(oTerminal);
        } else {
            // Get it from the list otherwise
            oTerminal = (Terminal) this.oGrammar.getTerminalList().elementAt(iTerminalIndex);
        }

        // Add the guy to rule's RHS
        this.oRule.addRHSElement(oTerminal);
    }

    /**
     * Dies on unexpected grammar token type.
     *
     * @throws SyntaxError indicated unepected token type.
     */
    protected void getBusted()
            throws SyntaxError {
        throw new SyntaxError
                (
                        "Unexpected grammar token type.\n"
                                + "Faulting token: [" + this.oToken.getLexeme() + "], line: "
                                + this.oToken.getPosition().y + ", type: "
                                + GrammarTokenType.soTokenSubTypes.get(new Integer(((GrammarTokenType) this.oToken.getTokenType()).getSubtype()))
                );
    }

    /**
     * Creates and end-of-file indicator terminal.
     * By default it is denoted by the `$' sign.
     */
    protected void createEOFTerminal() {
        // EOF Terminal
        this.oGrammar.setEOFTerminal(new Terminal("$", this.oGrammar.getTerminalList().size()));
        this.oGrammar.addTeminal(this.oGrammar.getEOFTerminal());
    }

    /**
     * Checks for undefined non-terminals in the grammar.
     *
     * @throws SemanticError if there were undefied non-terminals
     */
    protected void checkUndefinedNonTerminals()
            throws SemanticError {
        // Check for undefined non-terminals

        System.out.println("Grammar Compiler: checking for undefined non-terminals...");

        boolean bUndefNonTerminals = false;
        StringBuffer oUndefinedNonTerminals = new StringBuffer();

        for (int i = 0; i < this.oGrammar.getNonTerminalList().size(); i++) {
            NonTerminal oNonTerminal = (NonTerminal) this.oGrammar.getNonTerminalList().elementAt(i);

            if (oNonTerminal.isDefined()) {
                continue;
            } else {
                bUndefNonTerminals = true;

                oUndefinedNonTerminals.append
                        (
                                "Undefined non-terminal: " + oNonTerminal.getName() +
                                        ", line: " + oNonTerminal.getToken().getPosition().y + "\n"
                        );
            }
        }

        if (bUndefNonTerminals) {
            oUndefinedNonTerminals.append("There are undefined non-terminals (see above).");
            oUndefinedNonTerminals.append("Please fix before proceeding.");

            throw new SemanticError(oUndefinedNonTerminals.toString());
        } else {
            System.out.println("All non-terminals have been defined.");
        }
    }

    /**
     * Parses grammar and outputs stats at the end.
     * The end result of this method is the grammar
     * data structure with a collection of rules in it.
     *
     * @throws CompilerError in case of lexical, syntax, or semantic errors
     * @see Grammar
     * @see Rule
     */
    protected void parseGrammar()
            throws CompilerError {
        createEpsilonToken();

        while (true) {
            if (createNextNonTerminal() == false) {
                // EOF
                break;
            }

            // The Rule object and its LHS at the very least
            createRule();

			/*
			 * Here we're trying to get all the
			 * production rules (RHS's) for the given LHS
			 */
            while (true) {
                String strNextAction = getNextRHSToken();
                System.out.println("Action to do: " + strNextAction);

                // For semantic tokens
                if (strNextAction.equals(TOKEN_ACTION_CONTINUE)) {
                    continue;
                }

                // Grammar %EOL or EOF
                else if (strNextAction.equals(TOKEN_ACTION_BREAK)) {
                    break;
                }

                // Add next RHS element to the current rule
                addNextRHSElement();
            } // smaller while

            // Let's add yet another Rule to the list
            // of parsed rules...
            this.oRule.setID(this.oGrammar.getRules().size());
            this.oGrammar.addRule(this.oRule);
        } // outer while

        // Everything ends by an EOF
        createEOFTerminal();

        // Final reporting
        checkUndefinedNonTerminals();
        outputStats();
    }

    /**
     * Returns a grammar element object by it's name
     * (lexeme) if it exists; null otherwise. First, the
     * terminals list is checked and then the non-terminals one.
     *
     * @param pstrName the name of the element
     * @return corresponding GrammarElement object or null if not found
     */
    protected GrammarElement getGrammarElement(String pstrName) {
        // Search the terminal list.
        Vector oTerminals = this.oGrammar.getTerminalList();

        for (int i = 0; i < oTerminals.size(); i++) {
            Terminal oTerminal = (Terminal) oTerminals.elementAt(i);

            if (oTerminal.getName().equals(pstrName)) {
                return oTerminal;
            }
        }

        // Search the non-terminal list.
        Vector oNonTerminals = this.oGrammar.getNonTerminalList();

        for (int i = 0; i < oNonTerminals.size(); i++) {
            NonTerminal oNonTerminal = (NonTerminal) oNonTerminals.elementAt(i);

            if (oNonTerminal.getName().equals(pstrName)) {
                return oNonTerminal;
            }
        }

        return null;
    }

    /**
     * Fills in TransitionalTable data structure.
     * Having parsed all Terminals and NonTerminals from the grammar,
     * computed all rules, first and follow sets, we can finally fill
     * this in to be used by the main parser.
     *
     * @throws CompilerError if either list of terminals, or non-terminals
     *                       or rules is empty
     */
    private void fillInTransitionTable()
            throws CompilerError {
        // Check whether we can to cannot do it.
        if
                (
                this.oGrammar.getTerminalList().isEmpty()
                        ||
                        this.oGrammar.getNonTerminalList().isEmpty()
                        ||
                        this.oGrammar.getRules().isEmpty()
                ) {
            throw new CompilerError
                    (
                            "ERROR: Not all grammar elements have been processed."
                    );
        }

        // Set Terminals and NonTerminal lists
        // to TT first as indices.

        soTransitionTable.setTerminals(this.oGrammar.getTerminalList());
        soTransitionTable.setNonTerminals(this.oGrammar.getNonTerminalList());

        // Set IDs of EOF terminal and starting non-terminal
        // for future access in the Parser

        soTransitionTable.setEOFTerminalID(this.oGrammar.getEOFTerminal().getID());
        soTransitionTable.setStartNonTerminalID(this.oGrammar.getStartNonTerminal().getID());

        // Bulding state transition algorithm
        // For every production rule...
        for (int p = 0; p < this.oGrammar.getRules().size(); p++) {
            // ... such that ...
            Rule oProductionRule = (Rule) this.oGrammar.getRules().elementAt(p);

            System.out.println(oProductionRule.toAbbrString() + ": " + oProductionRule.toString());

            // For all terminals...
            Vector oRHSFirstSet = oProductionRule.getRHSFirstSet(this.oGrammar.getEpsilonTerminal());

            for (int t = 0; t < oRHSFirstSet.size(); t++) {
                Terminal oTerminal = (Terminal) oRHSFirstSet.elementAt(t);

                if (oTerminal.getType().getSubtype() == GrammarTokenType.EPSILON) {
                    for (int f = 0; f < oProductionRule.getLHS().getFollowSet().size(); f++) {
                        Terminal oETerminal = (Terminal) oProductionRule.getLHS().getFollowSet().elementAt(f);

                        if (soTransitionTable.getEntryAt(oProductionRule.getLHS(), oETerminal) != null) {
                            Rule oCellRule = (Rule) soTransitionTable.getEntryAt(oProductionRule.getLHS(), oETerminal);

                            if (oProductionRule.equals(oCellRule) == false) {
                                throw new CompilerError
                                        (
                                                "&: GrammarCompiler::fillInTransitionTable() - ERROR: Overwriting cell with a rule in it!\n"
                                                        + "Rule in the table cell: " + oCellRule.toAbbrString() + ": " + oCellRule.toString() + "\n"
                                                        + "Overwriting Rule      : " + oProductionRule.toAbbrString() + ": " + oProductionRule.toString() + "\n"
                                                        + "This means there are ambiguites in the source grammar.\n"
                                                        + "Please remove them and restart grammar compilation."
                                        );
                            }

                            // For efficiency, to avoid adding dupes
                            continue;
                        }

                        Debug.debug
                                (
                                        "&: Adding an entry to TT[" +
                                                oProductionRule.getLHS().getName() + "," +
                                                oETerminal.getName() + "]: " + oProductionRule.toString()
                                );

                        soTransitionTable.setEntryAt
                                (
                                        oProductionRule.getLHS(),
                                        oETerminal,
                                        oProductionRule
                                );
                    }

                    break;
                } else {
                    if (soTransitionTable.getEntryAt(oProductionRule.getLHS(), oTerminal) != null) {
                        Rule oCellRule = (Rule) soTransitionTable.getEntryAt(oProductionRule.getLHS(), oTerminal);

                        if (oProductionRule.equals(oCellRule) == false) {
                            throw new CompilerError
                                    (
                                            "p: GrammarCompiler::fillInTransitionTable() - ERROR: Overwriting cell with a rule in it!\n"
                                                    + "Rule in the table cell: " + oCellRule.toAbbrString() + ": " + oCellRule.toString() + "\n"
                                                    + "Overwriting Rule      : " + oProductionRule.toAbbrString() + ": " + oProductionRule.toString() + "\n"
                                                    + "This means there are ambiguites in the source grammar.\n"
                                                    + "Please remove them and restart grammar compilation."
                                    );
                        }

                        // For efficiency, to avoid adding dupes
                        continue;
                    }

                    Debug.debug
                            (
                                    "p: Adding an entry to TT[" +
                                            oProductionRule.getLHS().getName() + "," +
                                            oTerminal.getName() + "]: " + oProductionRule.toString()
                            );

                    soTransitionTable.setEntryAt
                            (
                                    oProductionRule.getLHS(),
                                    oTerminal,
                                    oProductionRule
                            );
                    //break;
                }
            }
        }

/*
		// For every production rule...
		for(int p = 0; p < this.oGrammar.getRules().size(); p++)
		{
			// ... such that ...
			Rule loRule = (Rule)this.oGrammar.getRules().elementAt(p);

			// For all terminals...
			for(int t = 0; t < this.oGrammar.getTerminalList().size(); t++)
			{
				Terminal loTerminal = (Terminal)this.oGrammar.getTerminalList().elementAt(t);

				if(loTerminal.getType().subtype == GrammarTokenType.EPSILON)
				{
					//continue;
				}

				// ...belonging to the first set of the RHS
				for(int a = 0; a < loRule.getRHS().size(); a++)
				{
					GrammarElement this.oGrammarElement = (GrammarElement)loRule.getRHS().elementAt(a);

					// Add p to TT[LHS, t]
					if(this.oGrammarElement.getFirstSet().contains(loTerminal))
				//	if(loRule.getLHS().getFirstSet().contains(loTerminal))
					{
						if(this.oTransitionTable.getEntryAt(loRule.getLHS(), loTerminal) != null)
						{
							Rule loCellRule = (Rule)this.oTransitionTable.getEntryAt(loRule.getLHS(), loTerminal);

							if(loRule.equals(loCellRule) == false)
							{
								System.out.println("p: GrammarCompiler::fillInTransitionTable() - ERROR: Overwriting cell with a rule in it!");
								System.out.println("Rule in the table cell: " + loCellRule.toAbbrString() + ": " + loCellRule.toString());
								System.out.println("Overwriting Rule      : " + loRule.toAbbrString() + ": " + loRule.toString());
								System.out.println("This means there are ambiguites in the source grammar.");
								System.out.println("Please remove them and restart grammar compilation.");
								//System.exit(1);
							}

							// For efficiency, to avoid adding dupes
							//continue;
						}

						Debug.debug
						(
							"p: Adding an entry to TT[" +
							loRule.getLHS().getName() + "," +
							loTerminal.getName() + "]: " + loRule.toString()
						);

						this.oTransitionTable.setEntryAt
						(
							loRule.getLHS(),
							loTerminal,
							loRule
						);
					}
				}
			}

			// If epsilon belongs to the first set of RHS
			// as well then for add any terminal from the follow
			// set of LHS to the TT

			for(int a = 0; a < loRule.getRHS().size(); a++)
			{
				GrammarElement this.oGrammarElement = (GrammarElement)loRule.getRHS().elementAt(a);

				if(this.oGrammarElement.getFirstSet().contains(Grammar.EpsilonTerminal))
			//	if(loRule.getLHS().getFirstSet().contains(Grammar.EpsilonTerminal))
				{
					for(int t = 0; t < this.oGrammar.getTerminalList().size(); t++)
					{
						Terminal loTerminal = (Terminal)this.oGrammar.getTerminalList().elementAt(t);

						if(loTerminal.getType().subtype == GrammarTokenType.EPSILON)
						{
							//continue;
						}

						if(loRule.getLHS().getFollowSet().contains(loTerminal) == true)
						{
							if(this.oTransitionTable.getEntryAt(loRule.getLHS(), loTerminal) != null)
							{
								Rule loCellRule = (Rule)this.oTransitionTable.getEntryAt(loRule.getLHS(), loTerminal);

								if(loRule.equals(loCellRule) == false)
								{
									System.out.println("&: GrammarCompiler::fillInTransitionTable() - ERROR: Overwriting cell with a rule in it!");
									System.out.println("Rule in the table cell: " + loCellRule.toAbbrString() + ": " + loCellRule.toString());
									System.out.println("Overwriting Rule      : " + loRule.toAbbrString() + ": " + loRule.toString());
									System.out.println("This means there are ambiguites in the source grammar.");
									System.out.println("Please remove them and restart grammar compilation.");
									//System.exit(1);
								}

								// For efficiency, to avoid adding dupes
								//continue;
							}

							Debug.debug
							(
								"&: Adding an entry to TT[" +
								loRule.getLHS().getName() + "," +
								loTerminal.getName() + "]: " + loRule.toString()
							);

							this.oTransitionTable.setEntryAt
							(
								loRule.getLHS(),
								loTerminal,
								loRule
							);
						}
					}
				}
			} // for a
		}
		*/

        // For every syntax error...

		/*
		 * TODO: _PROPER_ Syntax Errors to TT
		 */
        for (int r = 0; r < this.oGrammar.getNonTerminalList().size(); r++) {
            NonTerminal oCurrentNonTerminal = (NonTerminal) this.oGrammar.getNonTerminalList().elementAt(r);

            for (int t = 0; t < this.oGrammar.getTerminalList().size(); t++) {
                Terminal oCurrentTerminal = (Terminal) this.oGrammar.getTerminalList().elementAt(t);

                if (soTransitionTable.getEntryAt(oCurrentNonTerminal, oCurrentTerminal) == null) {
                    soTransitionTable.setEntryAt
                            (
                                    oCurrentNonTerminal,
                                    oCurrentTerminal,
                                    new SyntaxError(SyntaxError.ERR_GENERAL_SYNTAX_ERROR)
                            );

                    Debug.debug("Error at: [" + oCurrentNonTerminal.getName() + "," + oCurrentTerminal.getName() + "]");
                }
            }
        }
    }

    /**
     * Loads (previously serialized) state of the TT.
     * Method declared as static and can be called without an
     * instance of the GrammarCompiler.
     *
     * @param pstrTTFileName filename of a file with previously stored transition table
     * @return reference to the newly loaded instance of the TransitionTable data structure
     * @throws StorageException if there was any problem loading the table from file
     */
    public static TransitionTable loadTT(String pstrTTFileName)
            throws StorageException {
        try {
            // Load from
            FileInputStream oFIS = new FileInputStream(pstrTTFileName);

            // Compressed
            GZIPInputStream oGZIS = new GZIPInputStream(oFIS);

            // Load objects
            ObjectInputStream oOIS = new ObjectInputStream(oGZIS);

            TransitionTable oTT = (TransitionTable) oOIS.readObject();

            oOIS.close();

            soTransitionTable = oTT;

            return oTT;
        } catch (Exception e) {
            System.err.println("GrammarCompiler::loadTT() - ERROR: " + e.getMessage());
            e.printStackTrace(System.err);

            throw new StorageException(e.getMessage(), e);
        }
    }

    /**
     * Text serialization routine for grammar
     * compilation. Loading is not implemented.
     * <p>
     * TODO: migrate to standard MARF's way of serialization
     *
     * @param piOperation 0 for load and 1 for save
     * @return <code>true</code> if the I/O operation was successful
     */
    public boolean serialize(int piOperation) {
        if (piOperation == 0) {
			/*
			 * TODO: Token::serialize(LOAD)
			 */
            System.err.println("Token::serialize(LOAD) - Not implemented.");
            return false;
        } else {
            boolean bSuccess = this.oGrammarAnalyzer.serialize(1);

			/*
			System.out.println("Grammar Compiler Trace Output");
			System.out.println("-----------------------------");
			this.oTransitionTable.serialize(1);
			*/

            // Write down rules
            try {
                if (this.oGrammar.getRules().size() > 0) {
                    FileWriter oFileWriterRules = new FileWriter(Grammar.DEFAULT_RULES_FILE);

                    oFileWriterRules.write
                            (
                                    "-----------------------------------\n" +
                                            "MARF Grammar Rules\n" +
                                            "Source file: \"" + getGrammarFileName() + "\"\n" +
                                            "Rules: " + this.oGrammar.getRules().size() + "\n" +
                                            "-----------------------------------\n\n"
                            );

                    oFileWriterRules.write
                            (
                                    "Synopsis:\n" +
                                            "   Rule#: <nonTerminal> -> RHS\n\n"
                            );

                    //this.oGrammar.serialize(1, loFileWriterRules);
                    Enumeration oRuleList = this.oGrammar.getRules().elements();

                    while (oRuleList.hasMoreElements()) {
                        Rule oGrammarRule = (Rule) oRuleList.nextElement();
                        oFileWriterRules.write("R" + oGrammarRule.getID() + ": " + oGrammarRule + "\n");
                    }

                    oFileWriterRules.flush();
                }
            } catch (IOException e) {
                System.err.println("GrammarCompler::serialize() - " + e.getMessage());
                e.printStackTrace(System.err);

                bSuccess = false;
            }

            // Write down first sets
            try {
                if (this.oGrammar.getRules().size() > 0) {
                    FileWriter oFileWriter = new FileWriter(Grammar.DEFAULT_FIRST_SETS_FILE);

                    oFileWriter.write
                            (
                                    "-----------------------------------\n" +
                                            "MARF Grammar First Sets\n" +
                                            "Source file: \"" + getGrammarFileName() + "\"\n" +
                                            //"Rules: " + this.oGrammar.getRules().size() + "\n" +
                                            "-----------------------------------\n\n"
                            );

                    oFileWriter.write
                            (
                                    "Synopsis:\n" +
                                            "   First set of <nonTerminal>: { [terminal1 [terminal 2] ...] }\n\n"
                            );

                    for (int i = 0; i < this.oGrammar.getNonTerminalList().size(); i++) {
                        oFileWriter.write
                                (
                                        "First set of " +
                                                ((NonTerminal) this.oGrammar.getNonTerminalList().elementAt(i)).getName() +
                                                ": { "
                                );

                        for (int j = 0; j < ((NonTerminal) this.oGrammar.getNonTerminalList().elementAt(i)).getFirstSet().size(); j++) {
                            oFileWriter.write
                                    (
                                            ((GrammarElement) ((NonTerminal) this.oGrammar.getNonTerminalList().elementAt(i)).getFirstSet().elementAt(j)).getName()
                                                    + " "
                                    );

                        }

                        oFileWriter.write("}\n");
                    }

                    oFileWriter.flush();
                }
            } catch (IOException e) {
                System.err.println("GrammarCompler::serialize() - " + e.getMessage());
                e.printStackTrace(System.err);

                bSuccess = false;
            }

            // Write down follow sets
            try {
                if (this.oGrammar.getRules().size() > 0) {
                    FileWriter oFileWriter = new FileWriter(Grammar.DEFAULT_FOLLOW_SETS_FILE);

                    oFileWriter.write
                            (
                                    "-----------------------------------\n" +
                                            "MARF Grammar Follow Sets\n" +
                                            "Source file: \"" + getGrammarFileName() + "\"\n" +
                                            "-----------------------------------\n\n"
                            );

                    oFileWriter.write
                            (
                                    "Synopsis:\n" +
                                            "   Follow set of <nonTerminal>: { [terminal1 [terminal 2] ...] }\n\n"
                            );

                    for (int i = 0; i < this.oGrammar.getNonTerminalList().size(); i++) {
                        oFileWriter.write
                                (
                                        "Follow set of " +
                                                ((NonTerminal) this.oGrammar.getNonTerminalList().elementAt(i)).getName() +
                                                ": { "
                                );

                        for (int j = 0; j < ((NonTerminal) this.oGrammar.getNonTerminalList().elementAt(i)).getFollowSet().size(); j++) {
                            oFileWriter.write
                                    (
                                            ((GrammarElement) ((NonTerminal) this.oGrammar.getNonTerminalList().elementAt(i)).getFollowSet().elementAt(j)).getName()
                                                    + " "
                                    );

                        }

                        oFileWriter.write("}\n");
                    }

                    oFileWriter.flush();
                }
            } catch (IOException e) {
                System.err.println("GrammarCompler::serialize() - " + e.getMessage());
                e.printStackTrace(System.err);

                bSuccess = false;
            }

            return bSuccess;
        }
    }

    /**
     * Allows querying for inner instance of the grammar.
     *
     * @return the contained Grammar object
     */
    public final Grammar getGrammar() {
        return this.oGrammar;
    }

    /**
     * Allows querying for the filename of the grammar.
     *
     * @return the current grammar file name
     */
    public final String getGrammarFileName() {
        return this.strGrammarFileName;
    }

    /**
     * Allows querying for the inner transition table data structure.
     *
     * @return the reference to the transition table
     */
    public static final TransitionTable getTransitionTable() {
        return soTransitionTable;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.29 $";
    }
}

// EOF
