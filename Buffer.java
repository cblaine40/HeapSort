import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 */

/**
 * Class that represents a single buffer in a buffer pool.
 * Each buffer is block size of 4096 bytes which is 1024 logical records.
 * 
 * @author Camille Blaine (cblaine)
 * @author Samridhi Roshan (samridhi18)
 * @version (2021-07-01)
 */
class Buffer {
    // The buffer pool
    private BufferPool bp;

    // Get the stats class
    private Stats stat;
    // Boolean to say if the content has been changed
    private Boolean dirtyBit;
    private RandomAccessFile disk;

    private byte[] buf;

    private int offset;

    private byte[] data;

    /**
     * Buffer constructor
     * 
     * @throws IOException
     * @param file
     *            the file randomly passed from the BufferPool
     * @param pool
     *            the BufferPool in charge of the buffers
     * 
     * @param pos
     *            the position of the intial offset the buffer is set to
     * @param stats
     *            class in charge of keeping statistics of all hits and misses
     */
    public Buffer(RandomAccessFile file, BufferPool pool, int pos, Stats stats)
        throws IOException {
        bp = pool;
        // Mark the dirty bit as false
        dirtyBit = false;
        disk = file;
        // offset = pos;
        this.setOffset(pos);
        buf = null;
        // stat = new Stats();
        stat = stats;
        data = this.read();

    }


    /**
     * Set offset
     * 
     * @param setOffset
     *            the offset to set the buffer to
     */
    public void setOffset(int setOffset) {
        offset = setOffset;
    }


    /**
     * Reads the entire buffer
     * 
     * @throws IOException
     * @return the data stored in the buffer
     */
    public byte[] read() throws IOException {

        // bp.use(this);
        // if the buf is null
        if (buf == null) {
            stat.miss();
            buf = new byte[HeapSort.BLOCK_SIZE];

            // Search for the position
// System.out.println("offset in read MISS " + offset);
            disk.seek(offset);

            // Read in the file
            disk.read(buf);

            // increment a disk read
            stat.diskRead();

        }
        else {
            // System.out.println("offset in read HIT " + offset);
            stat.hit();
        }
// data = buf;
        return buf;

    }


    /**
     * Flag the buffer's contents for being changed. Flushing the block will
     * write it back to the heap array.
     */
    public void markDirty() {
        // Flag the buffer
        dirtyBit = true;

    }


    /**
     * Write the byte
     * 
     * @param setData
     *            the data the Byte is to be set to
     *            git
     * @param location
     *            where in the byte array to set the data
     * @throws IOException
     */
    public void write(byte[] setData, int location) throws IOException {

        // System.out.println("Offset: " + getOffset());
        // bp.use(this);
        // Get the byte offset
        int byteOffset = location - (offset);

        // this.read();
        // Set the data to the data inputed
        for (int i = 0; i < HeapSort.RECORD_SIZE; i++) {
            data[byteOffset + i] = setData[i];
        }

        this.markDirty();
    }


    /**
     * Flush the byte
     * 
     * @throws IOException
     */
    public void flush() throws IOException {

        // Does it need to be written on to get written to the disk????
        if (dirtyBit) {
            disk.seek(offset);
            disk.write(data);
            // Increment the number of times written to the disk
            stat.diskWrite();

        }
        bp.removeBuffer(this);

        dirtyBit = false;
        buf = null;

    }


    /**
     * Getter method for the size of the buffer
     * 
     * @return the size of the buffer
     */
    public int getSize() {
        return buf.length;
    }


    /**
     * Getter method for the location
     * 
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }


    /**
     * Getter method for the record after given a location
     * 
     * @throws IOException
     * @param recordPos
     *            the location of the record inside the disk
     * @return the record correlated to the position
     */
    public Record getRecord(int recordPos) throws IOException {
        // get the recordPos - getOffset

        // recordPos is the actual position in the input file
        // getOffset should give the start block index in actual file

        int startLocation = recordPos - getOffset();

        byte[] record = new byte[HeapSort.RECORD_SIZE];

        // add the next four bytes to make a record

        for (int i = 0; i < HeapSort.RECORD_SIZE; i++) {

            record[i] = data[startLocation + i];
        }
        Record rec = new Record(record);

        return rec;

    }

}
