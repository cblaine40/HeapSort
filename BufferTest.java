import java.io.File;

import java.io.IOException;
import java.io.RandomAccessFile;

import student.TestCase;

/**
 * Test case for a buffer
 * 
 * @author Camille Blaine (cblaine)
 * @author Samridhi Roshan (samridhi18)
 * @version (2021-07-01)
 */
public class BufferTest extends TestCase {

    private Buffer buf;

    private BufferPool pool;

    private byte[] by;

    /**
     * Set up
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        int offset = 0;
        File file = new File("4096Bytes.txt");

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        Stats sta = new Stats();
        pool = new BufferPool(file, 3, sta);
        buf = new Buffer(raf, pool, offset, sta);

        by = new byte[4];
        byte one = 01;
        byte two = 23;
        byte three = 00;
        byte four = 33;
        by[0] = one;
        by[1] = two;
        by[2] = three;
        by[3] = four;

    }


    /**
     * Test get data pointer
     * 
     * @throws IOException
     */
    public void testRead() throws IOException {
        byte[] test = buf.read();
        // Not sure if this is testing it correct

        assertEquals(4096, test.length);
    }


    /**
     * Test write
     * 
     * @throws IOException
     */
    public void testWrite() throws IOException {
        Record rex = pool.getRecord(0);
        buf = pool.getQueue().frontValue();

        buf.write(by, 0);

        assertEquals(4096, buf.getSize());

    }


    /**
     * Test dirty bit
     * 
     * @throws IOException
     */
    public void testDirtyAndFlush() throws IOException {

        // Test not dirty
        buf.flush();
        assertEquals(0, pool.getQueue().length());

        Record rex = pool.getRecord(0);
        buf = pool.getQueue().frontValue();

        buf.write(by, 0);

        assertEquals(4096, buf.getSize());
        assertEquals(1, pool.getQueue().length());
        // test dirty
        buf.flush();

        assertEquals(0, pool.getQueue().length());

    }


    /**
     * Test get record
     * 
     * @throws IOException
     */
    public void testGetRecord() throws IOException {
        Record rex = pool.getRecord(0);
        buf = pool.getQueue().frontValue();

        assertEquals(1, pool.getQueue().length());
    }

}
