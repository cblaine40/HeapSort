import java.nio.ByteBuffer;

/**
 * Class to represent a single record
 * 
 * @author Camille Blaine (cblaine)
 * @author Samridhi Roshan (samridhi18)
 * @version (2021-07-01)
 *
 */
public class Record {
    private ByteBuffer b;

    private byte[] recordFull;
    private byte[] key;
    private byte[] value;

    /**
     * Constructor
     * 
     * @param rec
     *            4 byte record that
     * 
     */
    public Record(byte[] rec) {

        recordFull = rec;
        key = new byte[2];
        value = new byte[2];
        key[0] = rec[0];
        key[1] = rec[1];
        value[0] = rec[2];
        value[1] = rec[3];
        b = ByteBuffer.allocate(2);

    }


    /**
     * Getter method to get the key for an individual record
     * 
     * @return the short representing the key
     */
    public short getKey() {

        short shortKey = b.wrap(key).getShort(0);

        return shortKey;

    }


    /**
     * Getter method to get the data value of a record
     * 
     * @return the short representing the value
     */
    public short getValue() {

        short val = b.wrap(value).getShort(0);

        return val;

    }


    /**
     * Get byte
     * 
     * @return the byte array that makes up the record
     */
    public byte[] getByte() {
        return recordFull;
    }

}
