import java.io.*;
import student.TestCase;

/**
 * This class tests the Heap Sort's main method
 * 
 * @author Samridhi Roshan
 * @author Camille Blaine
 * @version 7/1/2021
 *
 */
public class HeapSortTest extends TestCase {

    // instance of the class to be tested
    private HeapSort heap;
    private String stdout; // to see what is printed in the console
    // to use the CheckFile class
    private CheckFile chFile;


    /**
     * This is the set up for the tests
     */
    public void setUp() {
        heap = new HeapSort();
        chFile = new CheckFile();

    }


    /**
     * Tests if the input file to be sorted is not present
     * 
     * @throws IOException
     */
    public void testNoInputFile() throws IOException {
        // set up arguments
        String[] args = new String[3];
        args[0] = "someInput.dat";
        args[1] = "2";
        args[2] = "someOutput.dat";
        heap.main(args);
        stdout = systemOut().getHistory(); // now get the print from console
        // check if the correct error message is printed
        assertEquals("Input File does not exists someInput.dat" + "\n", stdout);

        systemOut().clearHistory(); // just if we want to test other prints
    }


    /**
     * Tests that the program works correctly when the file exists
     * 
     * @throws Exception
     */
    public void testFileExists() throws Exception {
        // set up arguments
        String[] args = new String[3];
        args[0] = "SampleInput.dat"; // exists
        args[1] = "2";
        args[2] = "heapOutputStat.txt"; // this file should be created and the
                                        // stats should be printed correctly

        heap.main(args); // run the program

        // Need to check if SampleInput.dat has the values in correct order
        boolean check = false;
        check = chFile.checkFile("SampleInput.dat");
        assertTrue(check);
    }


    /**
     * Tests the stdout for 8 elements print
     * 
     * @throws IOException
     */
    public void testStatOutput() throws IOException {
        String[] args = new String[3];
        args[0] = "SampleInput.dat"; // exists
        args[1] = "2";
        args[2] = "heapOutputStat.txt"; // this file should be created and the
                                        // stats should be printed correctly
        heap.main(args); // run the program
        stdout = systemOut().getHistory(); // now get the print from console
        // check if the correct error message is printed
        assertEquals("    5  8404  8131   244 16634  2746 24619  6627 " + "\n"
            + "\n", stdout);

        systemOut().clearHistory(); // just if we want to test other prints

    }
}
