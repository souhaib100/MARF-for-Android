package marf.util.comparators;

import marf.util.NotImplementedException;
import marf.util.SortComparator;

/**
 * <p>Rank Comparator.</p>
 * <p>
 * <p>TODO: Implement and document.</p>
 * <p>
 * $Id: RankComparator.java,v 1.13 2005/08/13 23:09:39 susan_fan Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.13 $
 * @since 0.3.0
 */
public class RankComparator
        extends SortComparator {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -4257414077743470961L;

    /**
     * Not implemented.
     *
     * @param poObject1 unused
     * @param poObject2 unused
     * @return nothing
     * @throws NotImplementedException
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object poObject1, Object poObject2) {
        throw new NotImplementedException(getClass().getName());
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.13 $";
    }
}

// EOF
