package ch.hsr.ifs.petmanager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class PetManagerTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PetManagerTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PetManagerTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testPetManager() {
        assertTrue(true);
    }
    
}
