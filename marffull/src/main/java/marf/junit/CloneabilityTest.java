package marf.junit;

import junit.framework.TestCase;

import marf.Storage.StorageException;
import marf.junit.Stats.WordStatsTest;
import marf.junit.Storage.DatabaseTest;


/**
 * <p>Performs cloneability unit tests.</p>
 * <p>
 * $Id: CloneabilityTest.java,v 1.3 2006/02/23 01:15:34 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @author Shuxin Fan
 * @since 0.3.0.5
 */
public class CloneabilityTest
        extends TestCase {
    /**
     * @param argv
     */
    public static void main(String[] argv) {
        //junit.textui.TestRunner.run(CloneabilityTest.class);
    }

    /**
     * @param pstrName
     */
    public CloneabilityTest(String pstrName) {
        super(pstrName);
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
        TestCase oTestDbClone = new DatabaseTest("testClone");
        oTestDbClone.run();
        TestCase oTestWsClone = new WordStatsTest("testClone");
        oTestWsClone.run();
    }
}

// EOF
