package marf.Storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import marf.util.NotImplementedException;


/**
 * <p>Class StorageManager.</p>
 * <p>Almost every concrete module must inherit from this class.
 * If that's not possible, implement IStorageManager interface.</p>
 * <p>
 * <p>$Id: StorageManager.java,v 1.26 2006/01/02 22:24:00 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.26 $
 * @since 0.0.1
 */
public abstract class StorageManager
        implements IStorageManager {
    /**
     * Indicates in which format dump training data.
     * <p>Can either be one of the <code>DUMP_</code> flags,
     * with the <code>DUMP_GZIP_BINARY</code> being the default.</p>
     *
     * @since 0.2.0
     */
    protected transient int iCurrentDumpMode = DUMP_GZIP_BINARY;

    /**
     * Filename of the file to be dumped/restored.
     *
     * @since 0.3.0
     */
    protected transient String strFilename = null;

    /**
     * Actual object to be serialized (primarily for GZIP and BINARY modes.
     * Has to be back-synchronized.
     *
     * @see #backSynchronizeObject()
     * @since 0.3.0
     */
    protected Object oObjectToSerialize = null;

    /**
     * If set to <code>true</code> (the default), causes
     * <code>restoreBinary()</code> or <code>restoreGzipBinary()</code>
     * to create a file if it does not exist. If set to <code>false<code>,
     * an exception is thrown.
     *
     * @see #restoreBinary()
     * @see #restoreGzipBinary()
     * @since 0.3.0
     */
    protected transient boolean bDumpOnNotFound = true;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.5
     */
    private static final long serialVersionUID = -7137065556183693005L;

    /**
     * Default constructor equivalent to <code>StorageManager(null, getClass().getName())</code>.
     * Sets internal filename to the class name of the derivative.
     *
     * @see #StorageManager(Object, String)
     * @see #strFilename
     * @since 0.3.0
     */
    public StorageManager() {
        /*
         * We cannot call getClass().getName() in
		 * this() because 'this' has to exist first.
		 */
        this(null, "");
        setFilename(getClass().getName());
    }

    /**
     * Constructor with filename parameter equivalent to
     * <code>StorageManager(null, pstrFilename)</code>.
     *
     * @param pstrFilename customized filename
     * @see #StorageManager(Object, String)
     * @see #strFilename
     * @since 0.3.0
     */
    public StorageManager(String pstrFilename) {
        this(null, pstrFilename);
    }

    /**
     * General constructor with serializable object parameter.
     * Sets internal filename to the class name of the parameter.
     *
     * @param poObjectToSerialize reference to object to be dumped to a file
     * @see #oObjectToSerialize
     * @see #bDumpOnNotFound
     * @since 0.3.0
     */
    public StorageManager(Object poObjectToSerialize) {
        this(poObjectToSerialize, poObjectToSerialize.getClass().getName());
    }

    /**
     * General constructor with filename and serializable object parameters.
     *
     * @param poObjectToSerialize reference to object to be dumped to a file
     * @param pstrFilename        customized filename
     * @see #oObjectToSerialize
     * @see #strFilename
     * @see #bDumpOnNotFound
     * @since 0.3.0
     */
    public StorageManager(Object poObjectToSerialize, String pstrFilename) {
        this.strFilename = pstrFilename;
        this.oObjectToSerialize = poObjectToSerialize;
        //Debug.debug(StorageManager.class, "constructed: " + pstrFilename + ":" + this.oObjectToSerialize);
    }

    /**
     * Constructor with filename parameter equivalent to
     * <code>StorageManager(null, pstrFilename)</code>.
     *
     * @param pstrFilename     customized filename
     * @param pbDumpOnNotFound if <code>true</code>, a dump file will be created if it does not exist;
     *                         if <code>false</code>, an exception will be thrown
     * @see #StorageManager(Object, String)
     * @see #strFilename
     * @since 0.3.0
     */
    public StorageManager(String pstrFilename, boolean pbDumpOnNotFound) {
        this(null, pstrFilename, pbDumpOnNotFound);
    }

    /**
     * General constructor with serializable object parameter.
     * Sets internal filename to the class name of the parameter.
     *
     * @param poObjectToSerialize reference to object to be dumped to a file
     * @param pbDumpOnNotFound    if <code>true</code>, a dump file will be created if it does not exist;
     *                            if <code>false</code>, an exception will be thrown
     * @see #oObjectToSerialize
     * @see #bDumpOnNotFound
     * @since 0.3.0
     */
    public StorageManager(Object poObjectToSerialize, boolean pbDumpOnNotFound) {
        this(poObjectToSerialize, poObjectToSerialize.getClass().getName(), pbDumpOnNotFound);
    }

    /**
     * General constructor with filename and serializable object parameters.
     *
     * @param poObjectToSerialize reference to object to be dumped to a file
     * @param pstrFilename        customized filename
     * @param pbDumpOnNotFound    if <code>true</code>, a dump file will be created if it does not exist;
     *                            if <code>false</code>, an exception will be thrown
     * @see #oObjectToSerialize
     * @see #strFilename
     * @since 0.3.0
     */
    public StorageManager(Object poObjectToSerialize, String pstrFilename, boolean pbDumpOnNotFound) {
        this.strFilename = pstrFilename;
        this.oObjectToSerialize = poObjectToSerialize;
        this.bDumpOnNotFound = pbDumpOnNotFound;
    }

    /**
     * An object must know how dump itself or its data structures to a file.
     * Options are: Object serialization, XML, CSV, HTML, SQL. Internally, the method
     * calls all the <code>dump*()</code> methods based on the current dump mode.
     * If the derivatives use only <code>DUMP_GZIP_BINARY</code> or <code>DUMP_BINARY</code> modes, the
     * need not do anything except implementing <code>backSynchronizeObject()</code>. For the
     * rest of modes they only have to override a corresponding <code>dump*()</code> method.
     *
     * @throws StorageException if saving to a file for some reason fails or
     *                          the dump mode set to an unsupported value
     * @see #DUMP_GZIP_BINARY
     * @see #DUMP_BINARY
     * @see #dumpGzipBinary()
     * @see #dumpCSV()
     * @see #dumpBinary()
     * @see #dumpXML()
     * @see #dumpHTML()
     * @see #dumpSQL()
     * @see #backSynchronizeObject()
     * @see #iCurrentDumpMode
     */
    public synchronized void dump()
            throws StorageException {
        switch (this.iCurrentDumpMode) {
            case DUMP_GZIP_BINARY:
                dumpGzipBinary();
                break;

            case DUMP_CSV_TEXT:
                dumpCSV();
                break;

            case DUMP_BINARY:
                dumpBinary();
                break;

            case DUMP_XML:
                dumpXML();
                break;

            case DUMP_HTML:
                dumpHTML();
                break;

            case DUMP_SQL:
                dumpSQL();
                break;

            default:
                throw new StorageException("Unsupported dump mode: " + this.iCurrentDumpMode);
        }
    }

    /**
     * Implements object dump in GZIP-compressed form. Attempts
     * to save internal object reference to the generater/specified filename
     *
     * @throws StorageException in case of I/O or otherwise error during object dump
     * @see #oObjectToSerialize
     * @see #strFilename
     * @since 0.3.0
     */
    public synchronized void dumpGzipBinary()
            throws StorageException {
        try {
            FileOutputStream oFOS = new FileOutputStream(strFilename);
            GZIPOutputStream oGZOS = new GZIPOutputStream(oFOS);
            ObjectOutputStream oOOS = new ObjectOutputStream(oGZOS);

            oOOS.writeObject(this.oObjectToSerialize);
            oOOS.flush();
            oOOS.close();
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Implements object dump in plain binary form without compression. Attempts
     * to save internal object reference to the generater/specified filename
     *
     * @throws StorageException in case of I/O or otherwise error during object dump
     * @see #oObjectToSerialize
     * @see #strFilename
     * @since 0.3.0
     */
    public synchronized void dumpBinary()
            throws StorageException {
        try {
            FileOutputStream oFOS = new FileOutputStream(this.strFilename);
            ObjectOutputStream oOOS = new ObjectOutputStream(oFOS);

            oOOS.writeObject(this.oObjectToSerialize);
            oOOS.flush();
            oOOS.close();
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * If derivatives use the generic implementation of <code>dump()</code>
     * with the CSV dump mode, they must override this method.
     *
     * @throws NotImplementedException
     * @throws StorageException,       declared, but never thrown
     * @see #dump()
     * @since 0.3.0
     */
    public synchronized void dumpCSV()
            throws StorageException {
        throw new NotImplementedException(this, "dumpCSV()");
    }

    /**
     * If derivatives use the generic implementation of <code>dump()</code>
     * with the XML dump mode, they must override this method.
     *
     * @throws NotImplementedException
     * @throws StorageException,       declared, but never thrown
     * @see #dump()
     * @since 0.3.0
     */
    public synchronized void dumpXML()
            throws StorageException {
        throw new NotImplementedException(this, "dumpXML()");
    }

    /**
     * If derivatives use the generic implementation of <code>dump()</code>
     * with the HTML dump mode, they must override this method.
     *
     * @throws NotImplementedException
     * @throws StorageException,       declared, but never thrown
     * @see #dump()
     * @since 0.3.0
     */
    public synchronized void dumpHTML()
            throws StorageException {
        throw new NotImplementedException(this, "dumpHTML()");
    }

    /**
     * If derivatives use the generic implementation of <code>dump()</code>
     * with the SQL dump mode, they must override this method.
     *
     * @throws NotImplementedException
     * @throws StorageException,       declared, but never thrown
     * @see #dump()
     * @since 0.3.0
     */
    public synchronized void dumpSQL()
            throws StorageException {
        throw new NotImplementedException(this, "dumpSQL()");
    }

    /**
     * An object must know how restore itself or its data structures from a file.
     * Options are: Object serialization, XML, CSV, HTML, SQL. Internally, the method
     * calls all the <code>restore*()</code> methods based on the current dump mode.
     * If the derivatives use only <code>DUMP_GZIP_BINARY</code> or <code>DUMP_BINARY</code> modes, the
     * need not do anything except implementing <code>backSynchronizeObject()</code>. For the
     * rest of modes they only have to override a corresponding <code>restore*()</code> method.
     *
     * @throws StorageException if loading from a file for some reason fails or
     *                          the dump mode set to an unsupported value
     * @see #DUMP_GZIP_BINARY
     * @see #DUMP_BINARY
     * @see #dumpGzipBinary()
     * @see #dumpCSV()
     * @see #dumpBinary()
     * @see #dumpXML()
     * @see #dumpHTML()
     * @see #dumpSQL()
     * @see #backSynchronizeObject()
     * @see #iCurrentDumpMode
     */
    public synchronized void restore()
            throws StorageException {
        switch (this.iCurrentDumpMode) {
            case DUMP_GZIP_BINARY:
                restoreGzipBinary();
                break;

            case DUMP_CSV_TEXT:
                restoreCSV();
                break;

            case DUMP_BINARY:
                restoreBinary();
                break;

            case DUMP_XML:
                restoreXML();
                break;

            case DUMP_HTML:
                restoreHTML();
                break;

            case DUMP_SQL:
                restoreSQL();
                break;

            default:
                throw new StorageException("Unsupported dump mode: " + this.iCurrentDumpMode);
        }
    }

    /**
     * Implements object loading from plain binary form without compression. Attempts
     * to load internal object reference with the generater/specified filename. After,
     * calls <code>backSynchronizeObject()</code> so the actual mode can reset back
     * references in its own data structures. If the file that we attempt to load
     * did not exist, it will be created.
     *
     * @throws StorageException in case of I/O or otherwise error during object retoration
     * @see #backSynchronizeObject()
     * @see #strFilename
     * @since 0.3.0
     */
    public synchronized void restoreBinary()
            throws StorageException {
        try {
            FileInputStream oFIS = new FileInputStream(strFilename);
            ObjectInputStream oOIS = new ObjectInputStream(oFIS);

            this.oObjectToSerialize = oOIS.readObject();

            oOIS.close();

            backSynchronizeObject();
        } catch (FileNotFoundException e) {
            if (this.bDumpOnNotFound == true) {
                System.err.println
                        (
                                "StorageManager.restore() --- FileNotFoundException for file: \""
                                        + strFilename + "\", " +
                                        e.getMessage() + "\n" +
                                        "Creating one now..."
                        );

                dump();
            } else {
                throw new StorageException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new StorageException
                    (
                            "StorageManager.restore() --- ClassNotFoundException: " +
                                    e.getMessage()
                    );
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Implements object loading from GZIP-compressed binary form. Attempts
     * to load internal object reference with the generater/specified filename. After,
     * calls <code>backSynchronizeObject()</code> so the actual mode can reset back
     * references in its own data structures. If the file that we attempt to load
     * did not exist, it will be created.
     *
     * @throws StorageException in case of I/O or otherwise error during object retoration
     * @see #backSynchronizeObject()
     * @see #strFilename
     * @since 0.3.0
     */
    public synchronized void restoreGzipBinary()
            throws StorageException {
        try {
            FileInputStream oFIS = new FileInputStream(this.strFilename);
            GZIPInputStream oGZIS = new GZIPInputStream(oFIS);
            ObjectInputStream oOIS = new ObjectInputStream(oGZIS);

            this.oObjectToSerialize = oOIS.readObject();

            oOIS.close();

            backSynchronizeObject();
        } catch (FileNotFoundException e) {
            if (this.bDumpOnNotFound == true) {
                System.err.println
                        (
                                "StorageManager.restore() --- FileNotFoundException for file: \""
                                        + this.strFilename + "\", " +
                                        e.getMessage() + "\n" +
                                        "Creating one now..."
                        );

                dump();
            } else {
                throw new StorageException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new StorageException
                    (
                            "StorageManager.restore() --- ClassNotFoundException: " +
                                    e.getMessage()
                    );
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * If derivatives use the generic implementation of <code>restore()</code>
     * with the CSV dump mode, they must override this method.
     *
     * @throws NotImplementedException
     * @throws StorageException,       declared, but never thrown
     * @see #restore()
     * @since 0.3.0
     */
    public synchronized void restoreCSV()
            throws StorageException {
        throw new NotImplementedException(this, "restoreCSV()");
    }

    /**
     * If derivatives use the generic implementation of <code>restore()</code>
     * with the XML dump mode, they must override this method.
     *
     * @throws NotImplementedException
     * @throws StorageException,       declared, but never thrown
     * @see #restore()
     * @since 0.3.0
     */
    public synchronized void restoreXML()
            throws StorageException {
        throw new NotImplementedException(this, "restoreXML()");
    }

    /**
     * If derivatives use the generic implementation of <code>restore()</code>
     * with the HTML dump mode, they must override this method.
     *
     * @throws NotImplementedException
     * @throws StorageException,       declared, but never thrown
     * @see #restore()
     * @since 0.3.0
     */
    public synchronized void restoreHTML()
            throws StorageException {
        throw new NotImplementedException(this, "restoreHTML()");
    }

    /**
     * If derivatives use the generic implementation of <code>restore()</code>
     * with the SQL dump mode, they must override this method.
     *
     * @throws NotImplementedException
     * @throws StorageException,       declared, but never thrown
     * @see #restore()
     * @since 0.3.0
     */
    public synchronized void restoreSQL()
            throws StorageException {
        throw new NotImplementedException(this, "restoreSQL()");
    }

    /**
     * Must to be overridden by the modules that use object serialization
     * with the generic implementation of <code>restore()</code>. By default
     * this method is unimplemented.
     *
     * @throws NotImplementedException
     * @see #restore()
     * @since 0.3.0
     */
    public synchronized void backSynchronizeObject() {
        throw new NotImplementedException(this, "backSynchronizeObject()");
    }

    /**
     * Retrieves inner filename reference.
     *
     * @return filename string
     * @since 0.3.0
     */
    public synchronized String getFilename() {
        return this.strFilename;
    }

    /**
     * Allows to alter inner filename reference.
     *
     * @param pstrFilename new filename
     * @since 0.3.0
     */
    public synchronized void setFilename(String pstrFilename) {
        this.strFilename = pstrFilename;
    }

    /**
     * Retrieves default filename extension of this storage manager.
     *
     * @return extension String
     * @since 0.3.0
     */
    public synchronized String getDefaultExtension() {
        return getDefaultExtension(this.iCurrentDumpMode);
    }

    /**
     * Retrieves default filename extension depending on dump type.
     *
     * @param piDumpMode dump mode to query extensions map by
     * @return extension String; "unknown" is returned if the parameter
     * is outside of the range
     * @since 0.3.0
     */
    public static String getDefaultExtension(int piDumpMode) {
        try {
            return STORAGE_FILE_EXTENSIONS[piDumpMode];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "unknown";
        }
    }

    /**
     * Retrieves current dump mode.
     *
     * @return the mode, integer
     * @since 0.3.0
     */
    public synchronized final int getDumpMode() {
        return this.iCurrentDumpMode;
    }

    /**
     * Sets the dump mode.
     *
     * @param piCurrentDumpMode the mode
     * @since 0.3.0
     */
    public synchronized final void setDumpMode(final int piCurrentDumpMode) {
        this.iCurrentDumpMode = piCurrentDumpMode;
    }

    /**
     * Enables or disables creation of a file if it does not exist.
     *
     * @param pbEnable new value of the flag
     * @return old value of the flag
     * @see #bDumpOnNotFound
     * @since 0.3.0
     */
    public synchronized final boolean enableDumpOnNotFound(boolean pbEnable) {
        boolean bOld = this.bDumpOnNotFound;
        this.bDumpOnNotFound = pbEnable;
        return bOld;
    }

    /**
     * Implements Cloneable for this StorageManager.
     * The object to serialize is not clone, but rather
     * its reference is copied by assignment.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public synchronized Object clone() {
        try {
            StorageManager oClone = (StorageManager) super.clone();

            oClone.strFilename = new String(this.strFilename);
            oClone.oObjectToSerialize = this.oObjectToSerialize;

            return oClone;
        }

        // Should never happend.
        catch (CloneNotSupportedException e) {
            throw new InternalError(e.getMessage());
        }
    }

    /**
     * Checks equality of two storage managers whether
     * the parameter is not null and its toString() output
     * is equal to this one.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @since 0.3.0.5
     */
    public synchronized boolean equals(Object poStorageManager) {
        if (poStorageManager instanceof StorageManager) {
            return poStorageManager.toString().equals(toString());
        }

        return false;
    }

    /**
     * Overrides <code>hashCode()</code> since <code>equals()</code> is overridden by
     * returning the hash code of the <code>toString()</code>.
     *
     * @see java.lang.Object#hashCode()
     * @see #equals(Object)
     * @since 0.3.0.5
     */
    public synchronized int hashCode() {
        return toString().hashCode();
    }

    /**
     * Default implementation of the toString() for all stoage
     * manager derivatives.
     *
     * @see java.lang.Object#toString()
     * @since 0.3.0.5
     */
    public synchronized String toString() {
        String strObjectName =
                this.oObjectToSerialize == null ?
                        "null" :
                        this.oObjectToSerialize.getClass().getName();

        return new StringBuffer()
                .append("StorageManager implementation: ").append(getClass().getName()).append("\n")
                .append("Filename: ").append(this.strFilename).append("\n")
                .append("Current dump mode: ").append(this.iCurrentDumpMode).append("\n")
                .append("Dump if not found: ").append(this.bDumpOnNotFound).append("\n")
                .append("Object to dump: ").append(strObjectName).append("\n")
                .toString();
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.26 $";
    }
}

// EOF
