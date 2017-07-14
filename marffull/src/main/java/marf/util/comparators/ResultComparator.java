package marf.util.comparators;

import marf.Storage.Result;
import marf.util.SortComparator;


/**
 * <p>Compares two Result objects for equality.</p>
 * Used in sorting.
 * <p>
 * $Id: ResultComparator.java,v 1.14 2005/08/14 01:15:57 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.14 $
 * @since 0.3.0
 */
public class ResultComparator
        extends SortComparator {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -5312907998392826142L;

    /**
     * Takes the sorting mode into consideration.
     *
     * @param piMethod sorting method, either ASCENDING or DESCENDING
     */
    public ResultComparator(int piMethod) {
        super(piMethod);
    }

    /**
     * Implementation of the Comparator interface for the Result objects.
     * To decide on inequality of the Results objects we compare
     * their outcome (distance or probability) value only. Takes into
     * consideration ASCENDING or DESCENDING order of sorting.
     *
     * @param poResult1 first Result object for comparison
     * @param poResult2 second Result object for comparison
     * @return 0 if the objects are equal, 1 if the 2nd is greater then the 1st
     * under ASCENDING, or the 1st is greater then the 2nd under DESCENDING.
     * Otherwise, -2 is returned.
     */
    public int compare(Object poResult1, Object poResult2) {
        Result oResult1 = (Result) poResult1;
        Result oResult2 = (Result) poResult2;

        switch (this.iSortMode) {
            case DESCENDING: {
                if (oResult1.getOutcome() < oResult2.getOutcome())
                    return 1;

                if (oResult1.getOutcome() > oResult2.getOutcome())
                    return -1;

                return 0;
            }

            case ASCENDING:
            default: {
                if (oResult1.getOutcome() < oResult2.getOutcome())
                    return -1;

                if (oResult1.getOutcome() > oResult2.getOutcome())
                    return 1;

                return 0;
            }
        }
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.14 $";
    }
}

// EOF
