/*
 * Generic Parser
 * (C) 2001 - 2006 Serguei Mokhov, <mailto:mokhov@cs.concordia.ca>
 */
package marf.nlp.Parsing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;

import marf.Storage.StorageException;
import marf.nlp.Parsing.GrammarCompiler.GrammarCompiler;
import marf.nlp.Parsing.GrammarCompiler.GrammarElement;
import marf.nlp.Parsing.GrammarCompiler.GrammarTokenType;
import marf.nlp.Parsing.GrammarCompiler.NonTerminal;
import marf.nlp.Parsing.GrammarCompiler.Rule;
import marf.nlp.Parsing.GrammarCompiler.Terminal;
import marf.util.Debug;


/**
 * <p>Generic Language Parser.</p>
 * <p>
 * $Id: Parser.java,v 1.22 2006/01/19 04:13:16 mokhov Exp $
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.22 $
 * @since 0.3.0.2
 */
public class Parser {
    /**
     * Transitinal Table
     * for Transition State Parsing Algorithm.
     */
    private TransitionTable oTT = null;

    /**
     * An instance of Lexical Analyzer.
     */
    private LexicalAnalyzer oLexer = null;

    /**
     * Global Scope Symbol Table.
     */
    private static SymbolTable soSymTab = null;

    /**
     * Grammar Compiler.
     * Needed to compile source grammar file if TT
     * does not exist.
     */
    private GrammarCompiler oGrammarCompiler = null;

    /**
     * Stack needed for
     * Transition State Parsing Algorithm.
     */
    private Stack oStack = new Stack();

    /**
     * A list of Syntax Errors so far.
     */
    protected Vector oSyntaxErrors = new Vector();

    /**
     * Fact, indicating that there are syntax errors.
     */
    protected boolean bErrorsPresent = false;

    /**
     * Stack for checking mismatched brackets.
     */
    protected Stack oBracketStack = new Stack();

    /**
     * Pushes bracket-type token onto the bracket stack.
     * Used for further validation of matching brackets,
     * parentheses, and braces.
     *
     * @param poToken the bracket-type token
     */
    public void pushBracket(Token poToken) {
        // TODO: validate whether incoming token is really a bracket type
        this.oBracketStack.push(poToken);
    }

    /**
     * Pops a bracket-type token from the stack.
     *
     * @return the top bracket-type token
     */
    public Token popBracket() {
        return (Token) this.oBracketStack.pop();
    }

    /**
     * Consrtuctor with command-line arguments.
     *
     * @param argv the command-line arguments
     * @throws CompilerError if there are problems initializing a lexer or a transition table
     */
    public Parser(String[] argv)
            throws CompilerError {
        // Getting TT ready
        System.out.println("Instantiating Transition Table... ");
        this.oTT = new TransitionTable();

        System.out.println("Trying to deserialize Transition Table from file " + this.oTT.getTableFile() + "...");

        try {
            if ((this.oTT = GrammarCompiler.loadTT(this.oTT.getTableFile())) == null) {
                System.out.println("Looks like Transition Table file either does not exist or not proper format.");
                System.out.println("Trying to compile grammar.");

                System.out.println("Instantiating Grammar Compiler...");
                this.oGrammarCompiler = new GrammarCompiler();

                System.out.println("Compiling Grammar...");
                this.oGrammarCompiler.compileGrammar();

                this.oTT = GrammarCompiler.getTransitionTable();

                if (Debug.isDebugOn()) {
                    this.oGrammarCompiler.serialize(1);
                }
            }
        } catch (StorageException e) {
            throw new CompilerError(e.getMessage(), e);
        }

        System.out.println("Transition Table Done.");

        // Getting Global Symbol Table ready
        System.out.print("Instantiating Global Symbol Table... ");
        soSymTab = new SymbolTable();
        System.out.println("Done.");

        // Getting Lexer ready
        System.out.print("Instantiating Lexical Analyzer... ");
        this.oLexer = new LexicalAnalyzer(soSymTab);
        System.out.println("Done.");

        System.out.println("Setting up the source file \"" + argv[0] + "\" to be read...");
        this.oLexer.setSourceFilename(argv[0]);

        System.out.print("Initializing Lexer... ");

        if (this.oLexer.init() == true) {
            System.out.println("Done.");

			/*
            System.out.println("Initiating scanning process...");
			Lexer.scan();
			System.out.println("Done.");

			Lexer.serialize(1);

			System.out.println("The lexical analysis output file is \"" + Lexer.getOutputFilename() + "\".");

			if(Lexer.getErrorsPresent() == true)
			{
				System.out.println("There were errors while scanning the source file.");
				System.out.println("The error log file is \"" + Lexer.getErrorLogFilename() + "\".");
			}

			System.out.println("Symbol Table has " + SymTab.getSymTabEntries().size() + " entries.");
//			*/
        } else {
            throw new CompilerError("Failed to initialize lexer...\n");
        }
    }

