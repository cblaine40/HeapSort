
// On my honor:
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project
// with anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.
// - samridhi18, cblaine
import java.io.*;

/**
 * A memory manager package for variable length
 * records, and a spatial data structure to support
 * geographical queryies.
 * 
 * @author Camille Blaine (cblaine)
 * @author Samridhi Roshan (samridhi18)
 * @version 6/1/2021
 */
public class HeapSort {

    /**
     * Represents one block size
     */
    public static final int BLOCK_SIZE = 4096;
    /**
     * Represents the size of one record which has a 2 byte key and 2 byte value
     */
    public static final int RECORD_SIZE = 4;
    /**
     * This is the total number of records that can be stored in a buffer of
     * size 4096 as 4 * 1024 = 4096
     */
    public static final int BUFFER_RECORDS_NUM = 1024;

    /**
     * This is the entry point of the application
     * 
     * @param args
     *            Command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Stats stats = new Stats();
        // inputFile is the input file to the program
        File inputFile = null;
        // args[0] is the name of the input file
        inputFile = new File(args[0]);

        if (!inputFile.exists()) {
            // if input file does not exist, inform and stop
            System.out.println("Input File does not exists " + args[0]);
            return;
        }

        // If the file exists, set the pool
        BufferPool bufPool = new BufferPool(inputFile, Integer.parseInt(
            args[1]), stats);

        // The pool now needs to be sent to maxHeap class that handels heapify
        // and sort
        int numRecords = (int)inputFile.length() / RECORD_SIZE;
        MaxHeap heap = new MaxHeap(numRecords, bufPool);

        long startTime = System.currentTimeMillis();
        // Now, sort the heap
        heap.heapSort(numRecords);

        // Need to flush the pool now (should write the buffers value back to
        // the disk)
        bufPool.flush();
        long endTime = System.currentTimeMillis();
        long sortTime = endTime - startTime;

        // TODO: CHECK WHERE THE SAMPLEOUTPUT.TXT OUTPUT (which is the 8 records
        // and all) IS PRINTED??????? STDOUT OR SOME FILE??

        // Create a new output file based on the name given on the command line
        // args[2] has the name of the output file
        File file = new File(args[2]);
        // if the file does not exists, create the file
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        // This prints the lines in the output file
        pw.println("------  STATS ------");
        // print all the data

        pw.println("File name: " + args[0]);
        pw.println("Cache Hits: " + stats.hitCount());
        pw.println("Cache Misses: " + stats.missCount());
        pw.println("Disk Reads: " + stats.diskReadCount());
        pw.println("Disk Writes: " + stats.diskWriteCount());
        // calculate the time needed to sort
        pw.println("Time to Sort: " + sortTime); // --->> TODO: Complete

        pw.close();

        // Print out the first 8 records
        for (int i = 0; i < numRecords / BUFFER_RECORDS_NUM; i++) {
            // 5 shorts could be up to 5 decimal integers: 5d
            System.out.printf("%5d %5d ", bufPool.getRecord(i
                * BUFFER_RECORDS_NUM).getKey(), bufPool.getRecord(i
                    * BUFFER_RECORDS_NUM).getValue());

            // Print 8 records on a line
            if ((i + 1) % 8 == 0) {
                System.out.println("\n");
            }

        }
        // new line
        System.out.println("\n");

    }
}
