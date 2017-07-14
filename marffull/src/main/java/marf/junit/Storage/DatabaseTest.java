package marf.junit.Storage;

import junit.framework.TestCase;

import marf.Storage.Database;
import marf.Storage.StorageException;


/**
 * <p>Database data structure unit tests.</p>
 * <p>
 * $Id: DatabaseTest.java,v 1.3 2006/02/23 01:16:47 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @author Shuxin Fan
 * @since 0.3.0.5
 */
public class DatabaseTest
        extends TestCase {
    /**
     * @param args
     */
    public static void main(String[] args) {
        //junit.textui.TestRunner.run(DatabaseTest.class);
    }

    /**
     * @param name
     */
    public DatabaseTest(String name) {
        super(name);
    }

    protected void setUp()
            throws Exception {
        super.setUp();
    }

    protected void tearDown()
            throws Exception {
        super.tearDown();
    }

    /**
     * Test method for 'marf.Storage.Database.clone()'
     *
     * @throws StorageException
     */
    public void testClone()
            throws StorageException {
        Database oDatabase = new Database();
        //oDatabase.connect();
        Database oDatabaseClone = (Database) oDatabase.clone();
        System.out.println("db:" + oDatabase + "h: " + oDatabase.hashCode());
        System.out.println("cl:" + oDatabaseClone + "h: " +
                oDatabaseClone.hashCode());
        System.out.println("eq:" + oDatabase.equals(oDatabaseClone));
        assertTrue(oDatabase.equals(oDatabaseClone));
    }
}

// EOF
