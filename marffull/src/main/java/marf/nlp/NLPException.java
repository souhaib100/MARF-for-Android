package marf.nlp;

import marf.util.MARFException;


/**
 * <p>Class StorageException.</p>
 * <p>
 * <p>$Id: NLPException.java,v 1.4 2006/01/06 22:20:13 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.4 $
 * @since 0.3.0.4
 */
public class NLPException
        extends MARFException {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -6738475142356489003L;

    /**
     * Default NLP exception.
     */
    public NLPException() {
        super();
    }

    /**
     * Generic exception.
     *
     * @param pstrMessage Error message string
     */
    public NLPException(String pstrMessage) {
        super(pstrMessage);
    }

    /**
     * Exception wrapper constructor.
     *
     * @param poException Exception object to wrap
     */
    public NLPException(Exception poException) {
        super(poException);
    }

    /**
     * Accepts custom message and the exception object.
     *
     * @param pstrMessage custom error message
     * @param poException exception happened
     */
    public NLPException(String pstrMessage, Exception poException) {
        super(pstrMessage, poException);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.4 $";
    }
}

// EOF
