/**
 * Class for heap-sorting lab.
 *
 * @author M. Allen
 * @author YOUR NAME HERE
 */
import java.io.*;
import javax.swing.*;

public class Sorter
{
    private File inFile;
    private JTextArea output;
    
    /**
     * Constructor for the default Sorter object, taking data from input file
     * and printing it in sorted order to a given text area.
     *
     * @param fin Text file containing IP data.
     * @param out Text-area where sorted results will display.
     */
    public Sorter( File fin, JTextArea out )
    {
        inFile = fin;
        output = out;
    }
    
    /**
     * Sort the data inFile according to the upload usage.
     */
    public void printSortUp()
    {
        // TO BE FILLED IN (DELETE THIS COMMENT)
    }
    
    /**
     * Sort the data inFile according to the download usage.
     */
    public void printSortDown()
    {
        // TO BE FILLED IN (DELETE THIS COMMENT)
    }
}
