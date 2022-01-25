
import student.TestCase;

/**
 * This is the test class for the doubly linked queue
 * 
 * @author Camille Blaine (cblaine)
 * @author Samridhi Roshan (samridhi18)
 * @version 7/1/2021
 *
 */
public class DLQueueTest extends TestCase {

    /**
     * instance of the class to be tested
     */
    private DLQueue<Short> queue;


    /**
     * The test set up for the doubly linked list Queue
     */
    public void setUp() {
        queue = new DLQueue<Short>();
    }


    /**
     * Tests the initialization state
     */
    public void testInit() {
        assertEquals(0, queue.length());
    }


    /**
     * Test enqueue and dequeue with just one elem
     */
    public void testOneElem() {
        queue.enqueue((short)245);
        assertEquals(1, queue.length());
        short num = queue.frontValue();
        assertEquals((short)245, num);
        queue.dequeue();
        assertEquals(0, queue.length());
        assertNull(queue.frontValue());
    }


    /**
     * Tests empty lists front Val, dequeue and isEmpty
     */
    public void testEmpty() {
        assertTrue(queue.isEmpty());
        assertNull(queue.dequeue());
        assertNull(queue.frontValue());
    }


    /**
     * Tests multiple enqueue and then clear
     */
    public void testEnqueue() {

        assertFalse(queue.enqueue(null));

        queue.enqueue((short)100);
        queue.enqueue((short)101);
        queue.enqueue((short)102);
        queue.enqueue((short)103);

        assertEquals(4, queue.length());
        assertFalse(queue.isEmpty());

        short firstVal = queue.frontValue();
        assertEquals(100, firstVal);

        // Clear
        queue.clear();
        assertEquals(0, queue.length());

    }


    /**
     * Tests multiple dequeue
     */
    public void testDequeue() {
        queue.enqueue((short)8907);
        queue.enqueue((short)101);
        queue.enqueue((short)102);
        queue.enqueue((short)103);
        queue.enqueue((short)890);

        assertEquals(5, queue.length());
        assertFalse(queue.isEmpty());

        short firstVal = queue.frontValue();
        assertEquals(8907, firstVal);

        // Now dequeue
        short elemRemoved = queue.dequeue();
        assertEquals(8907, elemRemoved);

        short frontElem = queue.frontValue();
        assertEquals(101, frontElem);

        elemRemoved = queue.dequeue();
        assertEquals(101, elemRemoved);

        elemRemoved = queue.dequeue();
        assertEquals(2, queue.length());
        assertEquals(102, elemRemoved);

        elemRemoved = queue.dequeue();
        assertEquals(103, elemRemoved);

        elemRemoved = queue.dequeue();
        assertEquals(0, queue.length());
        assertEquals(890, elemRemoved);
    }


    /**
     * Tests current node methods
     */
    public void testCurrMethods() {

        // Add 5 elements in the list as 1,2,3,4,5
        queue.enqueue((short)1);
        queue.enqueue((short)2);
        queue.enqueue((short)3);
        queue.enqueue((short)4);
        queue.enqueue((short)5);

        // curr should start at the front element
        short currVal = queue.getValue();
        assertEquals(1, currVal);

        queue.next(); // moves curr to next ptr
        currVal = queue.getValue();
        assertEquals(2, currVal);

        queue.next(); // moves curr to next ptr
        currVal = queue.getValue();
        assertEquals(3, currVal);

        queue.moveToFront();
        currVal = queue.getValue();
        assertEquals(1, currVal);

    }


    /**
     * Tests the current remove method
     */
    public void testRemove() {

        // Add 4 elements, curr is at the front now
        // 1, 2, 3, 4
        queue.enqueue((short)1);
        queue.enqueue((short)2);
        queue.enqueue((short)3);
        queue.enqueue((short)4);
        assertEquals(4, queue.length());

        // Remove when curr is at the front
        queue.prev(); // will still be the front elem
        short currVal = queue.getValue();
        assertEquals(1, currVal); // curr 1
        queue.remove(queue.getNode()); // remove current element i.e = 1
        assertEquals(3, queue.length());
        // the curr is set back to the new front = 2
        currVal = queue.getValue();
        assertEquals(2, currVal);

        // New list: 2, 3, 4
        // move curr next (2nd elem)
        queue.next();
        currVal = queue.getValue();
        assertEquals(3, currVal);
        queue.remove(queue.getNode());
        assertEquals(2, queue.length()); // 2, 4
        currVal = queue.getValue();
        assertEquals(2, currVal);

        // Now remove the last elem
        queue.next();
        queue.prev();
        queue.next();
        currVal = queue.getValue();
        assertEquals(4, currVal);
        queue.next();
        currVal = queue.getValue();
        // should be still 4 as curr is rear
        assertEquals(4, currVal);

        queue.remove(queue.getNode());
        assertEquals(1, queue.length()); // 2 - list
        currVal = queue.getValue();
        assertEquals(2, currVal);

        // Only one elem left
        queue.remove(queue.getNode());
        assertEquals(0, queue.length());
        assertNull(queue.getValue());
        queue.moveToEnd();
        assertNull(queue.getValue());

    }

}
