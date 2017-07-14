package marf.Classification;

import marf.util.MARFException;

/**
 * <p>Class ClassificationException indicates an error
 * during classification process.</p>
 * <p>
 * <p>$Id: ClassificationException.java,v 1.11 2005/08/11 00:44:50 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.11 $
 * @since 0.0.1
 */
public class ClassificationException
        extends MARFException {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -1088263414931478219L;

    /**
     * Generic exception.
     *
     * @param pstrMessage Error message string
     */
    public ClassificationException(String pstrMessage) {
        super(pstrMessage);
    }

    /**
     * Replicates parent's constructor.
     *
     * @param poException Exception object to encapsulate
     */
    public ClassificationException(Exception poException) {
        super(poException);
    }

    /**
     * Replicates parent's constructor.
     *
     * @param pstrMessage Error message string
     * @param poException Exception object to encapsulate
     */
    public ClassificationException(String pstrMessage, Exception poException) {
        super(pstrMessage, poException);
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.11 $";
    }
}

// EOF
