package marf.nlp.Parsing.GrammarCompiler;


/**
 * <p>Probabilistic Rule encapsulates a rule of a
 * probabilistic grammar, which is amended with probabilities.</p>
 * <p>
 * $Id: ProbabilisticRule.java,v 1.10 2005/12/22 00:41:14 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.10 $
 * @since 0.3.0.2
 */
public class ProbabilisticRule
        extends Rule {
    /**
     * Probability of the rule.
     */
    protected double dProbability = 0.0;

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
     * Constructor with the LHS non-terminal set.
     *
     * @param poLHS
     */
    public ProbabilisticRule(NonTerminal poLHS) {
        super(poLHS);
    }

    /**
     * Sets probability of the rule.
     *
     * @param pdProbability the desired probability
     */
    public final void setProbability(final double pdProbability) {
        this.dProbability = pdProbability;
    }

    /**
     * Retrieves probability of the rule.
     *
     * @return current probability value
     */
    public final double getProbability() {
        return this.dProbability;
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
