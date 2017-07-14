package marf.Stats;

/**
 * <p>A pure container for statistics of a word --- no business logic.</p>
 * <p>
 * $Id: WordStats.java,v 1.20 2006/01/17 22:41:14 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.20 $
 * @since 0.3.0.2
 */
public class WordStats
        extends StatisticalObject {
    /**
     * Spelling of the word.
     */
    private String strLexeme = "";

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 1553592340644601516L;

    /**
     * Constructs the default object with an empty lexeme.
     */
    public WordStats() {
        super();
    }

    /**
     * Constructs object with the desired frequency and the lexeme.
     *
     * @param piFrequency the frequency
     * @param pstrLexeme  the lexeme
     */
    public WordStats(int piFrequency, String pstrLexeme) {
        super(piFrequency);
        this.strLexeme = pstrLexeme;
    }

    /**
     * Constructs object with the desired frequency.
     *
     * @param piFrequency the frequency
     */
    public WordStats(int piFrequency) {
        super(piFrequency);
    }

    /**
     * Copy-constructor.
     *
     * @param poWordStats WordStats object to copy properties from
     * @since 0.3.0.5
     */
    public WordStats(final WordStats poWordStats) {
        super(poWordStats);
        this.strLexeme = new String(poWordStats.strLexeme);
    }

    /**
     * Retrieves the lexeme of the object.
     *
     * @return the current lexeme
     */
    public final String getLexeme() {
        return this.strLexeme;
    }

    /**
     * Reports lexeme, frequency, and rank of an occurence of a word.
     *
     * @see java.lang.Object#toString()
     * @since 0.3.0.5
     */
    public String toString() {
        return new StringBuffer()
                .append("Lexeme: ").append(this.strLexeme).append(", ")
                .append(super.toString())
                .toString();
    }

    /**
     * Implements Cloneable interface for the WordStats object.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        return new WordStats(this);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.20 $";
    }
}

// EOF
