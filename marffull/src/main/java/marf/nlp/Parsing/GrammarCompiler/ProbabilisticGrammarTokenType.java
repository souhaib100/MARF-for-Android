package marf.nlp.Parsing.GrammarCompiler;

/**
 * Probabilistic Grammar Toke Type.
 * <p>
 * $Id: ProbabilisticGrammarTokenType.java,v 1.11 2006/01/19 04:13:17 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.11 $
 * @since 0.3.0
 */
public class ProbabilisticGrammarTokenType
        extends GrammarTokenType {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 2537398679757478875L;

    /**
     * Probability token.
     */
    public static final int PROBABILITY = 56;

    /**
     * Dictionary word (smth in between an identifier and a keyword).
     */
    public static final int DICT_WORD = 57;

    /**
     * Constructor.
     */
    public ProbabilisticGrammarTokenType() {
        super();

        soTokenTypes.put(new Integer(PROBABILITY), new String("PROBABILITY"));
        soTokenSubTypes.put(new Integer(DICT_WORD), new String("DICT_WORD"));
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
