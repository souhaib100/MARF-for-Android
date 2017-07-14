/*
 * WordStatsTest.java
 * JUnit based test
 *
 * Created on December 27, 2005, 9:19 PM
 */

package marf.junit.Stats;

import junit.framework.TestCase;

import marf.Stats.WordStats;


/**
 * <p>WordStats data structure unit tests.</p>
 * <p>
 * $Id: WordStatsTest.java,v 1.2 2006/02/23 01:16:46 mokhov Exp $
 *
 * @author Shuxin Fan
 * @since 0.3.0.5
 */
public class WordStatsTest
        extends TestCase {
    /**
     * @param args
     */
    public static void main(String[] args) {
        //junit.textui.TestRunner.run(WordStatsTest.class);
    }

    /**
     * @param name
     */
    public WordStatsTest(String name) {
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
     * Test of getLexeme method, of class marf.Stats.WordStats.
     */
    public void testGetLexeme() {
        System.out.println("testGetLexeme");
        WordStats oWordStats1 = new WordStats(10, "Christmas");
        assertEquals("Christmas", oWordStats1.getLexeme());
    }

    /**
     * Test of toString method, of class marf.Stats.WordStats.
     */
    public void testToString() {
        System.out.println("testToString");

        WordStats oWordStats1 = new WordStats(10, "Christmas");

        String strWordStats1
                = "Lexeme: " + oWordStats1.getLexeme() + ", "
                + "Frequency: " + oWordStats1.getFrequency() + ", "
                + "Rank: " + oWordStats1.getRank()
                + "";

        assertEquals(strWordStats1, oWordStats1.toString());
    }

    /**
     * Test of clone method, of class marf.Stats.WordStats.
     */
    public void testClone() {
        System.out.println("testClone of class marf.Stats.WordStats.");
        WordStats oWordStats1 = new WordStats(10, "Christmas");
        WordStats oWordStatsClone1 = (WordStats) oWordStats1.clone();

        assertTrue(oWordStats1.equals(oWordStatsClone1));

        WordStats oWordStats2 = new WordStats();
        WordStats oWordStatsClone2 = (WordStats) oWordStats2.clone();

        assertTrue(oWordStats2.equals(oWordStatsClone2));
    }
}

// EOF
