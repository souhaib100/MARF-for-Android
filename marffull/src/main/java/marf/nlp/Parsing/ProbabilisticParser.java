package marf.nlp.Parsing;

import java.io.StreamTokenizer;
import java.util.Vector;

import marf.Storage.StorageException;
import marf.Storage.StorageManager;
import marf.nlp.Parsing.GrammarCompiler.Grammar;
import marf.nlp.Parsing.GrammarCompiler.NonTerminal;
import marf.nlp.Parsing.GrammarCompiler.ProbabilisticGrammarCompiler;
import marf.nlp.Parsing.GrammarCompiler.ProbabilisticRule;
import marf.util.Debug;


/**
 * <p>Probabilistic parser is set of parsing a natural language
 * (e.g. English) given probabilistic grammar. Since natural language
 * sentences are ambiguous and a single sentence may have more than one
 * parse each grammar rule is assigned a probability and a parse is chosen
 * for the rule with according to the probability. This class implements
 * the well-known CYK probabilistic parsing algorithm.
 * </p>
 * <p>
 * <p>The CYK algorithm is cited below. The main reference is
 * <a href="http://www.cs.colorado.edu/~martin/SLP/New_Pages/pg455.pdf">here</a>.
 * </p>
 * <p>
 * <pre>
 * function CYK(words,grammar) returns The most probable parse
 *                             and its probability
 *
 *     create and clear pi[num_words, num_words, num_nonterminals]
 *
 *     # base case
 *     for i <-- 1 to num_words
 *         for A <-- 1 to num_nonterminals
 *             if (A --> wi) is in grammar then
 *                 pi [i, i, A] = P(A --> wi)
 *
 *     # recursive case
 *     for span <-- 2 to num_words
 *         for begin <-- 1 to num_words - span + 1
 *             end <-- begin + span - 1
 *             for m = begin to end - 1
 *
 *                 for A = 1 to num nonterminals
 *                     for B = 1 to num nonterminals
 *                         for C = 1 to num nonterminals
 *
 *                             prob = pi [begin, m, B] * pi [m + 1, end, C] * P(A --> BC)
 *
 *                             if (prob > pi[begin, end, A]) then
 *                                 pi [begin, end, A] = prob
 *                                 back[begin, end, A] = {m, B, C}
 *
 *     return build_tree(back[1, num_words, 1]), [1, num_words, 1])
 * </pre>
 * <p>
 * $Id: ProbabilisticParser.java,v 1.30 2006/01/30 03:43:17 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.30 $
 * @since 0.3.0.2
 */
