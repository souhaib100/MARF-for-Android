package marf.FeatureExtraction;

import marf.util.MARFException;

/**
 * <p>Class FeatureExtractionException.</p>
 * <p>
 * <p>$Id: FeatureExtractionException.java,v 1.8 2005/08/13 23:09:37 susan_fan Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.8 $
 * @since 0.0.1
 */
public class FeatureExtractionException
        extends MARFException {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -2325585432639761782L;

    /**
     * Encapsulation of another Exception object.
     *
     * @param poException Exception to wrap around
     * @since 0.3.0
     */
    public FeatureExtractionException(Exception poException) {
        super(poException);
    }

    /**
     * Encapsulation of another Exception object and a new message.
     *
     * @param pstrMessage additional information to add
     * @param poException Exception to wrap around
     * @since 0.3.0
     */
    public FeatureExtractionException(String pstrMessage, Exception poException) {
        super(pstrMessage, poException);
    }

    /**
     * Generic exception.
     *
     * @param pstrMessage Error message string
     */
    public FeatureExtractionException(String pstrMessage) {
        super(pstrMessage);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.8 $";
    }
}

// EOF
