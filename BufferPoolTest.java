import java.io.File;

import java.io.IOException;

import student.TestCase;

/**
 * 
 */

/**
 * Test class for buffer pool
 * 
 * @author Camille Blaine (cblaine)
 * @author Samridhi Roshan (samridhi18)
 * @version (2021-07-01)
 *
 */
public class BufferPoolTest extends TestCase {

    private BufferPool bp;

    private BufferPool sec;
    private byte[] by;

    private byte[] by2;

    /**
     * Set up
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        Stats s = new Stats();
        File file = new File("12288Bytes");

        bp = new BufferPool(file, 2, s);
        sec = new BufferPool(file, 1, s);
        by = new byte[4];
        by2 = new byte[5];
        by[0] = 1;
        by[1] = 2;
        by[2] = 3;
        by[3] = 4;

        by2[0] = 2;
        by2[1] = 3;
        by2[2] = 3;
        by2[3] = 1;
        by2[4] = 4;

    }


    /**
     * Test acquire buffer
     * 
     * @throws IOException
     */
    public void testAcquireBuffer() throws IOException {

        // Make sure queue is empty
        assertEquals(0, bp.getQueue().length());
        Buffer b = bp.acquireBuffer(2300);
        assertEquals(0, b.getOffset());
        b = bp.acquireBuffer(6000);
        assertEquals(4096, b.getOffset());
        assertEquals(2, bp.getQueue().length());
        assertNull(bp.isInside(12000));
        b = bp.acquireBuffer(12000);

        assertEquals(8192, b.getOffset());
        assertEquals(2, bp.getQueue().length());
    }


    /**
     * @throws IOException
     * 
     */
    public void testAcquireBufferOne() throws IOException {
        sec.acquireBuffer(1000);
        sec.flush();
        assertEquals(0, sec.getQueue().length());

    }


    /**
     * Test use
     * 
     * @throws IOException
     */
    public void testUse() throws IOException {
        Record rec = bp.getRecord(1);
        rec = bp.getRecord(1500);
        assertEquals(2, bp.getQueue().length());

        // add one more buffer. Queue should stay at 2
        rec = bp.getRecord(2500);
        assertEquals(2, bp.getQueue().length());

        // The front value should be 2 because it is the least recently used
        assertEquals(4096, bp.getQueue().frontValue().getOffset());
        bp.getQueue().next();

        // back value should be 3 because it is the most recently used
        assertEquals(8192, bp.getQueue().getValue().getOffset());

        // acquire a buffer that has already been acquired
        rec = bp.getRecord(1505);

        // queue size should stay the same
        assertEquals(2, bp.getQueue().length());

        // move the queue to the front
        bp.getQueue().moveToFront();
        // the buf 3 should be in the back and the buf2 should be in the front
        assertEquals(8192, bp.getQueue().frontValue().getOffset());
        bp.getQueue().next();
        assertEquals(4096, bp.getQueue().getValue().getOffset());

    }


    /**
     * Test use again
     * 
     * @throws IOException
     */
    public void testExtraUse() throws IOException {

        Record rec = bp.getRecord(1);
        rec = bp.getRecord(1);

        assertEquals(1, bp.getQueue().length());

        // Test dirty block
        // bp.dirtyBlock(0); // was this method removed?????????????
    }


    /**
     * Test flush and remove buffer
     * 
     * @throws IOException
     */
    public void testFlush() throws IOException {
        Record rec = bp.getRecord(1);
        rec = bp.getRecord(5000);
        assertEquals(2, bp.getQueue().length());

        bp.flush();
        assertEquals(0, bp.getQueue().length());

    }


    /**
     * test get record
     * 
     * @throws IOException
     */
    public void testGetRecord() throws IOException {

        Record rec = bp.getRecord(1);
        Buffer buf1 = bp.getQueue().frontValue();
        buf1.write(by, 4);
        Record one = buf1.getRecord(4);

        Record two = bp.getRecord(1);

        Record ra = bp.getRecord(2);
        bp.getQueue().next();
        Buffer buf2 = bp.getQueue().getValue();
        buf2.write(by2, 12);
        ra = buf2.getRecord(4);
        rec = buf2.getRecord(12);

        // Test swap
        bp.swap(1, 3);
        assertEquals(1, bp.getQueue().length());

    }


    /**
     * Test swap
     * 
     * @throws IOException
     */
    public void testSwap() throws IOException {
        Record rec = bp.getRecord(2);
        Record ra = bp.getRecord(3);

        // Check the queue

        // set the buffers
        Buffer buf1 = bp.getQueue().frontValue();
        // bp.getQueue().next();
        Buffer buf2 = bp.getQueue().getValue();

        // Write values into the buffers
        buf1.write(by, 8);
        buf2.write(by2, 9);

        Record recCheck = bp.getRecord(2);
        Record raCheck = bp.getRecord(3);

        int posOne = 2;
        int posTwo = 3;

        // Check the queue
        assertEquals(1, bp.getQueue().length());

        bp.swap(posOne, posTwo);

    }

}
