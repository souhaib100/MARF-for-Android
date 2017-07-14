package marf.nlp.Parsing.GrammarCompiler;

import java.io.Serializable;
import java.util.Vector;

import marf.nlp.Parsing.SyntaxError;
import marf.util.Debug;


/**
 * <p>Represents the language Grammar data structure.
 * Encapsulates collections of terminals, non-terminals,
 * and rules of the current grammar that can be serialized
 * and reloaded on demand.
 * </p>
 * <p>
 * $Id: Grammar.java,v 1.21 2006/01/21 02:35:32 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.21 $
 * @since 0.3.0.2
 */
public class Grammar
        implements Serializable {
    /**
     * Default filename to output first sets.
     */
    public static final String DEFAULT_FIRST_SETS_FILE = "first-sets.dat";

    /**
     * Default filename to output follow sets.
     */
    public static final String DEFAULT_FOLLOW_SETS_FILE = "follow-sets.dat";

    /**
     * Default filename to output rule productions.
     */
    public static final String DEFAULT_RULES_FILE = "rules.dat";

    /**
     * List of terminals.
     */
    protected Vector oTerminalList = new Vector();

    /**
     * List of non-terminals.
     */
    protected Vector oNonTerminalList = new Vector();

    /**
     * A vector of rules.
     */
    protected Vector oRules = new Vector();

    /**
     * A local EOF terminal reference.
     */
    protected Terminal oEOFTerminal;

    /**
     * The epsilon token.
     */
    protected Terminal oEpsilonTerminal;

    /**
     * A reference to the starting state.
     */
    protected NonTerminal oStartNonTerminal;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 6609212530215315816L;

    /**
     * Default constructor.
     */
    public Grammar() {
        this.oEOFTerminal = null;
        this.oEpsilonTerminal = null;
        this.oStartNonTerminal = null;
    }

    /**
     * Computes first sets for all non-ternimals
     * from the non-terminals list.
     */
    public void computeFirstSets() {
        System.out.println("Computing first sets...");

        boolean bThereAreChanges = true;

        while (bThereAreChanges) {
            bThereAreChanges = false;

            // For every production rule...
            for (int i = 0; i < this.oRules.size(); i++) {
                Rule oRule = (Rule) this.oRules.elementAt(i);

                Debug.debug("Current rule: " + oRule);

                NonTerminal oLHS = oRule.getLHS();
                Vector oRHS = oRule.getRHS();

                // Add first set of every RHS grammar element
                // to the existing first set of the LHS minus epsilon
                int j;

                for (j = 0; j < oRHS.size(); j++) {
                    GrammarElement oGrammarElement = (GrammarElement) oRHS.elementAt(j);

                    // Skip Semantic Tokens
                    if (oGrammarElement.isNonTerminal() == false && oGrammarElement.isTerminal() == false) {
                        continue;
                    }

                    Vector oRHSFirstSet = (Vector) oGrammarElement.getFirstSet().clone();

                    if (oRHSFirstSet.contains(this.oEpsilonTerminal)) {
                        oRHSFirstSet.removeElement(this.oEpsilonTerminal);
                    }

                    if (oLHS.addToFirstSet(oRHSFirstSet)) {
                        bThereAreChanges = true;
                    }

                    // If current RHS element has an epsilon in it,
                    // add first set of the next RHS element in this production.

                    if (oGrammarElement.getFirstSet().contains(this.oEpsilonTerminal) == false) {
                        Debug.debug("No epsilon found. Next...");
                        break;
                    } else {
                        Debug.debug(" - epsilon.");
                    }
                } // for j

                // In case RHS every element has an epsilon in it,
                // add it also to the LHS

                if (j == oRHS.size()) {
                    oLHS.addToFirstSet(this.oEpsilonTerminal);
                }
            } // for i
        } // while
    }

    /**
     * Computes follow sets for all non-ternimals
     * from the non-terminals list.
     *
     * @throws SyntaxError if there is no starting non-terminal
     */
    public void computeFollowSets()
            throws SyntaxError {
        System.out.println("Computing follow sets...");

        if (this.oStartNonTerminal == null) {
            throw new SyntaxError
                    (
                            "Grammar::computeFollowSets() - ERROR: No starting non-terminal symbol was found!"
                    );
        }

        // Add EOF to the follow set of our
        // starting symbol

        this.oStartNonTerminal.addToFollowSet(this.oEOFTerminal);

        boolean bThereAreChanges = true;

        // While at least one follow set has changed.
        while (bThereAreChanges) {
            bThereAreChanges = false;

            // Loop through each grammar rule (production).
            Rule oRule = null;

            for (int k = 0; k < this.oRules.size(); k++) {
                oRule = (Rule) this.oRules.elementAt(k);
                Vector oRHSList = oRule.getRHS();

                // For every RHS elements and add the first set of each and
                // everyone to our current follow set

                int iRHSLen = oRHSList.size();

                for (int i = 0; i < iRHSLen; ++i) {
                    GrammarElement oRHSElem = (GrammarElement) oRHSList.elementAt(i);

                    // Skip Semaantic Tokens
                    if (oRHSElem.isNonTerminal() == false && oRHSElem.isTerminal() == false) {
                        continue;
                    }

                    if (oRHSElem.isNonTerminal() == true) {
                        // For all RHS elements after current RHS element from the above
                        // and add their first sets without an epsilon to the above
                        // RHS' follow set.

                        int j;

                        for (j = i + 1; j < iRHSLen; ++j) {
                            GrammarElement oNextRHSElem = (GrammarElement) oRHSList.elementAt(j);

                            Vector oNextRHSElemFirstSet = (Vector) oNextRHSElem.getFirstSet().clone();

                            if (oNextRHSElemFirstSet.contains(this.oEpsilonTerminal)) {
                                oNextRHSElemFirstSet.removeElement(this.oEpsilonTerminal);
                            }

                            // Record change
                            if (((NonTerminal) oRHSElem).addToFollowSet(oNextRHSElemFirstSet)) {
                                bThereAreChanges = true;
                            }

                            // Terminate upon a given RHS here doesn't have
                            // the espsilon in it's first set
                            if (((GrammarElement) oRHSList.elementAt(j)).getFirstSet().contains(this.oEpsilonTerminal) == false) {
                                break;
                            }
                        }

                        // If all the above first sets had epsilon, we should
                        // add to the current LHS' follow set the follow set of
                        // the RHS being processed.
                        if (j == iRHSLen) {
                            Vector oLHSFollowSet = oRule.getLHS().getFollowSet();

                            if (((NonTerminal) oRHSElem).addToFollowSet(oLHSFollowSet)) {
                                bThereAreChanges = true;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks whether the named non-terminal is contained
     * in this grammar and gets its index.
     *
     * @param pstrName the name of the non-terminal to look for
     * @return the index of the non-terminal with the given name or -1 if not found
     */
    public int containsNonTerminal(final String pstrName) {
        for (int i = 0; i < this.oNonTerminalList.size(); i++) {
            if (((GrammarElement) this.oNonTerminalList.elementAt(i)).getName().equals(pstrName)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Checks whether the named terminal is contained
     * in this grammar and gets its index.
     *
     * @param pstrName the name of the terminal to look for
     * @return the index of the terminal with the given name or -1 if not found
     */
    public int containsTerminal(final String pstrName) {
        for (int i = 0; i < this.oTerminalList.size(); i++) {
            if (((GrammarElement) this.oTerminalList.elementAt(i)).getName().equals(pstrName)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Checks whether a grammar element with
     * a given name (lexeme) exists in the corresponding
     * list or not. If it does, returns it's index; -1 otherwise.
     * First, it searches for non-terminals, and if not found
     * then searches for terminals with the specified name.
     *
     * @param pstrName the name of the grammar element to look for
     * @return the index of the grammar element with the given name or -1 if not found
     */
    public int contains(final String pstrName) {
        int iIndex = containsNonTerminal(pstrName);

        if (iIndex < 0) {
            iIndex = containsTerminal(pstrName);
        }

        return iIndex;
    }

    /**
     * Similarly to <code>contains(String)</code> lookss up
     * the index of the specified grammar element.
     *
     * @param poGrammarElement a grammar element to look up.
     * @return the index of the grammar element with the given name or -1 if not found
     * @see #contains(String)
     */
    public int contains(final GrammarElement poGrammarElement) {
        return contains(poGrammarElement.getName());
    }

    /**
     * Allows querying for a grammar rule given the name of the
     * terminal and the index of the non-terminal grammar elements.
     *
     * @param pstrTerminal       the name of the terminal
     * @param piNonTerminalIndex the index of the non-terminal in the table
     * @return a reference tot he corresponding rule if found; null otherwise
     */
    public final Rule getRule(final String pstrTerminal, final int piNonTerminalIndex) {
        if (pstrTerminal == null || containsTerminal(pstrTerminal) < 0) {
            return null;
        }

        // TODO: out of bounds handling
        NonTerminal oLHS = (NonTerminal) this.oNonTerminalList.elementAt(piNonTerminalIndex);

        Rule oRule = null;

        for (int i = 0; i < this.oRules.size(); i++) {
            Rule oCurrentRule = (Rule) this.oRules.elementAt(i);

            GrammarElement oGrammarElement = (GrammarElement) oCurrentRule.getRHS().elementAt(0);

            // TODO: check whether RHS indeed has only one (terminal) element
            // and not more
            if (oGrammarElement.isTerminal()) {
                // The rule has to have both: LHS -> a
                if (oGrammarElement.getName().equals(pstrTerminal) && oCurrentRule.getLHS().equals(oLHS)) {
                    oRule = oCurrentRule;
                    break;
                }
            }
        }

        return oRule;
    }

    /**
     * Retrieves a grammar rule given the indices of the
     * LHS non-terminal and two grammar elements on the RHS.
     * The rule must be in CNF.
     * <p>
     * TODO: review in detail for error handling.
     *
     * @param piA
     * @param piB
     * @param piC
     * @return a reference to the rule objec given parameters; or null if not found
     * @throws RuntimeException if the rule found is not in CNF
     */
    public final Rule getRule(int piA, int piB, int piC) {
        Rule oRule = null;

/*		if
        (
			A < 0 || B < 0 || C < 0
			||
			A >= this.NonTerminalList.size()
			||
			B >= this.NonTerminalList.size()
			||
			C >= this.NonTerminalList.size()
		)
		{
			return null;
		}
*/
        try {
            NonTerminal oNonTerminalLHS = (NonTerminal) this.oNonTerminalList.elementAt(piA);
            NonTerminal oNonTerminalB = (NonTerminal) this.oNonTerminalList.elementAt(piB);
            NonTerminal oNonTerminalC = (NonTerminal) this.oNonTerminalList.elementAt(piC);

            Debug.debug("A = " + oNonTerminalLHS + ", B = " + oNonTerminalB + ", C = " + oNonTerminalC);

            for (int i = 0; i < this.oRules.size(); i++) {
                Rule oCurrentRule = (Rule) this.oRules.elementAt(i);

                NonTerminal oNonTerminal = oCurrentRule.getLHS();

                if (oNonTerminal.equals(oNonTerminalLHS) == false) {
                    continue;
                }

                Vector oRHS = oCurrentRule.getRHS();

                if (oRHS.size() > 2) {
                    throw new RuntimeException
                            (
                                    "Grammar.getRule() --- Grammar is not in Chomsky Normal Form: " +
                                            oCurrentRule
                            );
                }

                if (oRHS.elementAt(0).equals(oNonTerminalB) && oRHS.elementAt(1).equals(oNonTerminalC)) {
                    oRule = oCurrentRule;
                    break;
                }
            }
        } catch (NullPointerException e) {
            // Means element in the vector is null
            System.err.println("Grammar.getRule() --- NullPointerException: element in the vector is null");
            e.printStackTrace(System.err);
        } catch (ArrayIndexOutOfBoundsException e) {
            // Means either A, B, or C are outside bounds
            System.err.println("Grammar.getRule() --- ArrayIndexOutOfBoundsException: either A, B, or C are outside bounds");
            e.printStackTrace(System.err);
        }

        return oRule;
    }

    /**
     * Prforms serialization of this grammar data structure.
     * This particular implementation dumps the data in
     * the textula form to screen to STDOUT.
     * <p>
     * TODO: migrate to the MARF's dump/restore mechanisms
     *
     * @param piOperation UNUSED; (usually 0 means load, 1 means save)
     * @return <code>true</code> if successful
     */
    public boolean serialize(final int piOperation) {
        System.out.println("Terminals: ");

        for (int i = 0; i < this.oTerminalList.size(); i++) {
            Terminal oTerminal = (Terminal) this.oTerminalList.elementAt(i);
            System.out.println("(" + oTerminal.getID() + ")" + oTerminal.getName());
        }

        System.out.println("Non-Terminals:");

        for (int i = 0; i < this.oNonTerminalList.size(); i++) {
            NonTerminal oNonTerminal = (NonTerminal) this.oNonTerminalList.elementAt(i);
            System.out.println("(" + oNonTerminal.getID() + ")" + oNonTerminal.getName());
        }

        System.out.println("Rules:");

        for (int i = 0; i < this.oRules.size(); i++) {
            System.out.println("R" + i + ": " + this.oRules.elementAt(i));
        }

        System.out.println("First sets:");

        for (int i = 0; i < this.oNonTerminalList.size(); i++) {
            System.out.print
                    (
                            "First set of " +
                                    ((NonTerminal) this.oNonTerminalList.elementAt(i)).getName() +
                                    ": { "
                    );

            for (int j = 0; j < ((NonTerminal) this.oNonTerminalList.elementAt(i)).getFirstSet().size(); j++) {
                System.out.print
                        (
                                ((GrammarElement) ((NonTerminal) this.oNonTerminalList.elementAt(i)).getFirstSet().elementAt(j)).getName()
                                        + " "
                        );
            }

            System.out.println("}");
        }

        System.out.println("Follow sets:");

        for (int i = 0; i < this.oNonTerminalList.size(); i++) {
            System.out.print
                    (
                            "Follow set of " +
                                    ((NonTerminal) this.oNonTerminalList.elementAt(i)).getName() +
                                    ": { "
                    );

            for (int j = 0; j < ((NonTerminal) this.oNonTerminalList.elementAt(i)).getFollowSet().size(); j++) {
                System.out.print
                        (
                                ((GrammarElement) ((NonTerminal) this.oNonTerminalList.elementAt(i)).getFollowSet().elementAt(j)).getName()
                                        + " "
                        );

            }

            System.out.println("}");
        }

        return true;
    }

    /**
     * Adds a terminal to the set of terminals.
     *
     * @param poTerminal the terminal to add; must not be null
     */
    public void addTeminal(Terminal poTerminal) {
        assert poTerminal != null;
        this.oTerminalList.addElement(poTerminal);
    }

    /**
     * Adds non-terminal to the set of non-terminals.
     *
     * @param poNonTerminal the non-terminal to add; must not be null
     */
    public void addNonTeminal(NonTerminal poNonTerminal) {
        assert poNonTerminal != null;
        this.oNonTerminalList.addElement(poNonTerminal);
    }

    /**
     * Adds a grammar rule to the collection of rules.
     *
     * @param poRule the rule to add; must not be null
     */
    public void addRule(Rule poRule) {
        assert poRule != null;
        this.oRules.addElement(poRule);
    }

    /**
     * Allows querying for the current EOF terminal.
     *
     * @return the reference to the EOF terminal instance
     * @since 0.3.0.5
     */
    public Terminal getEOFTerminal() {
        return this.oEOFTerminal;
    }

    /**
     * Allows setting a new EOF terminal.
     *
     * @param poTerminal the terminal to become EOF
     * @since 0.3.0.5
     */
    public void setEOFTerminal(Terminal poTerminal) {
        this.oEOFTerminal = poTerminal;
    }

    /**
     * Allows querying for the current epsilon terminal.
     *
     * @return the reference to the epsilon terminal instance
     * @since 0.3.0.5
     */
    public Terminal getEpsilonTerminal() {
        return this.oEpsilonTerminal;
    }

    /**
     * Allows setting a new epsilon terminal.
     *
     * @param poEpsilonTerminal the terminal to become epsilon
     * @since 0.3.0.5
     */
    public void setEpsilonTerminal(Terminal poEpsilonTerminal) {
        this.oEpsilonTerminal = poEpsilonTerminal;
    }

    /**
     * Allows querying for the current starting non-terminal.
     *
     * @return the reference to the starting non-terminal instance
     * @since 0.3.0.5
     */
    public NonTerminal getStartNonTerminal() {
        return this.oStartNonTerminal;
    }

    /**
     * Allows setting a new starting non-terminal.
     *
     * @param poStartNonTerminal the non-terminal to become the starting point
     * @since 0.3.0.5
     */
    public void setStartNonTerminal(NonTerminal poStartNonTerminal) {
        this.oStartNonTerminal = poStartNonTerminal;
    }

    /**
     * Allows querying for the collection of terminals of this grammar.
     *
     * @return the collection of terminals
     */
    public final Vector getTerminalList() {
        return this.oTerminalList;
    }

    /**
     * Allows querying for the collection of non-terminals of this grammar.
     *
     * @return the collection of non-terminals
     */
    public final Vector getNonTerminalList() {
        return this.oNonTerminalList;
    }

    /**
     * Allows querying for the collection of rules of this grammar.
     *
     * @return the rules collection
     */
    public final Vector getRules() {
        return this.oRules;
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