    /**
     * Implements State Transition Algorithm
     * for top-down table-driven predictive parser.
     *
     * @return <code>true</code> if the parse was successful
     * @throws SyntaxError  if a parsed statement for some reason does not conform the grammar
     * @throws LexicalError if an invalid token read off the source character stream
     */
    public boolean parse()
            throws SyntaxError, LexicalError {
        this.oStack.push(this.oTT.getEOFTerminal());
        this.oStack.push(this.oTT.getStartNonTerminal());

        stackDump();

        Token oNextToken = this.oLexer.getNextToken();

        try {
            FileWriter oWriter = new FileWriter(this.oLexer.getOutputFilename() + ".parse.log");

            while (this.oStack.peek().equals(this.oTT.getEOFTerminal()) == false) {
                GrammarElement oTopElement = (GrammarElement) this.oStack.peek();

                // Sematic Token
                if (!oTopElement.isNonTerminal() && !oTopElement.isTerminal()) {
                    // Take it out from the stack
                    this.oStack.pop();

                    // Perform action of the token

					/*
                     * TODO
					 */

                    // Continue to the next token
                    continue;
                }

                if (oTopElement.isTerminal()) {
	/*
					// Pop epsilon terminal and continue
					if(((Terminal)oTopElement).getName().compareTo("&") == 0)
					{
						oStack.pop();
						//this.stackDump();
						continue;
					}
	//*/
                    Debug.debug
                            (
                                    "Matching [" + ((Terminal) oTopElement).getName() +
                                            "," + oNextToken.getLexeme() + "]"
                            );

                    if
                            (
                        // Either keywords and other language symbols
                            (((Terminal) oTopElement).getName().compareTo(oNextToken.getLexeme()) == 0)

                                    ||

                                    // Or user-defined IDs
                                    (
                                            (((Terminal) oTopElement).getName().compareTo("ID") == 0)
                                                    &&
                                                    (oNextToken.getTokenType().iType == TokenType.ID)
                                    )

                                    ||

                                    // NUM
                                    (
                                            (((Terminal) oTopElement).getName().compareTo("NUM") == 0)
                                                    &&
                                                    (oNextToken.getTokenType().iType == TokenType.NUM)
                                    )

                                    ||

                                    // INTEGER
                                    (
                                            (((Terminal) oTopElement).getName().compareTo("INTEGER") == 0)
                                                    &&
                                                    (oNextToken.getTokenType().iSubtype == GrammarTokenType.INTEGER)
                                    )

                        //||

                        // Epsilon
                        //(((Terminal)oTopElement).getName().compareTo("&") == 0)
                            ) {
                        this.oStack.pop();
                        stackDump();
                        oNextToken = this.oLexer.getNextToken();
                    } else {
                        skipErrors();
                        this.bErrorsPresent = true;
                    }
                }

                // NonTerminal is on the top
                else {
                    System.out.println
                            (
                                    "Trying to get element TT[" + oTopElement.getName() +
                                            "(" + oTopElement.getID() + ")," + oNextToken.getLexeme() + "]"
                            );

                    Object oTTEntry = this.oTT.getEntryAt((NonTerminal) oTopElement, oNextToken);

                    // Unexpected error - null entry in TT
                    if (oTTEntry == null) {
                        throw new SyntaxError
                                (
                                        "Parser::pasre() - ERROR: Unexpected missing entry in Transition Table: " +
                                                "[" + oTopElement.getName() + "," + oNextToken.getLexeme() + "]\n" +
                                                "Looks like Scanner and TT are not it sync."
                                );
                    }

                    System.out.println(oTTEntry.getClass().getName());

                    // It's either a Rule
                    if (oTTEntry instanceof Rule) {
                        Rule oRule = ((Rule) this.oTT.getEntryAt((NonTerminal) oTopElement, oNextToken));

                        oWriter.write(oRule.toAbbrString() + ": " + oRule.toString() + "\n");
                        System.out.println(oRule.toAbbrString() + ": " + oRule.toString());

                        this.oStack.pop();

                        inverseMultiplePush((NonTerminal) oTopElement, oNextToken);
                        stackDump();
                    }

                    // Or a SyntaxError
                    else {
                        assert oTTEntry instanceof SyntaxError;

                        StringBuffer oBuffer = new StringBuffer();

                        oBuffer
                                .append("parse(): ")
                                .append(((SyntaxError) this.oTT.getEntryAt((NonTerminal) oTopElement, oNextToken)).getMessage())
                                .append("\n");

                        oWriter.write(oBuffer.toString());
                        System.err.println(oBuffer);

                        skipErrors();
                        this.bErrorsPresent = true;
                    }
                }
            }

            oWriter.flush();
            oWriter.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace(System.err);
            this.bErrorsPresent = true;
        }

        return ((oNextToken.getTokenType().iType != TokenType.EOF) || this.bErrorsPresent) ? false : true;

		/*
		while(oNextToken.getTokenType().type != TokenType.EOF)
		{
			if(oNextToken.getTokenType().type != TokenType.BACKTRACK)
			{
				this.oTokenList.addElement(oNextToken);
			}

			oNextToken = getNextToken();
		}
		*/
    }

