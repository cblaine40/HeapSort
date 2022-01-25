import student.TestCase;
import java.io.*;

/**
 * Class to test the MaxHeap
 * 
 * @author Camille Blaine (cblaine)
 * @author Samridhi Roshan (samridhi18)
 * @version 7/1/2021
 *
 */
public class MaxHeapTest extends TestCase {

    // Objects to pass to the methods of maxHeap
    private BufferPool pool;

    // length of the records in a file
    private int length;

    /**
     * The test set up
     */
    public void setUp() throws IOException {

        // set up the buffer pool
        Stats stat = new Stats();
        File file = new File("SampleInput.dat");
        pool = new BufferPool(file, 2, stat);

        length = (int)(file.length() / 4);
    }


    /**
     * This method tests the heapify and sort methods
     * 
     * @throws IOException
     */
    public void testSort() throws IOException {

        // set the heap constructor which also build the heap
        MaxHeap heap = new MaxHeap(length, pool);
        // Then call the sort method
        heap.heapSort(length);

        // Checking these values from the sorted sample output provided to us in
        // class
        // Check if the values are in ascending order or not
        assertEquals(5, pool.getRecord(0).getKey());
        assertEquals(11, pool.getRecord(1).getKey());
        assertEquals(17, pool.getRecord(2).getKey());
        assertEquals(27, pool.getRecord(3).getKey());

        assertEquals(32765, pool.getRecord(4095).getKey()); // last rec
        assertEquals(32761, pool.getRecord(4094).getKey());
        assertEquals(32755, pool.getRecord(4093).getKey());

    }

}
