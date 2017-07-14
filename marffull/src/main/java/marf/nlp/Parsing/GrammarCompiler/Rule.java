package marf.nlp.Parsing.GrammarCompiler;

import java.io.Serializable;
import java.util.Vector;


/**
 * <p>Language Grammar Rule.</p>
 * <p>
 * $Id: Rule.java,v 1.11 2006/01/01 01:28:17 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.11 $
 * @since 0.3.0.2
 */
public class Rule
        implements Serializable {
    /**
     * ID for reference and debugging.
     */
    protected int iID;

    /**
     * Left-hand side of this rule.
     * Must be a non-terminal.
     */
    protected NonTerminal oLHS = null;

    /**
     * A vector of grammar elements
     * on the right-hand-side of a production.
     */
    protected Vector oRHS = new Vector();

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 1346265103950170148L;

    /**
     * Constructor with LHS.
     *
     * @param poLHS the default LHS non-terminal
     */
    public Rule(NonTerminal poLHS) {
        this.oLHS = poLHS;
    }

    /**
     * Allows adding grammar elements on the RHS of this rule.
     *
     * @param poGrammarElement RHS grammar element to add
     */
    public void addRHSElement(final GrammarElement poGrammarElement) {
        this.oRHS.addElement(poGrammarElement);
    }

    /**
     * Function that computes the first set of the right hand side of the current
     * grammar rule.
     *
     * @param poEpsilon what to consider a termination element
     * @return a collection of the elements in the first set
     */
    public Vector getRHSFirstSet(final GrammarElement poEpsilon) {
        Vector oFirstSet = new Vector();

        boolean bAllEpsilon = true;

        //GrammarElement rhsElemOfProduction = null;

        for (int j = 0; j < oRHS.size() && bAllEpsilon; j++) {
            // Add the first set of each rhs elem to the set (as long as all the
            // previous ones contained epsilon).
            GrammarElement oProdElement = (GrammarElement) oRHS.elementAt(j);

            // Skip semantic tokens here
            if (oProdElement.isNonTerminal() == false && oProdElement.isTerminal() == false) {
                continue;
            }

            Vector oNextFirstSet = (Vector) oProdElement.getFirstSet().clone();

            for (int f = 0; f < oNextFirstSet.size(); f++) {
                Terminal oTerminal = (Terminal) oNextFirstSet.elementAt(f);
                oFirstSet.addElement(oTerminal);
            }

            if (oFirstSet.contains(poEpsilon)) {
                oFirstSet.removeElement(poEpsilon);
            } else {
                bAllEpsilon = false;
            }
        }

        // If a first set of every element of a RHS production
        // had epsilon in it, we add it to the FirstSet as well
        if (bAllEpsilon) {
            oFirstSet.addElement(poEpsilon);
        }

        return oFirstSet;
    }

    /**
     * Overrides Object's toString()
     * to human-readable production rule.
     *
     * @return rule dump in the textual form
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer oRhsStrings = new StringBuffer();

        oRhsStrings.append(this.oLHS).append(" -> ");

        for (int i = 0; i < this.oRHS.size(); i++) {
            oRhsStrings.append(this.oRHS.elementAt(i));

            if (i != this.oRHS.size() - 1) {
                oRhsStrings.append(" ");
            }
        }

        return oRhsStrings.toString();
    }

    /**
     * Returns analogous representation of a rule
     * to that of <code>toString()</code> except
     * numeric IDs are used on the LHS.
     *
     * @return textual rule dump with numeric IDs on the LHS
     * @see #toString()
     */
    public String toStringNumeric() {
        StringBuffer oRhsStrings = new StringBuffer();

        oRhsStrings.append(this.oLHS.getID()).append("->");

        for (int i = 0; i < this.oRHS.size(); i++) {
            oRhsStrings.append(((GrammarElement) this.oRHS.elementAt(i)).getID());

            if (i != this.oRHS.size() - 1) {
                oRhsStrings.append(" ");
            }
        }

        return oRhsStrings.toString();
    }

    /**
     * Just returns rule's ID prefixed with "R" as a string.
     *
     * @return rule's ID in string form
     */
    public String toAbbrString() {
        return new String("R" + this.iID);
    }

    /**
     * Allows setting a new LHS from the outside.
     *
     * @param poNewLHS new LHS non-terminal
     */
    public void setLHS(NonTerminal poNewLHS) {
        this.oLHS = poNewLHS;
    }

    /**
     * Allows setting a new RHS from the outside.
     *
     * @param poNewRHS new collection of the RHS elements
     */
    public void setRHS(Vector poNewRHS) {
        this.oRHS = poNewRHS;
    }

    /**
     * Allows getting current LHS non-terminal of this rule.
     *
     * @return the LHS non-terminal
     */
    public NonTerminal getLHS() {
        return this.oLHS;
    }

    /**
     * Allows getting the RHS part of this rule.
     *
     * @return current collection of the RHS elements
     */
    public Vector getRHS() {
        return this.oRHS;
    }

    /**
     * Allows getting this rule's ID.
     *
     * @return the ID
     */
    public int getID() {
        return this.iID;
    }

    /**
     * Allows setting this rule's ID.
     *
     * @param piID new ID of the rule.
     */
    public void setID(int piID) {
        this.iID = piID;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.11 $";
    }
}

// EOF
