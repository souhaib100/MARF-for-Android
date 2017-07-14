package marf.util;

import java.io.Serializable;
import java.util.Comparator;


/**
 * <p>Sort Comparator is the base for all sorting operations in MARF.</p>
 * <p>
 * $Id: SortComparator.java,v 1.9 2005/12/24 19:47:39 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.9 $
 * @since 0.3.0.2
 */
public abstract class SortComparator
        implements Comparator, Serializable {
    /*
     * Constants to use for idication of the sorting order
	 */

    /**
     * Constant indicating to sort in the ascending order.
     */
    public static final int ASCENDING = 0;

    /**
     * Constant indicating to sort in the descending order.
     */
    public static final int DESCENDING = 1;

    /**
     * Current sorting mode: either <code>ASCENDING</code>
     * or <code>DESCENDING</code>.
     */
    protected int iSortMode = ASCENDING;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.5
     */
    private static final long serialVersionUID = -3395493010875051653L;

    /**
     * When constsructed, default mode is ASCENDING.
     */
    public SortComparator() {
        this(ASCENDING);
    }

    /**
     * When constsructed, the mode is the one specified.
     *
     * @param piSortMode desired sorting mode of ASCENDING or DESCENDING.
     * @throws IllegalArgumentException is not ASCENDING or DESCENDING
     */
    public SortComparator(final int piSortMode) {
        if (piSortMode < ASCENDING || piSortMode > DESCENDING) {
            throw new IllegalArgumentException
                    (
                            "Sort mode (" + piSortMode + ") is out of range.\n" +
                                    "HINT: use SortComparator.ASCENDING or SortComparator.DESCENDING"
                    );
        }

        this.iSortMode = piSortMode;
    }

    /**
     * Retrieves the current sorting mode.
     *
     * @return the mode
     * @see #DESCENDING
     * @see #ASCENDING
     * @since 0.3.0.3
     */
    public int getSortMode() {
        return this.iSortMode;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.9 $";
    }
}

// EOF
