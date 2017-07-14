package marf.nlp.Parsing.GrammarCompiler;

import java.io.Serializable;
import java.util.Vector;

import marf.nlp.Parsing.Token;


/**
 * Generic grammar token that must be subclassed.
 * <p>
 * $Id: GrammarElement.java,v 1.9 2005/12/24 19:47:39 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.9 $
 * @since 0.3.0.2
 */
public abstract class GrammarElement
        implements Serializable {
    /**
     * A name of a token to refer to.
     */
    protected String strName = "";

    /**
     * ID to act as an index to table.
     */
    protected int iID;

    /**
     * A bit extra info in the encasulted token.
     */
    protected Token oToken = null;

    /**
     * FirstSet of us.
     */
    protected Vector oFirstSet = new Vector();

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.5
     */
    private static final long serialVersionUID = -7580710676855516151L;

    /**
     * Do not allow poblic default constructor
     * because it doesn't make sense from outside.
     */
    private GrammarElement() {
    }

    /**
     * Preferred Constructor.
     *
     * @param poToken token corresponding to this grammar element
     * @param piID    element's ID
     */
    protected GrammarElement(Token poToken, int piID) {
        this.oToken = poToken;
        this.strName = this.oToken.getLexeme();
        this.iID = piID;
    }

    /**
     * Constructor by name; inner token is set to null.
     *
     * @param pstrName name of the grammar element
     * @param piID     element's ID
     */
    protected GrammarElement(String pstrName, int piID) {
        this.iID = piID;
        this.strName = pstrName;
        this.oToken = null;
    }

    // To be overridden by derivatives

    /**
     * Allows querying this grammar element for terminality.
     *
     * @return <code>true</code> if this grammar element is a terminal
     */
    public abstract boolean isTerminal();

    /**
     * Allows querying this grammar element for non-terminality.
     *
     * @return <code>true</code> if this grammar element is a non-terminal
     */
    public abstract boolean isNonTerminal();

    /**
     * Allows querying this element's ID.
     *
     * @return the ID
     */
    public final int getID() {
        return this.iID;
    }

    /**
     * Allows querying this element's name.
     *
     * @return the name
     */
    public final String getName() {
        return this.strName;
    }

    /**
     * Allows querying this element's enclosed token.
     *
     * @return the token
     */
    public final Token getToken() {
        return this.oToken;
    }

    /**
     * Appends elements from the passed
     * set to its current first set.
     * Duplicates will not be added.
     *
     * @param poSet collection of grammar elements to add to the first set
     * @return <code>true</code> if the undelying first set has change
     * as a result of operation.
     */
    public boolean addToFirstSet(Vector poSet) {
        boolean bChanged = false;

        for (int i = 0; i < poSet.size(); i++) {
            if (this.oFirstSet.contains(poSet.elementAt(i)) == false) {
                bChanged = true;
                this.oFirstSet.addElement(poSet.elementAt(i));
            }
        }

        return bChanged;
    }

    /**
     * Adds a single terminal to the first set
     * if it's not already there.
     *
     * @param poTerminal the terminal element to add
     * @return <code>true</code> if the terminal was added
     */
    public boolean addToFirstSet(Terminal poTerminal) {
        if (this.oFirstSet.contains(poTerminal) == false) {
            this.oFirstSet.addElement(poTerminal);
            return true;
        }

        return false;
    }

    /**
     * Adds a single non-terminal to the first set.
     *
     * @param poNonTerminal the non-terminal element to add
     * @return <code>true</code> if the non-terminal was added
     */
    public boolean addToFirstSet(NonTerminal poNonTerminal) {
        if (this.oFirstSet.contains(poNonTerminal) == false) {
            this.oFirstSet.addElement(poNonTerminal);
            return true;
        }

        return false;
    }

    /**
     * Allows getting the first set of this grammar element.
     *
     * @return the first set of this element
     */
    public Vector getFirstSet() {
        return this.oFirstSet;
    }

    /**
     * Overridden to return the grammar element's name.
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.strName;
    }

    /**
     * Tests whether the parameter is equal to the internal
     * name of the grammar element.
     *
     * @param pstrName name to test
     * @return <code>true</code> if the names are equal
     */
    public boolean isEqualByName(final String pstrName) {
        return this.strName.equals(pstrName);
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.9 $";
    }
}

// EOF
