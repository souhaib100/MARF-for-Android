package marf.Storage;

import marf.util.NotImplementedException;


/**
 * <p>Subject database.
 * To be used by an application to contain information
 * about known subjects (speakers, instruments, languages, etc).</p>
 * <p>
 * $Id: Database.java,v 1.10 2006/01/02 22:24:00 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.10 $
 * @since 0.3.0.2
 */
public class Database
        extends StorageManager
        implements IDatabase {
    /**
     * Indicates whether we are connected or not.
     */
    protected boolean bConnected = false;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -2712666434829768112L;

    /**
     * Default constructor with the StorageManager.
     *
     * @see StorageManager
     */
    public Database() {
        super();
        this.oObjectToSerialize = this;
    }

    /**
     * Given ID, fetches the corresponding filename.
     * Retrieves Speaker's ID by a sample filename.
     *
     * @param pstrFileName Name of a .wav file for which ID must be returned
     * @param pbTraining   indicates whether the filename is a training (<code>true</code>) sample or testing (<code>false</code>)
     * @return int ID, -1 if not found
     * @throws StorageException
     */
    public int getIDByFilename(String pstrFileName, boolean pbTraining)
            throws StorageException {
        throw new NotImplementedException();
    }

    /**
     * Retrieves subject's name by their ID.
     *
     * @param piID ID of a subject in the DB to return a name for
     * @return name string
     * @throws StorageException
     */
    public String getName(int piID)
            throws StorageException {
        throw new NotImplementedException();
    }

    /**
     * Connects to the database of subjects.
     *
     * @throws StorageException
     */
    public void connect()
            throws StorageException {
        throw new NotImplementedException();
    }

    /**
     * Retrieves subject's data from the database and populates
     * internal data structures.
     *
     * @throws StorageException
     */
    public void query()
            throws StorageException {
        throw new NotImplementedException();
    }

    /**
     * Closes (file) database connection.
     *
     * @throws StorageException
     */
    public void close()
            throws StorageException {
        throw new NotImplementedException();
    }

    /**
     * Implementaion of back-synchronization of Database loaded object.
     *
     * @since 0.3.0.5
     */
    public void backSynchronizeObject() {
        Database oDatabase = (Database) this.oObjectToSerialize;
        this.bConnected = oDatabase.bConnected;
        this.bDumpOnNotFound = oDatabase.bDumpOnNotFound;
        this.iCurrentDumpMode = oDatabase.iCurrentDumpMode;
        this.strFilename = oDatabase.strFilename;
        this.oObjectToSerialize = this;
    }

    /**
     * Default implementation of the toString() for all stoage
     * manager derivatives.
     *
     * @see java.lang.Object#toString()
     * @since 0.3.0.5
     */
    public String toString() {
        return new StringBuffer()
                .append(super.toString())
                .append("Connected: ").append(this.bConnected).append("\n")
                .toString();
    }

    /**
     * Implementes Cloneable interface for the Database object.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        Database oClone = (Database) super.clone();
        return oClone;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.10 $";
    }
}

// EOF
