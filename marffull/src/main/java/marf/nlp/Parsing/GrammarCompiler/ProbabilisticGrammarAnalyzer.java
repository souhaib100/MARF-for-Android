package marf.nlp.Parsing.GrammarCompiler;

import java.io.IOException;
import java.io.StreamTokenizer;

import marf.nlp.Parsing.LexicalError;
import marf.nlp.Parsing.TokenSubType;
import marf.nlp.Parsing.TokenType;
import marf.util.Debug;


/**
 * <p>Pobabilistic Grammar Analyzer -- an lexical analyzer designed
 * for probabilistic grammars out of a stream of characters.</p>
 * <p>
 * $Id: ProbabilisticGrammarAnalyzer.java,v 1.15 2006/01/19 04:13:17 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.15 $
 * @since 0.3.0.2
 */
public class ProbabilisticGrammarAnalyzer
        extends GrammarAnalyzer {
    /**
     * Constructs probabilistic grammar analyzer from a given
     * grammar file.
     *
     * @param pstrGrammarFilename the filename of a probabilistic grammar
     */
    public ProbabilisticGrammarAnalyzer(String pstrGrammarFilename) {
        super(pstrGrammarFilename);
    }

    /**
     * Initializes this grammar analyzer and sets the stream
     * tokenizer to regonize numerical values specially and
     * don't fold anything to lowercase.
     *
     * @return <code>true</code> if parent's initialization was successful
     * @see marf.nlp.Parsing.GenericLexicalAnalyzer#init()
     */
    public boolean init() {
        boolean bRetVal = super.init();

        this.oStreamTokenizer.parseNumbers();
        this.oStreamTokenizer.lowerCaseMode(false);

        return bRetVal;
    }

    /**
     * Creates a propabilistic token type as a part of
     * the grammar.
     */
    protected void createProbabilityToken() {
        this.oTokenType.setType(ProbabilisticGrammarTokenType.PROBABILITY);
        this.oTokenType.setSubtype(ProbabilisticGrammarTokenType.NUM);
        this.oToken = createToken(String.valueOf(oStreamTokenizer.nval), this.oTokenType);
    }

    /**
     * Creates an instance of the next token in the grammar
     * token stream given integer token type. The token type
     * is determined from the undelying stream tokenizer.
     *
     * @throws LexicalError if there was an error reading off the token
     * @see marf.nlp.Parsing.GrammarCompiler.GrammarAnalyzer#createNextToken()
     */
    protected void createNextToken()
            throws LexicalError {
        try {
            int iTokenType = this.oStreamTokenizer.nextToken();
            this.oTokenType = new GrammarTokenType();

            switch (iTokenType) {
                // Signal EOF
                case StreamTokenizer.TT_EOF: {
                    createEOFToken();
                    break;
                }

                case StreamTokenizer.TT_WORD: {
                    Debug.debug("Probabilistic Grammar Lexer - [TT_WORD]: " + oStreamTokenizer.sval);
                    createWordToken();
                    break;
                }

                case StreamTokenizer.TT_NUMBER: {
                    createProbabilityToken();
                    break;
                }

                default: {
                    Debug.debug("Probabilistic Grammar Lexer - [default]: " + (char) oStreamTokenizer.ttype);
                    createOrdinaryToken();
                    break;
                }
            } // switch

			/*
            Debug.debug
			(
				this.oTokenType.type + "," +
				this.oTokenType.subtype + "," +
				this.oToken
			);
			*/

        } // try

        catch (IOException e) {
            throw new LexicalError(e.getMessage(), e);
        }
    }

    /**
     * Creates word tokens for keywords, identifiers, non-terminals and
     * dictionary words.
     *
     * @see marf.nlp.Parsing.GrammarCompiler.GrammarAnalyzer#createWordToken()
     */
    protected void createWordToken() {
        // Keywords (including grammar ones)
        if (TokenSubType.soKeywords.containsKey(this.oStreamTokenizer.sval)) {
            this.oTokenType.setType(TokenType.KEYWORD);
            this.oTokenType.setSubtype(((Integer) TokenSubType.soKeywords.get(this.oStreamTokenizer.sval)).intValue());
            this.oToken = createToken(this.oStreamTokenizer.sval, this.oTokenType);
        } else {
            // IDs
            this.oTokenType.setType(TokenType.ID);

            // "<nonTerminal>"
            if (this.oStreamTokenizer.sval.startsWith("<")) {
                this.oTokenType.setSubtype(GrammarTokenType.GRAMMAR_ID);
            }

            // Dictionary word
            else {
                this.oTokenType.setSubtype(ProbabilisticGrammarTokenType.DICT_WORD);
            }

            this.oToken = createToken(this.oStreamTokenizer.sval, this.oTokenType);
        }
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.15 $";
    }
}

// EOF
