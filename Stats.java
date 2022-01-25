/**
 * 
 */

/**
 * Class to keep track of cahce hits, cache misses, and disk reads.
 * 
 * @author Camille Blaine (cblaine)
 * @author Samridhi Roshan (samridhi18)
 * @version 7/1/2021
 */
public class Stats {
    private int hit;
    private int miss;
    private int read;
    private int write;

    /**
     * Constructor
     */
    public Stats() {

        // Initialize all the counts
        hit = 0;
        miss = 0;
        read = 0;
        write = 0;

    }


    /**
     * Increment the number of cache hits
     */
    public void hit() {
        hit++;

    }


    /**
     * Increment the number of cache misses
     */
    public void miss() {
        miss++;

    }


    /**
     * Increment the number of disk reads
     */
    public void diskRead() {
        read++;
    }


    /**
     * Increment the number of disk writes
     */
    public void diskWrite() {
        write++;
    }


    /**
     * Getter method for hits
     * 
     * @return the number of hits
     */
    public int hitCount() {
        return hit;
    }


    /**
     * Getter method for misses
     * 
     * @return the number of misses
     */
    public int missCount() {
        return miss;
    }


    /**
     * Getter method for disk reads
     * 
     * @return read the number of disk reads
     */
    public int diskReadCount() {
        return read;
    }


    /**
     * Get disk writes
     * 
     * @return the number of disk writes
     */
    public int diskWriteCount() {
        return write;
    }

}
