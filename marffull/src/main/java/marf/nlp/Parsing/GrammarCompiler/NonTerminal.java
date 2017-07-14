package marf.nlp.Parsing.GrammarCompiler;

import java.util.Vector;

import marf.nlp.Parsing.Token;


/**
 * <p>Non-terminal grammar element.
 * E.g.: &lt;prog&gt;.</p>
 * <p>
 * $Id: NonTerminal.java,v 1.10 2005/12/24 04:01:11 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.10 $
 * @since 0.3.0.2
 */
public class NonTerminal
        extends GrammarElement {
    /**
     * Flag indicating whether this non-terminal is
     * fefined or not.
     */
    protected boolean bDefined = false;

    /**
     * Follow set of this non-terminal.
     */
    protected Vector oFollowSet = new Vector();

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 5638226005744022394L;

    /**
     * Creates a non-terminal by a name and ID.
     *
     * @param pstrName the name
     * @param piID     the ID
     */
    public NonTerminal(String pstrName, int piID) {
        super(pstrName, piID);
    }

    /**
     * Creates a non-terminal from a token and the ID.
     *
     * @param poToken the token
     * @param piID    the ID.
     */
    public NonTerminal(Token poToken, int piID) {
        super(poToken, piID);
    }

    /**
     * Tests whether this non-terminal was defined at some
     * point in the grammar.
     *
     * @return <code>true</code> if the non-terminal defined as a rule;
     * <code>false</code> otherwise
     */
    public boolean isDefined() {
        return this.bDefined;
    }

    /**
     * Marks this non-terminal as defined.
     * Only allow to set it, but not unset.
     */
    public void setDefined() {
        this.bDefined = true;
    }

    /**
     * Allows to query for the collection of
     * the follow set elements of this non-terminal.
     *
     * @return the follow set collection
     */
    public Vector getFollowSet() {
        return this.oFollowSet;
    }

    /**
     * Appends elements from the passed
     * set to its current follow set.
     *
     * @param poSet the collection of elements to append
     * @return <code>true</code> if the current follow set has
     * changed as a result of addition; <code>false</code> otherwise
     */
    public boolean addToFollowSet(Vector poSet) {
        boolean bChanged = false;

        for (int i = 0; i < poSet.size(); i++) {
            if (this.oFollowSet.contains(poSet.elementAt(i)) == false) {
                this.oFollowSet.addElement(poSet.elementAt(i));
                bChanged = true;
            }
        }

        return bChanged;
    }

    /**
     * Adds a single terminal to the follow set if
     * it's not already there.
     *
     * @param poTerminal the terminal to add
     * @return <code>true</code> if the current follow set has
     * changed as a result of addition; <code>false</code> otherwise
     */
    public boolean addToFollowSet(Terminal poTerminal) {
        if (this.oFollowSet.contains(poTerminal) == false) {
            this.oFollowSet.addElement(poTerminal);
            return true;
        }

        return false;
    }

    /**
     * Adds a single non-terminal to the follow set.
     *
     * @param poNonTerminal the non-terminal to add
     * @return <code>true</code> if the current follow set has
     * changed as a result of addition; <code>false</code> otherwise
     */
    public boolean addToFollowSet(NonTerminal poNonTerminal) {
        if (this.oFollowSet.contains(poNonTerminal) == false) {
            this.oFollowSet.addElement(poNonTerminal);
            return true;
        }

        return false;
    }

    /**
     * @return <code>false</code>
     * @see marf.nlp.Parsing.GrammarCompiler.GrammarElement#isTerminal()
     */
    public boolean isTerminal() {
        return false;
    }

    /**
     * @return <code>true</code>
     * @see marf.nlp.Parsing.GrammarCompiler.GrammarElement#isNonTerminal()
     */
    public boolean isNonTerminal() {
        return true;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.10 $";
    }
}

// EOF
