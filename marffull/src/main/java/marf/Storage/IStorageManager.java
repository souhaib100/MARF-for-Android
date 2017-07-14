package marf.Storage;

import java.io.Serializable;


/**
 * <p>Almost every concrete module must implement this interface
 * if it cannot extend from the StorageManager class. The interface
 * extends <code>Serializable</code>. Additionally, since 0.3.0.5
 * it also extends <code>Cloneable</code>.
 * </p>
 * <p>
 * $Id: IStorageManager.java,v 1.11 2005/12/27 23:55:30 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.11 $
 * @see Serializable
 * @see Cloneable
 * @since 0.3.0.2
 */
public interface IStorageManager
        extends Serializable, Cloneable {
    /**
     * Indicates to dump/restore data as gzipped binary file.
     */
    int DUMP_GZIP_BINARY = 0;

    /**
     * Indicates to dump/restore data as CSV text file.
     */
    int DUMP_CSV_TEXT = 1;

    /**
     * Indicates to dump/restore set data as uncompressed binary file.
     */
    int DUMP_BINARY = 2;

    /**
     * Indicates to dump/restore data as XML file.
     */
    int DUMP_XML = 3;

    /**
     * Indicates to dump/restore data as an HTML file.
     */
    int DUMP_HTML = 4;

    /**
     * Indicates to dump/restore data as a set of SQL commands.
     */
    int DUMP_SQL = 5;

    /**
     * Interface source code revision.
     */
    String MARF_INTERFACE_CODE_REVISION = "$Revision: 1.11 $";

    /**
     * Maps <code>DUMP_</code> constants to default filename extensions.
     * The order must follow the order of the constants as they are
     * used as indices for this array.
     */
    String[] STORAGE_FILE_EXTENSIONS = new String[]
            {
                    "gzbin",
                    "csv",
                    "bin",
                    "xml",
                    "html",
                    "sql"
            };

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.5
     */
    long serialVersionUID = -7137065556183693005L;

    /**
     * An object must know how dump itself to a file.
     * Options are: object serialization (possibly compressed), XML, CSV, HTML, and SQL.
     *
     * @throws StorageException if there was a problem saving the object
     */
    void dump()
            throws StorageException;

    /**
     * An object must know how retore its non-transient data structures from a file.
     * Options are: object serialization (possibly compressed), XML, CSV, HTML, and SQL.
     *
     * @throws StorageException if there was a problem (re)loading the object
     */
    void restore()
            throws StorageException;

    /**
     * Implement to save data structures as compressed binary.
     *
     * @throws StorageException in case of I/O or otherwise error
     */
    void dumpGzipBinary()
            throws StorageException;

    /**
     * Implement to save data structures as binary.
     *
     * @throws StorageException in case of I/O or otherwise error
     */
    void dumpBinary()
            throws StorageException;

    /**
     * Implement to save data structures in CSV format.
     *
     * @throws StorageException in case of I/O or otherwise error
     */
    void dumpCSV()
            throws StorageException;

    /**
     * Implement to save data structures as XML document.
     *
     * @throws StorageException in case of I/O or otherwise error
     */
    void dumpXML()
            throws StorageException;

    /**
     * Implement to save data structures as HTML document.
     *
     * @throws StorageException in case of I/O or otherwise error
     */
    void dumpHTML()
            throws StorageException;

    /**
     * Implement to save data structures as SQL script.
     *
     * @throws StorageException in case of I/O or otherwise error
     */
    void dumpSQL()
            throws StorageException;

    /**
     * Implement to load data structures in binary form.
     *
     * @throws StorageException in case of I/O or otherwise error
     */
    void restoreBinary()
            throws StorageException;

    /**
     * Implement to load data structures in compressed binary form.
     *
     * @throws StorageException in case of I/O or otherwise error
     */
    void restoreGzipBinary()
            throws StorageException;

    /**
     * Implement to load data structures in CSV format.
     *
     * @throws StorageException in case of I/O or otherwise error
     */
    void restoreCSV()
            throws StorageException;

    /**
     * Implement to load data structures from an XML document.
     *
     * @throws StorageException in case of I/O or otherwise error
     */
    void restoreXML()
            throws StorageException;

    /**
     * Implement to load data structures from an HTML document.
     *
     * @throws StorageException in case of I/O or otherwise error
     */
    void restoreHTML()
            throws StorageException;

    /**
     * Implement to load data structures from an SQL script.
     *
     * @throws StorageException in case of I/O or otherwise error
     */
    void restoreSQL()
            throws StorageException;
}

// EOF
