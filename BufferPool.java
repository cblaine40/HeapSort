import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Class to represent the buffer pool using buffer-passing style.
 * All accesses to the file are mediated by this class. The buffer pool is
 * organized by least recently used.
 * 
 * @author Camille Blaine (cblaine)
 * @author Samridhi Roshan (samridhi18)
 * @version (2021-07-01)
 *
 */
public class BufferPool {

    private RandomAccessFile disk;

    private int numBuffers;
    private DLQueue<Buffer> queue;
    private int maxBuffersNeeded;

    private Stats sta;

    /**
     * Buffer pool constructor
     * 
     * @throws IOException
     * @param file
     *            the file from the command
     * @param numBuffers
     *            the max number of buffers allowed to be used
     * @param stat
     *            the instance of the class to keep track of all stats
     */
    public BufferPool(File file, int numBuffers, Stats stat)
        throws IOException {
        sta = stat;

        // Opens a disk file for processing
        disk = new RandomAccessFile(file, "rw");

        // Number of the command that determines the number of buffers allocated
        // for the buffer pool
        this.numBuffers = numBuffers;

        maxBuffersNeeded = (int)(disk.length() / HeapSort.BLOCK_SIZE);

        // FURTHER IMPLEMENTATION
        // Make a queue the size of the number of buffers
        queue = new DLQueue<Buffer>();

    }


    /**
     * Relate a block to a buffer, returning a pointer to a buffer object
     * 
     * @throws IOException
     * @param location
     *            the position in the disk to acquire a buffer for
     * @return the buffer that matches the location
     */
    public Buffer acquireBuffer(int location) throws IOException {

        // Get the block index
        int blockIdx = getBlockIndex(location);

        // see if the buffer is already inside the queue
        Buffer bf = isInside(location);

        // if the buffer already exist, move it back in the queue and return it
        if (bf != null) {
            bf.read();
            promote(bf);

            return bf;
        }

        // check the queue
        if (queue.length() < numBuffers) {
            // make a new buffer

            bf = new Buffer(disk, this, (blockIdx * HeapSort.BLOCK_SIZE), sta); 
                                                                                
                                                                                
            // Enqueue the buffer
            queue.enqueue(bf);

        }
        else {
            // If the queue length is already at the max then remove the LRU
            // buffer and flush it

            queue.dequeue().flush();
            // make a new buffer
            bf = new Buffer(disk, this, (blockIdx * HeapSort.BLOCK_SIZE), sta);
            // enqueue the buffer
            queue.enqueue(bf);
        }

        return bf;

    }


    /**
     * Check to see if the buffer is inside the queue
     * 
     * @param offset
     *            the position of the buffer
     * @return a buffer either = to the match or null
     */
    public Buffer isInside(int offset) {
        // set a buffer to null
        Buffer offBuf = null;
        queue.moveToFront();

        // go through the queue, if a buffer is equal to one of the values in
        // the queue then set it to null buffer and return it
        for (int i = 0; i < queue.length(); i++) {

            // Check if the position is between the offset and 4096
            if (offset >= queue.getValue().getOffset() && offset < (queue
                .getValue().getOffset() + HeapSort.BLOCK_SIZE)) {
                offBuf = queue.getValue();
                return offBuf;

            }
            queue.next();
        }
        return offBuf;

    }


    /**
     * Flush buffer at the end of running the program
     * 
     * @throws IOException
     */
    public void flush() throws IOException {

        queue.moveToFront(); // sets the curr ptr to front
        // while the buffer isn't null
        for (int i = 0; i < queue.length(); i++) {
            queue.dequeue().flush();
            queue.next();

        }
        // Flush the last value
        if (queue.getValue() != null) {
            queue.dequeue().flush();
        }

    }

    // METHODS SPECIFICALLY INVOLVED WITH THE QUEUE


    /**
     * Rearrange the queue
     * 
     * @param used
     *            the buffer that was already used that needs to be moved back
     *            in the queue
     */
    public void promote(Buffer used) {
        queue.moveToFront();
        for (int i = 0; i < queue.length(); i++) {
            if (queue.getValue() == used) {
                // If the queue already has the buffer then move it down to
                // the
                // bottom

                // Remove the match

                queue.remove(queue.getNode());

                // enqueue it on the back
                queue.enqueue(used);

            }
            queue.next();
        }

    }


    /**
     * Getter method for the queue
     * 
     * @return the queue
     */
    public DLQueue<Buffer> getQueue() {
        return queue;
    }


    /**
     * Remove the buffer from the queue
     * 
     * @param delete
     *            the buffer to be deleted from the queue
     */
    public void removeBuffer(Buffer delete) {

        // Remove the buffer from the queue
        for (int i = 0; i < queue.length(); i++) {

            if (queue.getValue() == delete) {

                queue.remove(queue.getNode());
                break;
            }

            queue.next();

        }

    }


    /**
     * Swap two records
     * 
     * @param recOneLocation
     *            is the location of the first record to swap
     * 
     * @param recTwoLocation
     *            is the location of the second record to swap
     * 
     * @throws IOException
     */
    public void swap(int recOneLocation, int recTwoLocation)
        throws IOException {

// Multiply the locations by four to get where they are in the disk
        int locationX = (recOneLocation * HeapSort.RECORD_SIZE);
        int locationY = (recTwoLocation * HeapSort.RECORD_SIZE);

        // get the buffers for the correct block indexes
        Buffer bufOne = acquireBuffer(locationX);
        bufOne.setOffset(getBlockIndex(locationX) * HeapSort.BLOCK_SIZE);

        Buffer bufTwo = acquireBuffer(locationY);
        bufTwo.setOffset(getBlockIndex(locationY) * HeapSort.BLOCK_SIZE);

        // Get the record for each location
        byte[] firstRec = bufOne.getRecord(locationX).getByte();
        byte[] secondRec = bufTwo.getRecord(locationY).getByte();
        // temperorily store the first rec
        byte[] temp = new byte[HeapSort.RECORD_SIZE];
        temp[0] = firstRec[0];
        temp[1] = firstRec[1];
        temp[2] = firstRec[2];
        temp[3] = firstRec[3];

        // Get the specific locations

        // The first buffer write the second record at it's location
        bufOne.write(secondRec, locationX);
        // dirtyBlock(locationX);

        // The second buffer writes the first record at it's location
        bufTwo.write(temp, locationY);

    }


    /**
     * get the location to the index on the buffer array
     * 
     * @throws IOException
     * @param loc
     *            the location of the buffer in the disk
     * @return the block correlated to that location
     */
    private int getBlockIndex(int loc) {

        int arrayIndex = 0;

        for (int i = 0; i < maxBuffersNeeded; i++) {

            // Make the start the first part of the disk
            int start = i * HeapSort.BLOCK_SIZE;

            // checking if it is with in 4096 bytes each time
            if (loc >= start && loc < (start + HeapSort.BLOCK_SIZE)) {

                arrayIndex = i;

            }
        }

        return arrayIndex;
    }


    /**
     * Get specific record
     * 
     * @throws IOException
     * @param indexRec
     *            the record position inside the record array
     * @return the record correlated to that location
     */
    public Record getRecord(int indexRec) throws IOException {

        indexRec = indexRec * HeapSort.RECORD_SIZE; // converting indexRec into
                                                    // an index in the

        return acquireBuffer(indexRec).getRecord(indexRec);

    }

}