public class ProbabilisticParser
        extends StorageManager {
    /**
     * Stream tokenizer to read off tokens form. Transient.
     */
    private transient StreamTokenizer oStreamTokenizer = null;

    /**
     * Probabilistic grammar filename.
     */
    private transient String strGrammarFilename = null;

    /**
     * 3-dimensional parse matrix for CYK.
     */
    private double[][][] adParseMatrix = null;

    /**
     * 3-dimensional collection of back-vectors for CYK.
     */
    private Vector[][][] aoBack = null;

    /**
     * A collection of words (dictionary).
     */
    private Vector oWords = null;

    /**
     * Instance of the grammar data structure to be used.
     */
    private Grammar oGrammar = null;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -2675525629027897961L;

    /**
     * Initializes probabilistic parser with the grammar filename.
     *
     * @param pstrGrammarFilename the filename of the probabilistic grammar
     */
    public ProbabilisticParser(String pstrGrammarFilename) {
        this();
        this.strGrammarFilename = pstrGrammarFilename;
    }

    /**
     * Initializes probabilistic parser with the specified tokenizer.
     * By default sets the tokenizer not to fold anything into the lower case.
     *
     * @param poStreamTokenizer the stream tokenizer to read the tokens off
     */
    public ProbabilisticParser(StreamTokenizer poStreamTokenizer) {
        this();
        this.oStreamTokenizer = poStreamTokenizer;
        this.oStreamTokenizer.lowerCaseMode(false);
    }

    /**
     * Initializes default probabilistic parser with empty grammar.
     */
    public ProbabilisticParser() {
        this.oGrammar = new Grammar();
        this.oObjectToSerialize = this.oGrammar;
        setFilename(this.oGrammar.getClass().getName() + "." + getDefaultExtension());
    }

    /**
     * Performs parse of a natural language sentence using the
     * CYK algorithm.
     *
     * @return <code>true</code> if the parse was successful
     * @throws SyntaxError in case of some unusual syntax brekage
     */
    public boolean parse()
            throws SyntaxError {
        try {
            if (this.oStreamTokenizer == null) {
                throw new RuntimeException("ProbabilisticParser.parse() --- there is no stream to parse!");
            }

            // Restore grammar from the disk
            restore();

            // Split the string into words

            this.oWords = new Vector();

            int iTokenType;

            while ((iTokenType = this.oStreamTokenizer.nextToken()) != StreamTokenizer.TT_EOF) {
                switch (iTokenType) {
                    case StreamTokenizer.TT_WORD: {
                        this.oWords.add(new String(this.oStreamTokenizer.sval));
                        break;
                    }

                    case StreamTokenizer.TT_NUMBER:
                    default: {
                        System.err.println("WARNING: Non-word token encountered: " + this.oStreamTokenizer);
                        break;
                    }
                }
            }

            // debug
            if (Debug.isDebugOn()) {
                System.out.println("Words found:");

                for (int i = 0; i < this.oWords.size(); i++) {
                    System.out.println("\t" + this.oWords.elementAt(i));
                }
            }

            // CYK
            Vector oNonTerminals = this.oGrammar.getNonTerminalList();

            this.adParseMatrix = new double[this.oWords.size()][this.oWords.size()][oNonTerminals.size()];
            this.aoBack = new Vector[this.oWords.size()][this.oWords.size()][oNonTerminals.size()];

            // Base case

            for (int i = 0; i < this.oWords.size(); i++) {
                String strTerminal = this.oWords.elementAt(i).toString();

				/*
                 * Fail-fast: if the terminal is not in the grammar (no rule 'A->wd' found ),
				 * there is no point to compute the parse
				 */
                if (this.oGrammar.containsTerminal(strTerminal) == -1) {
                    if (Debug.isDebugOn()) {
                        System.out.println("Terminal [ " + strTerminal + " ] not found in the grammar.");
                    }

                    return false;
                }

                for (int A = 0; A < oNonTerminals.size(); A++) {
                    ProbabilisticRule oRule = (ProbabilisticRule) this.oGrammar.getRule(strTerminal, A);

                    // Current pair: 'A->wd' does not form a Rule
                    if (oRule == null) {
                        continue;
                    }

                    this.adParseMatrix[i][i][A] = oRule.getProbability();

                    if (Debug.isDebugOn()) {
                        System.out.println("Rule (A->wd): " + oRule + ", P = " + oRule.getProbability());
                    }
                }
            }

			/*
             * Recursive case
			 * ('recursive' as authors call it, but it's implemented iteratively
			 * and me being just a copy-cat here)
			 */
            for (int iSpan = 2; iSpan <= this.oWords.size(); iSpan++) {
                for (int iBegin = 0; iBegin < this.oWords.size() - iSpan + 1; iBegin++) {
                    int iEnd = iBegin + iSpan - 1;

                    // For every split m of the incoming sentence ...
                    for (int m = iBegin; m <= iEnd - 1; m++) {
                        // Check how the split divides B and C in A->BC
                        for (int iA = 0; iA < oNonTerminals.size(); iA++) {
                            for (int iB = 0; iB < oNonTerminals.size(); iB++) {
                                for (int iC = 0; iC < oNonTerminals.size(); iC++) {
                                    ProbabilisticRule oRule = (ProbabilisticRule) this.oGrammar.getRule(iA, iB, iC);

                                    if (oRule == null) {
                                        continue;
                                    }

                                    if (Debug.isDebugOn()) {
                                        System.out.println("Rule (A->BC): " + oRule + ", P = " + oRule.getProbability());
                                    }

/*									System.out.println
                                    (
										"["+begin+"]["+m+"]["+B+"] = " + this.oParseMatrix[begin][m][B] +
										", ["+(m+1)+"]["+end+"]["+C+"] = " + this.oParseMatrix[m + 1][end][C]
									);
*/
                                    double dProb =
                                            this.adParseMatrix[iBegin][m][iB] *
                                                    this.adParseMatrix[m + 1][iEnd][iC] *
                                                    oRule.getProbability();

                                    if (dProb > this.adParseMatrix[iBegin][iEnd][iA]) {
                                        this.adParseMatrix[iBegin][iEnd][iA] = dProb;

                                        Vector oBackIndices = new Vector();

                                        oBackIndices.add(new Integer(m));
                                        oBackIndices.add(new Integer(iB));
                                        oBackIndices.add(new Integer(iC));

                                        this.aoBack[iBegin][iEnd][iA] = oBackIndices;

                                        if (Debug.isDebugOn()) {
                                            System.out.println
                                                    (
                                                            "[" + iBegin + "][" + iEnd + "][" + iA + "] = " +
                                                                    this.adParseMatrix[iBegin][iEnd][iA] +
                                                                    ", [" + m + "][" + iB + "][" + iC + "] = " +
                                                                    "prob: " + dProb
                                                    );
                                        }
                                    }
                                } // for C
                            } // for B
                        } // for A
                    } // split
                }
            } // "recursive" case

            if (Debug.isDebugOn()) {
                dumpBackPointersContents();
                dumpParseMatrix();
            }

            // No parse
            if (this.adParseMatrix[0][this.oWords.size() - 1][0] == 0) {
                return false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            throw new SyntaxError(e.getMessage(), e);
        }

        return true;
    }

    /**
     * Dumps back-pointers to the STDOUT.
     */
    public void dumpBackPointersContents() {
        for (int i = 0; i < this.oWords.size(); i++) {
            System.out.println("\n");

            for (int j = 0; j < this.oWords.size(); j++) {
                System.out.println();

                for (int k = 0; k < this.oGrammar.getNonTerminalList().size(); k++) {
                    System.out.print("\t" + (this.aoBack[i][j][k] != null));
                }
            }
        }

        System.out.println("\n");
    }

    /**
     * Dumpts parse matrix to the STDOUT.
     */
    public void dumpParseMatrix() {
        for (int i = 0; i < this.oWords.size(); i++) {
            System.out.println("\n");

            for (int j = 0; j < this.oWords.size(); j++) {
                System.out.println();

                for (int k = 0; k < this.oGrammar.getNonTerminalList().size(); k++) {
                    System.out.print("\t" + this.adParseMatrix[i][j][k]);
                }
            }
        }

        System.out.println("\n");
    }

    /**
     * Performs training of the parser by compiling the source
     * probabilistic grammar and then duming it onto disk as
     * a precompiled binary file for future re-load.
     *
     * @return <code>true</code> if the training went successful
     * @throws StorageException in case of any GrammarCompiler error
     */
    public boolean train()
            throws StorageException {
        try {
            ProbabilisticGrammarCompiler oGrammarCompiler
                    = new ProbabilisticGrammarCompiler(this.strGrammarFilename);

            oGrammarCompiler.compileGrammar();

            super.oObjectToSerialize = this.oGrammar = oGrammarCompiler.getGrammar();

            dump();

            return true;
        } catch (CompilerError e) {
            throw new StorageException(e.getMessage(), e);
        }
    }

    /**
     * Dumps parse tree to the STDOUT.
     */
    public void dumpParseTree() {
        System.out.println
                (
                        "SYNOPSIS:\n\n" +
                                "<NONTERMINAL> (PROBABILITY) [ SPAN: words of span ]\n"
                );

        dumpParseTree(0, 0, oWords.size() - 1, 0);

        System.out.println();
    }

    /**
     * Dumps a parse sub-tree to to the STDOUT
     * Initial level of S non-terminal should be 0.
     *
     * @param piLevel starting level (depth) of the tree; also acts as indentation marker
     * @param i       left index of the span
     * @param j       right index of the span
     * @param piA     the non-terminal index
     */
    public void dumpParseTree(int piLevel, int i, int j, int piA) {
        try {
            NonTerminal oLHS = (NonTerminal) this.oGrammar.getNonTerminalList().elementAt(piA);

            indent(piLevel);

            System.out.println
                    (
                            oLHS +
                                    " (" + this.adParseMatrix[i][j][piA] + ")" +
                                    " [" + getSentencePart(i, j) + " ]"
                    );

            // Termination case

            if (this.aoBack[i][j][piA] == null) {
                return;
            }

            // Recursive case

            int m = ((Integer) this.aoBack[i][j][piA].elementAt(0)).intValue();
            int iB = ((Integer) this.aoBack[i][j][piA].elementAt(1)).intValue();
            int iC = ((Integer) this.aoBack[i][j][piA].elementAt(2)).intValue();

            dumpParseTree(piLevel + 1, i, m, iB);
            dumpParseTree(piLevel + 1, m + 1, j, iC);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(e);
            e.printStackTrace(System.err);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Indents by the specified number of tabs.
     *
     * @param piTabSize the number of tab characters to indent by
     */
    protected void indent(int piTabSize) {
        for (int i = 0; i < piTabSize; i++) {
            System.out.print("\t");
        }
    }

    /**
     * Gets a sentence span given indices.
     *
     * @param i leftmost word index
     * @param j rightmost word index
     * @return the setence span string
     */
    protected String getSentencePart(int i, int j) {
        StringBuffer oSentencePart = new StringBuffer()
                .append(" ").append(i).append("-").append(j).append(":");

        for (int k = i; k <= j; k++) {
            oSentencePart.append(" ").append(this.oWords.elementAt(k));
        }

        return oSentencePart.toString();
    }

    /**
     * Allows setting desired stream tokenzer.
     *
     * @param poStreamTokenizer the NLP stream tokenizer to read off tokens from
     */
    public void setStreamTokenizer(StreamTokenizer poStreamTokenizer) {
        this.oStreamTokenizer = poStreamTokenizer;
    }

    /**
     * Implements StorageManager interface.
     *
     * @see marf.Storage.StorageManager#backSynchronizeObject()
     */
    public void backSynchronizeObject() {
        this.oGrammar = (Grammar) super.oObjectToSerialize;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.30 $";
    }
}

// EOF
