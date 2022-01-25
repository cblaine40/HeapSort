import java.util.NoSuchElementException;

/**
 * This class is a doubly linked list Queue which will help us to implement the
 * Least Recently Used policy in the buffer. There is a head and tail in the
 * list which will always have the value of null
 * There is a private Node class contained in this class
 * The methods names has been used from OpenDSA's Queue ADT
 * 
 * @author Camille Blaine (cblaine)
 * @author Samridhi Roshan (samridhi18)
 * @version (2021-07-01)
 * @param <T>
 *            the element to be inserted which is a buffer
 */
public class DLQueue<T> {

    /**
     * This is a private node class which represents a single Node to be used in
     * a doubly linked list
     *
     * @author Camille Blaine (cblaine)
     * @author Samridhi Roshan (samridhi18)
     * @version 7/1/2021
     */
    private static class Node<T> {
        // Pointer to the next node in the list
        private Node<T> next;
        // Pointer to the previous node in the list
        private Node<T> previous;
        // value of the node
        private T value;

        /**
         * Creates a new node with the given data
         *
         * @param val
         *            the data for the node
         */
        public Node(T val) {
            value = val;
            next = null;
            previous = null;
        }


        /**
         * Sets the node after the curr node
         *
         * @param node
         *            the node after this one
         */
        public void setNextNode(Node<T> node) {
            next = node;
        }


        /**
         * Sets the node before the curr node
         *
         * @param node
         *            the node before this one
         */
        public void setPreviousNode(Node<T> node) {
            previous = node;
        }


        /**
         * Gets the value in the node
         *
         * @return the value in the node
         */
        public T getValue() {
            return value;
        }


        /**
         * Gets the next node
         *
         * @return the next node
         */
        public Node<T> getNextNode() {
            return next;
        }


        /**
         * Gets the node before this one
         *
         * @return the node before this one
         */
        public Node<T> getPreviousNode() {
            return previous;
        }

    }

    // The queue size is the number of elements in the queue
    private int size;
    // The beginning of the list/ first element
    private Node<T> front;
    // The end of the list/ last element
    private Node<T> rear;

    // the current node
    private Node<T> curr;

    /**
     * Initialize the empty Double linked list Queue
     */
    public DLQueue() {
        size = 0;
        // set value of head and tail
        front = new Node<T>(null);
        front.next = null;
        front.previous = null;
        this.rear = new Node<T>(null);
        // set the front
        front.next = rear;
        front.previous = null;
        // set the rear
        rear.previous = front;
        rear.next = null;

        curr = front;
        curr.previous = null;

    }


    /**
     * Reinitialize the Queue like an empty list
     */
    public void clear() {
        size = 0;
        // set value of head and tail
        front = new Node<T>(null);
        rear = new Node<T>(null);
        // set the front
        front.setNextNode(rear);
        // set the rear
        rear.setPreviousNode(front);

        // set curr
        curr = front;
        curr.previous = null;
    }


    /**
     * Add elem at the rear end of the list
     * 
     * @param elem
     *            to be added
     * @return true if successful insertion
     */
    public boolean enqueue(T elem) {
        if (elem == null) {
            return false;
        }
        // add nodeElem to the list
        Node<T> nodeElem = new Node<T>(elem);
        if (size == 0) {
            // When there in no element in the list, both front and rear is the
            // same
            front = nodeElem;
            rear = front;

            curr = front; // set curr to be the front val to begin with

        }
        else {
            // When the list already has elements
            rear.setNextNode(nodeElem);
            nodeElem.setPreviousNode(rear);

            // Now update the last elem
            rear = nodeElem;
            rear.setNextNode(null);
        }

        size++;
        return true;

    }


    /**
     * Remove and return element from front
     * 
     * @return the removed element
     */
    public T dequeue() {
        if (size == 0) {
            return null;
        }
        Node<T> frontElem = front;
        // set front to next elem
        front = front.next;
        if (front == null) {
            // only one element present
            rear = null;

            curr = null; // curr as null
        }
        else {
            front.previous = null;

            curr = front; // set the new curr
        }

        size--;
        return frontElem.getValue();

    }


    /**
     * Return front element
     * 
     * @return first elem
     */
    public T frontValue() {
        if (size == 0) {
            return null;
        }
        return front.getValue();
    }


    /**
     * Return queue size
     * 
     * @return int size of the queue
     */
    public int length() {
        return size;
    }


    /**
     * Return true if the queue is empty
     * 
     * @return if it is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * Set curr point to the next node
     */
    public void next() {
        // curr only gets next if it is not rear
        if (curr != rear) {
            curr = curr.next;
        }
    }


    /**
     * Get previous node
     */
    public void prev() {
        if (curr != front) {
            curr = curr.previous;
        }
    }


    /**
     * It removes the removeNode from the queue
     * 
     * @param removeNode
     *            the node to be removed
     */
    public void remove(Node<T> removeNode) {

        if (removeNode == rear) {
            // them remove the last element

            if (rear.previous != null) {
                rear = rear.previous;
                rear.next = null;

            }
            else {
                // else, it is already the last element
                rear = null;
                front = null;
            }
            curr = front;
            size--;
            return; // or no throw no element
        }

        if (removeNode.getValue() == frontValue()) { // front element
            dequeue();
            return;

        }
        if (removeNode.next != null) {
            // removeNode's next.previous set to removeNode previous;
            removeNode.getNextNode().setPreviousNode(removeNode
                .getPreviousNode());
        }
        if (removeNode.previous != null) {
            // removeNode's previous.next set to removeNode next;
            removeNode.getPreviousNode().setNextNode(removeNode.getNextNode());
        }
        curr = front;
        size--;

    }


    /**
     * Get value of the current node
     * 
     * @throws NoSuchElementException
     * @return Object the element
     */
    public T getValue() {

        // when curr is null, size is also 0
        if (curr == null) {
            return null;
        }
        return curr.getValue();
    }


    /**
     * Get current node
     * 
     * @return the current node
     */
    public Node<T> getNode() {
        return curr;
    }


    /**
     * Move curr to the front
     */
    public void moveToFront() {
        curr = front;
    }


    /**
     * Move curr to end
     */
    public void moveToEnd() {
        curr = rear;
    }

}
