package marf.nlp.Parsing.GrammarCompiler;

import marf.nlp.Parsing.Token;


/**
 * <p>Class SematicToken, for Semantic Analysis.</p>
 * <p>
 * $Id: SemanticToken.java,v 1.10 2005/12/18 21:29:18 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.10 $
 * @since 0.3.0.2
 */
public class SemanticToken
        extends GrammarElement {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 2945717735922665260L;

    /**
     * Token-based constructor.
     *
     * @param poToken the token
     */
    public SemanticToken(Token poToken) {
        super(poToken, 0);
    }

    /**
     * Token- and ID-based constructor.
     *
     * @param poToken the token
     * @param piID    token ID
     */
    public SemanticToken(Token poToken, int piID) {
        super(poToken, piID);
    }

    /**
     * Name- and ID-based constructor.
     *
     * @param pstrName the name
     * @param piID     token ID
     */
    public SemanticToken(String pstrName, int piID) {
        super(pstrName, piID);
    }

    /**
     * @return <code>false</code>
     * @see marf.nlp.Parsing.GrammarCompiler.GrammarElement#isNonTerminal()
     */
    public boolean isNonTerminal() {
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
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.10 $";
    }
}

// EOF
