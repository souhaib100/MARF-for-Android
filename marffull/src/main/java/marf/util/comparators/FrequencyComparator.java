package marf.util.comparators;

import marf.Stats.WordStats;
import marf.util.SortComparator;


/**
 * <p>Compare WordStats objects by their frequency when sorting.</p>
 * <p>
 * $Id: FrequencyComparator.java,v 1.14 2005/08/14 01:15:57 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.14 $
 * @see WordStats
 * @since 0.3.0
 */
public class FrequencyComparator
        extends SortComparator {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 5923172975252427681L;

    /**
     * Constructs a frequency comparator with the specified sort mode.
     *
     * @param piSortMode ASCENDING or DESCENDING
     */
    public FrequencyComparator(int piSortMode) {
        super(piSortMode);
    }

    /**
     * Implementation of the Comparator interface for the WordStats objects.
     * To decide on inequality of the <code>WordStats</code> objects we
     * compare their frequencies only.
     *
     * @param poWordStats1 first WordStats object to compare
     * @param poWordStats2 second WordStats object to compare
     * @return 0 of the frequencies are equal. Depending on the sort mode; a negative
     * value may mean poWordStats1 &lt; poWordStats2 if ASCENDING; or otherwise if DESCENDING
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     * @see WordStats
     */
    public int compare(Object poWordStats1, Object poWordStats2) {
        WordStats oWordStats1 = (WordStats) poWordStats1;
        WordStats oWordStats2 = (WordStats) poWordStats2;

        switch (this.iSortMode) {
            case DESCENDING:
                return (oWordStats2.getFrequency() - oWordStats1.getFrequency());

            case ASCENDING:
            default:
                return (oWordStats1.getFrequency() - oWordStats2.getFrequency());
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
