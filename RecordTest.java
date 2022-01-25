
import student.TestCase;

/**
 * Test class for record
 * 
 * @author Camille Blaine (cblaine)
 * @author Samridhi Roshan (samridhi18)
 * @version (2021-07-01)
 */
public class RecordTest extends TestCase {

    private Record rec;

    /**
     * Set up
     */
    public void setUp() {
        byte[] by = new byte[4];
        byte one = 01;
        byte two = 23;
        byte three = 00;
        byte four = 33;
        by[0] = one;
        by[1] = two;
        by[2] = three;
        by[3] = four;

        rec = new Record(by);

    }


    /**
     * Test getter methods
     */
    public void testGet() {

        assertEquals(279, rec.getKey());

        assertEquals(33, rec.getValue());
    }


    /**
     * Print the full byte
     */
    public void testGetByte() {
        assertEquals(4, rec.getByte().length);
    }

}
