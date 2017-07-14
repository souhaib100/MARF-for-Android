package marf.Storage;

import java.io.Serializable;
import java.util.Vector;

import marf.util.Arrays;
import marf.util.Debug;
import marf.util.SortComparator;
import marf.util.comparators.ResultComparator;


/**
 * <p>Class ResultSet represents classification result
 * set - IDs and some stats.</p>
 * <p>
 * <p>$Id: ResultSet.java,v 1.20 2006/01/02 22:24:00 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.20 $
 * @since 0.3.0.2
 */
public class ResultSet
        implements Serializable, Cloneable {
    /**
     * Indicates that this result set is unsorted.
     *
     * @since 0.3.0.4
     */
    public static final int UNSORTED = -1;

    /**
     * Distances from other samples and other stats or
     * probabilities and likelyhood (growable and shrinkable).
     */
    protected Vector oResultSet = null;

    /**
     * References to the objects in the oResultSet
     * in the sorted order.
     */
    protected Result[] aoResultSetSorted = null;

    /**
     * Used to remember in which direction sorting was performed,
     * so some methods can work correctly. Default is UNSORTED.
     * Otherwise, it is <code>SortComparator.ASCENDING</code>
     * or <code>SortComparator.DESCENDING</code>. It is set by
     * the <code>sort()</code> method only.
     *
     * @see #getSecondClosestID()
     * @see #sort(int)
     * @see SortComparator#ASCENDING
     * @see SortComparator#DESCENDING
     * @see #UNSORTED
     * @since 0.3.0.3
     */
    protected int iSortMode = UNSORTED;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -3133714664001380852L;

    /**
     * Default constructor, which is
     * equivalent to <code>ResultSet(new Vector())</code>.
     */
    public ResultSet() {
        this(new Vector());
    }

    /**
     * ID Vector ResultSet Constructor.
     *
     * @param poResultSet Vector of relevant data
     * @throws IllegalArgumentException if the parameter is null
     */
    public ResultSet(Vector poResultSet) {
        if (poResultSet == null) {
            throw new IllegalArgumentException("Result set Vector cannot be null.");
        }

        this.oResultSet = poResultSet;
    }

    /**
     * Copy-constructor.
     *
     * @param poResultSet ResultSet object reference to make a copy of
     * @since 0.3.0.5
     */
    public ResultSet(final ResultSet poResultSet) {
        this.oResultSet = (Vector) poResultSet.getResultSetVector().clone();
        this.iSortMode = poResultSet.getSortMode();
        this.aoResultSetSorted = (Result[]) poResultSet.getResultSetSorted().clone();
    }

    /**
     * Retrieves ID of a subject with minimum distance/probability.
     *
     * @return integer ID
     * @throws ArrayIndexOutOfBoundsException if there are no results
     */
    public final int getMininumID() {
        sort(SortComparator.ASCENDING);
        return this.aoResultSetSorted[0].getID();
    }

    /**
     * Retrieves ID of a subject with the distance/probability next
     * to the minimum.
     *
     * @return integer ID
     * @throws ArrayIndexOutOfBoundsException if there is only one
     *                                        or no result in the result set.
     */
    public final int getSecondMininumID() {
        sort(SortComparator.ASCENDING);
        return this.aoResultSetSorted[1].getID();
    }

    /**
     * Retrieves ID of a subject with maximum distance/probability.
     *
     * @return integer ID
     * @throws ArrayIndexOutOfBoundsException if there are no results
     */
    public final int getMaximumID() {
        sort(SortComparator.DESCENDING);
        return this.aoResultSetSorted[0].getID();
    }

    /**
     * Retrieves ID of a subject with the distance/probability next
     * to the maximum.
     *
     * @return integer ID
     * @throws ArrayIndexOutOfBoundsException if there is only one
     *                                        or no result in the result set.
     */
    public final int getSecondMaximumID() {
        sort(SortComparator.DESCENDING);
        return this.aoResultSetSorted[1].getID();
    }

    /**
     * Returns second closest ID.
     *
     * @return ID of an entity (speaker, instrument, emotion, etc.); or -1
     * if the first ID was not retrieved yet.
     * @see #getSecondMininumID()
     * @see #getSecondMaximumID()
     */
    public final int getSecondClosestID() {
        switch (this.iSortMode) {
            case SortComparator.ASCENDING: {
                return getSecondMininumID();
            }

            case SortComparator.DESCENDING: {
                return getSecondMaximumID();
            }

            default: {
                Debug.debug(getClass(), "Call to getSecondClosestID() when not sorted.");
            }
        }

        return -1;
    }

    /**
     * Retrieves ID of a subject with average distance/probability.
     *
     * @return integer ID
     * @throws ArrayIndexOutOfBoundsException if there are no results
     */
    public final int getAverageID() {
        sort(SortComparator.ASCENDING);
        return this.aoResultSetSorted[size() / 2].getID();
    }

    /**
     * Retrieves a pseudo random ID of a subject.
     * Used in the base-line testing.
     *
     * @return integer ID
     * @throws ArrayIndexOutOfBoundsException if there are no results
     */
    public final int getRandomID() {
        return ((Result) this.oResultSet.elementAt((int) (Math.random() * size()))).getID();
    }

    /**
     * Perform sorting of the results in the result set.
     *
     * @param piMode sorting mode, either <code>SortComparator.ASCENDING</code> or
     *               <code>SortComparator.DESCENDING</code>.
     * @throws IllegalArgumentException if the parameter is neither of the two modes
     * @see SortComparator#ASCENDING
     * @see SortComparator#DESCENDING
     */
    public final void sort(final int piMode) {
        if (this.iSortMode != piMode) {
            this.iSortMode = piMode;
            this.aoResultSetSorted = (Result[]) oResultSet.toArray(new Result[0]);
            Arrays.sort(this.aoResultSetSorted, new ResultComparator(this.iSortMode));
        }
    }

    /**
     * Add result to the result set.
     *
     * @param piID            subject ID recognized
     * @param pdOutcome       outcome of the recongnition distance or likelyhood
     * @param pstrDescription textual description of the result
     */
    public final void addResult(int piID, double pdOutcome, String pstrDescription) {
        this.oResultSet.add(new Result(piID, pdOutcome, pstrDescription));

        // Invalidate the sorted flag.
        this.iSortMode = UNSORTED;
    }

    /**
     * Add result to the result set. Generates description based
     * on the two parameters.
     *
     * @param piID      subject ID recognized
     * @param pdOutcome outcome of the recongnition distance or likelyhood
     */
    public final void addResult(int piID, double pdOutcome) {
        addResult(piID, pdOutcome, "ID=" + piID + ", outcome=" + pdOutcome);
    }

    /**
     * Add result to the result set based on already pre-constructed
     * object.
     *
     * @param poResult Result object prepared ouside
     * @throws IllegalArgumentException of the parameter is null
     */
    public final void addResult(Result poResult) {
        if (poResult == null) {
            throw new IllegalArgumentException("Result parameter is null.");
        }

        this.oResultSet.add(poResult);
    }

    /**
     * Retrieves the result from the result set with the
     * minimum outcome value.
     *
     * @return corresponding Result object
     */
    public Result getMinimumResult() {
        getMininumID();
        return this.aoResultSetSorted[0];
    }

    /**
     * Retrieves the result from the result set with the
     * average outcome value.
     *
     * @return corresponding Result object
     */
    public Result getAverageResult() {
        getMininumID();
        return this.aoResultSetSorted[this.oResultSet.size() / 2];
    }

    /**
     * Retrieves pseudo-random result object from the result set.
     * Used in base-line testing.
     *
     * @return corresponding Result object
     */
    public Result getRandomResult() {
        return (Result) this.oResultSet.elementAt((int) (Math.random() * size()));
    }

    /**
     * Retrieves the result from the result set with the
     * maximum outcome value.
     *
     * @return corresponding Result object
     */
    public Result getMaximumResult() {
        sort(SortComparator.DESCENDING);

        // Debug
        if (Debug.isDebugOn()) {
            for (int i = 0; i < aoResultSetSorted.length; i++) {
                Debug.debug("after.oResultSet=" + aoResultSetSorted[i]);
            }
        }

        return this.aoResultSetSorted[0];
    }

    /**
     * Retrieves the underlying unsorted result collection.
     *
     * @return Vector of Results
     */
    public Vector getResultSetVector() {
        /*
         * Be pessimistic about what can be done with the
		 * retured result set vector reference outside of this.
		 * Thus, don't trust the client code anymore to keep
		 * the stuff unmodified (i.e. adding/deleting new records)
		 * and force a resorting internally in the sort() method.
		 */
        this.iSortMode = UNSORTED;

        return this.oResultSet;
    }

    /**
     * Allows querying the curren array of references to
     * the contained Result object sorted (if sorting was performed).
     * The sorting is usually done when querying for minimum or
     * maximum results.
     *
     * @return Returns the aoResultSetSorted.
     * @since 0.3.0.5
     */
    public final Result[] getResultSetSorted() {
        return this.aoResultSetSorted;
    }

    /**
     * Allows querying the sort mode the result set
     * may be currently sorted in accordance with.
     *
     * @return the curren sort mode
     * @see #UNSORTED
     * @see SortComparator#ASCENDING
     * @see SortComparator#DESCENDING
     * @see #iSortMode
     * @since 0.3.0.5
     */
    public final int getSortMode() {
        return this.iSortMode;
    }

    /**
     * Returns inner ResultSet data converted to string.
     *
     * @return String representation of a ResultSet object
     */
    public String toString() {
        StringBuffer oSortedBuffer = new StringBuffer();

        if (this.aoResultSetSorted == null) {
            oSortedBuffer.append("N/A");
        } else {
            oSortedBuffer.append("(");

            for (int i = 0; i < this.aoResultSetSorted.length; i++) {
                if (i != 0) {
                    oSortedBuffer.append(",");
                }

                oSortedBuffer.append(this.aoResultSetSorted[i]);
            }

            oSortedBuffer.append(")");
        }

        StringBuffer oBuffer = new StringBuffer();

        oBuffer
                .append("Raw ResultSet data:\n")
                .append(this.oResultSet).append("\n\n")
                .append("Sorted data:\n").append(oSortedBuffer);

        return oBuffer.toString();
    }

    /**
     * Retrieves result count of the results in this result set.
     *
     * @return integer result count
     */
    public int size() {
        return this.oResultSet.size();
    }

    /**
     * Implements Cloneable interface for the ResultSet object.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        return new ResultSet(this);
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
