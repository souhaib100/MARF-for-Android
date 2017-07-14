package marf.Stats;


/**
 * <p>N-gram Observation.</p>
 * <p>
 * $Id: Ngram.java,v 1.15 2006/01/02 22:24:00 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.15 $
 * @since 0.3.0.2
 */
public class Ngram
        extends Observation {
    /**
     * 0 - unset (default), 1 - unigram, 2 - bigram, 3 - trigram, etc.
     */
    private int iSize = 0;

    /**
     * N-gram's current capacity.
     * Default is 0.
     */
    private int iCapacity = 0;

    /**
     * N-gram's parts.
     */
    private Ngram[] aoNgramElements = null;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -7377051981115115072L;

    /**
     * Constructs an unigram.
     */
    public Ngram() {
        this(1);
    }

    /**
     * Constructs an N-gram of the specified size.
     * The first element of the N-gram is this object itself.
     *
     * @param piSize N
     */
    public Ngram(final int piSize) {
        this.iSize = piSize;
        this.aoNgramElements = new Ngram[piSize];
        this.aoNgramElements[0] = this;
        this.iCapacity = 1;
        this.bSeen = true;
    }

    /**
     * Constructs an N-gram out of the N-gram elements.
     * Makes a deep copy of the parameter array.
     *
     * @param paoNgramElements the N-gram array to make a copy from
     */
    public Ngram(final Ngram[] paoNgramElements) {
        this.iCapacity = this.iSize = paoNgramElements.length;
        this.aoNgramElements = (Ngram[]) paoNgramElements.clone();
        this.bSeen = true;
    }

    /**
     * Copy-constructor.
     *
     * @param poNgram Ngram object to copy properties of
     * @since 0.3.0.5
     */
    public Ngram(final Ngram poNgram) {
        super(poNgram);
        this.iCapacity = poNgram.getCapacity();
        this.iSize = poNgram.getSize();
        this.aoNgramElements = (Ngram[]) poNgram.getNgramElements().clone();
    }

    /**
     * Sets an N-gram at a specified index.
     *
     * @param piIndex the index
     * @param poNgram the n-gram
     * @throws ArrayIndexOutOfBoundsException if the index is outside the valid range
     */
    public void setNgram(int piIndex, Ngram poNgram) {
        this.aoNgramElements[piIndex] = poNgram;
    }

    /**
     * Adds an N-gram at the end of the N-gram elements array.
     *
     * @param poNgram the N-gram to add.
     * @throws ArrayIndexOutOfBoundsException the N-gram being added exceeds the initial capacity
     */
    public void addNgram(Ngram poNgram) {
        this.aoNgramElements[this.iCapacity++] = poNgram;
    }

    /**
     * Retrievs the size of the N-gram.
     *
     * @return N
     */
    public final int getSize() {
        return this.iSize;
    }

    /**
     * Allows querying for the contained N-gram elements.
     *
     * @return the collection of the elements
     * @since 0.3.0.5
     */
    public final Ngram[] getNgramElements() {
        return this.aoNgramElements;
    }

    /**
     * Allows querying for the current capacity of the
     * N-gram (how many elements are actually there).
     * Should be less than or equal to the length of
     * the elements array.
     *
     * @return current capacity of the n-gram
     * @since 0.3.0.5
     */
    public final int getCapacity() {
        assert this.iCapacity <= this.aoNgramElements.length;
        return this.iCapacity;
    }

    /**
     * Implements Cloneable interface for the Ngram object.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        return new Ngram(this);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.15 $";
    }
}

// EOF
