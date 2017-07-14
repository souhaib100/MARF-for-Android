package marf.Stats;

import java.io.Serializable;


/**
 * <p>Generic Statistical Object.</p>
 * <p>
 * $Id: StatisticalObject.java,v 1.15 2006/01/17 22:41:14 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.15 $
 * @since 0.3.0.2
 */
public class StatisticalObject
        implements Serializable, Cloneable {
    /**
     * Word's frequency in a given corpus. Default <code>0</code>.
     */
    protected int iFrequency = 0;

    /**
     * Word's rank in the corpus. The rank of 1 indicates the top frequent word.
     * Default is <code>-1</code>, i.e. unset.
     */
    protected int iRank = -1;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 3277836605237662721L;

    /**
     * Constructs a default statistical object.
     */
    public StatisticalObject() {
    }

    /**
     * Constructs a statistical object with a frequency parameter.
     *
     * @param piFrequency the desired frequency of this object
     */
    public StatisticalObject(final int piFrequency) {
        this.iFrequency = piFrequency;
    }

    /**
     * Copy-constructor.
     *
     * @param poStatisticalObject statistical object to copy properties of
     * @since 0.3.0.5
     */
    public StatisticalObject(final StatisticalObject poStatisticalObject) {
        this.iFrequency = poStatisticalObject.iFrequency;
        this.iRank = poStatisticalObject.iRank;
    }

    /**
     * Allows setting rank of this object.
     *
     * @param piRank the new rank
     */
    public final void setRank(final int piRank) {
        this.iRank = piRank;
    }

    /**
     * Increments rank of the object by one.
     *
     * @return the new rank
     */
    public final int incRank() {
        return (++this.iRank);
    }

    /**
     * Restrieves the object's rank.
     *
     * @return current rank
     */
    public final int getRank() {
        return this.iRank;
    }

    /**
     * Retrieves the object's frequency.
     *
     * @return current frequency
     */
    public final int getFrequency() {
        return this.iFrequency;
    }

    /**
     * Increments the object frequency by one.
     *
     * @return the new frequency
     */
    public final int incFrequency() {
        return (++this.iFrequency);
    }

    /**
     * Implements Cloneable interface for the StatisticalObject.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        return new StatisticalObject(this);
    }

    /**
     * Checks equality of two StatisticalObjects whether
     * the parameter is not null and its <code>toString()</code>'s output
     * is equal to this one.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @since 0.3.0.5
     */
    public synchronized boolean equals(Object poStatisticalObject) {
        if (poStatisticalObject instanceof StatisticalObject) {
            return poStatisticalObject.toString().equals(toString());
        }

        return false;
    }

    /**
     * Overrides <code>hashCode()</code> since <code>equals()</code> is
     * overridden by returning the hash code of the <code>toString()</code>.
     *
     * @see java.lang.Object#hashCode()
     * @see #equals(Object)
     * @since 0.3.0.5
     */
    public synchronized int hashCode() {
        return toString().hashCode();
    }

    /**
     * Reports frequency and rank of an occurence of a statistical object.
     *
     * @see java.lang.Object#toString()
     * @since 0.3.0.5
     */
    public String toString() {
        return new StringBuffer()
                .append("Frequency: ").append(this.iFrequency).append(", ")
                .append("Rank: ").append(this.iRank)
                .toString();
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
