package marf.Storage;

import marf.util.MARFException;

/**
 * <p>Class StorageException indicates
 * a serialization/storage-related error.</p>
 * <p>
 * <p>$Id: StorageException.java,v 1.12 2005/12/22 00:37:36 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.12 $
 * @since 0.3.0.1
 */
public class StorageException
        extends MARFException {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 8148028971974635121L;


    /**
     * Generic exception.
     *
     * @param pstrMessage Error message string
     */
    public StorageException(String pstrMessage) {
        super(pstrMessage);
    }

    /**
     * Exception wrapper constructor.
     *
     * @param poException Exception object to wrap
     */
    public StorageException(Exception poException) {
        super(poException);
    }

    /**
     * Default parameterless storage error.
     *
     * @since 0.3.0.5
     */
    public StorageException() {
        super();
    }

    /**
     * An storage exception with a message and wrapped exception object.
     *
     * @param pstrMessage the desired custom message
     * @param poException the wrapped exception object
     * @since 0.3.0.5
     */
    public StorageException(String pstrMessage, Exception poException) {
        super(pstrMessage, poException);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.12 $";
    }
}

// EOF