    /**
     * Error recovery function.
     * Skips syntax errors until next valid symbol in TT.
     *
     * @throws LexicalError if invalid token is encounterd
     */
    private void skipErrors()
            throws LexicalError {
        System.out.println("Parser::skipErrors() - errors encountered. Recovering...");

        Token oLookahead = this.oLexer.getNextToken();
        GrammarElement oTopElement = (GrammarElement) this.oStack.peek();

        if
                (
            // TODO: fix hardcoding
                (oLookahead.getLexeme().equals("$"))
                        ||
                        (
                                oTopElement.isNonTerminal()
                                        &&
                                        ((NonTerminal) oTopElement).getFollowSet().contains(oLookahead)
                        )
                ) {
            System.out.println("Parser::skipErrors() - popping " + oTopElement.getName());
            this.oStack.pop();
        } else {
            oLookahead = this.oLexer.getNextToken();
            this.oStack.pop();
            //this.oStack.push(this.oTT.getGrammarElement(oLookahead.getLexeme()));
            //System.out.println("Parser::skipErrors() - pushing " + oLookahead.getLexeme());
        }
    }

    /**
     * Pushes multiple tokens on a RHS of a rule
     * into the stack in the reverse order.
     *
     * @param poNonTerminal non-terminal to recover the corresponding rule
     * @param poToken       token to recover the corresponding rule
     */
    private void inverseMultiplePush(NonTerminal poNonTerminal, Token poToken) {
        Rule oRule = (Rule) this.oTT.getEntryAt(poNonTerminal, poToken);

        // Don't push epsilon
        // TODO: fix hardcoding
        if
                (
                oRule.getRHS().size() == 1
                        && (((GrammarElement) oRule.getRHS().elementAt(0)).getName().equals("&"))
                ) {
            return;
        }

        for (int i = oRule.getRHS().size() - 1; i >= 0; i--) {
            this.oStack.push(oRule.getRHS().elementAt(i));
        }
    }

    /**
     * Performs textual stack dump.
     */
    private void stackDump() {
        System.out.println("Stack dump (bottom to top):");
        System.out.println("------------8<-------------");

        for (int i = 0; i < this.oStack.size(); i++) {
            System.out.println(i + ": " + ((GrammarElement) this.oStack.elementAt(i)).getName());
        }

        System.out.println("------------8<-------------");
    }

    /**
     * Allows querying for the current symbol table.
     *
     * @return the reference to the contained symbol table
     * @since 0.3.0.5
     */
    public static SymbolTable getSymbolTable() {
        return soSymTab;
    }

    /**
     * Allows setting a new symbol table.
     *
     * @param poSymTab the global symbol table to set
     * @since 0.3.0.5
     */
    public static void setSymbolTable(SymbolTable poSymTab) {
        soSymTab = poSymTab;
    }

    /**
     * Allows to query the fact that there were lexical or syntax errors or not.
     *
     * @return <code>true</code> if there were errors in parsing
     * @since 0.3.0.5
     */
    public boolean areErrorsPresent() {
        return this.bErrorsPresent;
    }

    /**
     * Allows externally setting the fact that there were errors.
     *
     * @param pbErrorsPresent the new value of the flag
     * @since 0.3.0.5
     */
    public void setErrorsPresent(boolean pbErrorsPresent) {
        this.bErrorsPresent = pbErrorsPresent;
    }

    /**
     * Allows querying for the instance of the lexer.
     *
     * @return the reference to the lexer used
     * @since 0.3.0.5
     */
    public LexicalAnalyzer getLexer() {
        return this.oLexer;
    }

    /**
     * Allows setting a new lexer for scanning.
     *
     * @param poLexer the new lexer to be used
     * @since 0.3.0.5
     */
    public void setLexer(LexicalAnalyzer poLexer) {
        this.oLexer = poLexer;
    }

    /**
     * Allows querying for the grammar compiler used.
     *
     * @return the reference to the contained grammar compiler
     * @since 0.3.0.5
     */
    public GrammarCompiler getGrammarCompiler() {
        return this.oGrammarCompiler;
    }

    /**
     * Allows setting a new grammar compiler to use.
     *
     * @param poGrammarCompiler the new grammar compiler
     * @since 0.3.0.5
     */
    public void setGrammarCompiler(GrammarCompiler poGrammarCompiler) {
        this.oGrammarCompiler = poGrammarCompiler;
    }

    /**
     * Allows querying for the current stack.
     *
     * @return the current stack data structure
     * @since 0.3.0.5
     */
    public Stack getStack() {
        return this.oStack;
    }

    /**
     * Allows querying for the current transition table.
     *
     * @return the current transition table data structure
     * @since 0.3.0.5
     */
    public TransitionTable getTransitionTable() {
        return this.oTT;
    }

    /**
     * Allows setting a new transition table.
     *
     * @param poTT the new transition table data structure to set
     * @since 0.3.0.5
     */
    public void setTransitionTable(TransitionTable poTT) {
        this.oTT = poTT;
    }

    /**
     * Allows querying for the recorded syntax errors that were found.
     *
     * @return the collection of syntax errors
     * @since 0.3.0.5
     */
    public Vector getSyntaxErrors() {
        return this.oSyntaxErrors;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.22 $";
    }
}

// EOF
