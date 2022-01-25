import java.io.*;

/**
 * Class to heapify the array.
 * Leaf child (2*i + 1)
 * Right child (2*i +2)
 * Parent is (i-1)/2
 * 
 * @author Camille Blaine (cblaine)
 * @author Samridhi Roshan (samridhi18)
 * @version (2021-07-01)
 */
public class MaxHeap {

    private BufferPool pool;

    /**
     * Constructor for max heap
     * 
     * 
     * @param length
     *            is the length of the file
     * @param bp
     *            is the buffer pool
     * 
     * 
     * @throws IOException
     */
    public MaxHeap(int length, BufferPool bp) throws IOException {
        pool = bp;
        // heapify once before sorting so that we can start with a maxHeap
        // the index to begin with should be the last internal node
        // Building the tree
        for (int i = (length / 2) - 1; i >= 0; i--) {
            heapify(length, i);
        }

    }


    /**
     * Method to reset the tree in a max heap position
     * 
     * @param length
     *            the size of the heap
     * @param index
     *            the index to start at and check the children
     * @throws IOException
     */
    public void heapify(int length, int index) throws IOException {

        int l = (2 * index) + 1; // left
        int r = (2 * index) + 2; // right

        int largest = index;

        // compare the left child
        if (l < length && pool.getRecord(l).getKey() > pool.getRecord(index)
            .getKey()) {

            largest = l;
        }
        else {
            largest = index;
        }

        // compare the right child
        if (r < length && pool.getRecord(r).getKey() > pool.getRecord(largest)
            .getKey()) {

            largest = r;
        }

        // if root is not the largest
        if (largest != index) {
            pool.swap(index, largest);
            heapify(length, largest);
        }
    }


    /**
     * Sift down and remove the max record
     * 
     * @param heapSize
     *            the size of the heap
     * @throws IOException
     */
    public void heapSort(int heapSize) throws IOException {

        while (heapSize > 0) {
            pool.swap(0, heapSize - 1);
            // decrement the size
            heapSize--;

            heapify(heapSize, 0);
        }
    }
}
