import student.TestCase;

/**
 * 
 */

/**
 * Test class for stats
 * 
 * @author Camille Blaine (cblaine)
 * @author Samridhi Roshan (samridhi18)
 * @version (2021-07-01)
 */
public class StatsTest extends TestCase {

    private Stats stat;

    /**
     * Set up
     */
    public void setUp() {
        stat = new Stats();
    }


    /**
     * Test each method
     */
    public void testStats() {
        stat.diskRead();
        stat.hit();
        stat.miss();
        stat.diskWrite();

        assertEquals(1, stat.hitCount());
        assertEquals(1, stat.diskReadCount());
        assertEquals(1, stat.missCount());
        assertEquals(1, stat.diskWriteCount());

    }

}
