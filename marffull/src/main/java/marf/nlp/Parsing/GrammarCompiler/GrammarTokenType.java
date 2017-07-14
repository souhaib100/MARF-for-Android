package marf.nlp.Parsing.GrammarCompiler;

import marf.nlp.Parsing.TokenSubType;


/**
 * <p>Represents the data type of a grammar token.</p>
 * <p>
 * $Id: GrammarTokenType.java,v 1.13 2006/01/19 04:13:17 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.13 $
 * @since 0.3.0.2
 */
public class GrammarTokenType
        extends TokenSubType {
    /**
     * This indicates the epsilon in the grammar tokens.
     */
    public static final int EPSILON = 50;

    /**
     * Productions separator - `|'.
     */
    public static final int GRAMMAR_OR = 51;

    /**
     * Corresponds to the rule operator `::='.
     */
    public static final int RULE_OP = 52;

    /**
     * Equivalent to TokenType's ID but for grammar
     * parsing. Grammar's IDs are all non-terminals.
     */
    public static final int GRAMMAR_ID = 53;

    /**
     * End of grammar line or statement.
     */
    public static final int GRAMMAR_EOL = 54;

    /**
     * Semantic token type.
     */
    public static final int SEMANTIC_TOKEN = 55;

	/*
     * Semantic Tokens.
	 */

    /**
     * Indicates to check whether a symbol was defined.
     */
    public static final int S_CHECK_DEFINED = 100;

    /**
     * Indicates to check whether a variable was defined.
     */
    public static final int S_DEFINE_VAR = 101;

    /**
     * Indicates to check whether a function was defined.
     */
    public static final int S_DEFINE_FUNC = 102;

    /**
     * Indicates to check whether a class was defined.
     */
    public static final int S_DEFINE_CLASS = 103;

    /**
     * Indicates to check scope of applicability.
     */
    public static final int S_CHECK_SCOPE = 104;

    /**
     * Indicates to enforce integer type.
     */
    public static final int S_ENFORCE_INT = 105;

    /**
     * Indicates to do type checks and implicit casts if possible.
     */
    public static final int S_TYPE_CHECK_AND_CAST = 106;

    /**
     * Indicates to check whether a memory buffer was defined.
     */
    public static final int S_CHECK_MEMB_DEFINED = 107;

    /**
     * Indicates a missing semicolon syntax error.
     */
    public static final int SE_MISSING_SEMICOLON = 200;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -7553127246479223050L;

    /**
     * Constructor.
     * Initializes default token types and keywords.
     */
    public GrammarTokenType() {
        super();

        soTokenTypes.put(new Integer(SEMANTIC_TOKEN), new String("SEMANTIC_TOKEN"));

        // Special cases as extension to already
        // defined keywords of the MARF language
        soKeywords.put(new String("&"), new Integer(EPSILON));
        soKeywords.put(new String("|"), new Integer(GRAMMAR_OR));
        soKeywords.put(new String("$"), new Integer(EOF));
        soKeywords.put(new String("::="), new Integer(RULE_OP));
        soKeywords.put(new String("%EOL"), new Integer(GRAMMAR_EOL));

        soKeywords.put(new String("@CHECK_DEFINED"), new Integer(S_CHECK_DEFINED));
        soKeywords.put(new String("@DEFINE_VAR"), new Integer(S_DEFINE_VAR));
        soKeywords.put(new String("@DEFINE_FUNC"), new Integer(S_DEFINE_FUNC));
        soKeywords.put(new String("@DEFINE_CLASS"), new Integer(S_DEFINE_CLASS));
        soKeywords.put(new String("@CHECK_SCOPE"), new Integer(S_CHECK_SCOPE));
        soKeywords.put(new String("@ENFORCE_INT"), new Integer(S_ENFORCE_INT));
        soKeywords.put(new String("@TYPE_CHECK_AND_CAST"), new Integer(S_TYPE_CHECK_AND_CAST));
        soKeywords.put(new String("@CHECK_MEMB_DEFINED"), new Integer(S_CHECK_MEMB_DEFINED));

        soKeywords.put(new String("$MISSING_SEMICOLON"), new Integer(SE_MISSING_SEMICOLON));

        soTokenSubTypes.put(new Integer(EPSILON), new String("EPSILON"));
        soTokenSubTypes.put(new Integer(GRAMMAR_OR), new String("GRAMMAR_OR"));
        soTokenSubTypes.put(new Integer(RULE_OP), new String("RULE_OP"));
        soTokenSubTypes.put(new Integer(GRAMMAR_ID), new String("GRAMMAR_ID"));

        soTokenSubTypes.put(new Integer(S_CHECK_DEFINED), new String("@CHECK_DEFINED"));
        soTokenSubTypes.put(new Integer(S_DEFINE_VAR), new String("@DEFINE_VAR"));
        soTokenSubTypes.put(new Integer(S_DEFINE_FUNC), new String("@DEFINE_FUNC"));
        soTokenSubTypes.put(new Integer(S_DEFINE_CLASS), new String("@DEFINE_CLASS"));
        soTokenSubTypes.put(new Integer(S_CHECK_SCOPE), new String("@CHECK_SCOPE"));
        soTokenSubTypes.put(new Integer(S_ENFORCE_INT), new String("@ENFORCE_INT"));
        soTokenSubTypes.put(new Integer(S_TYPE_CHECK_AND_CAST), new String("@TYPE_CHECK_AND_CAST"));
        soTokenSubTypes.put(new Integer(S_CHECK_DEFINED), new String("@CHECK_MEMB_DEFINED"));

        soTokenSubTypes.put(new Integer(SE_MISSING_SEMICOLON), new String("$MISSING_SEMICOLON"));
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.13 $";
    }
}

// EOF
