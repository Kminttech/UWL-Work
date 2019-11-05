import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Lab Assignment 06: This file will load data from an input file into a
 * hash-table, then print out some results.
 *
 * @author M. Allen
 * @author Kevin Minter
 *
 */

public class Main
{
    /**
     * Does some sample hashing on a few vehicles, then runs loadHash().
     */
    private void procVehicles()
    {
        Vehicle[] vhs = { new Vehicle( "Chevy", "Tahoe", 2001, 25000 ),
            new Vehicle( "Chevy", "Tahoe", 2001, 11000 ),
            new Vehicle( "Chevy", "Tahoe", 2002, 25000 ) };
        
        System.out.println();
        for ( int v1 = 0; v1 < vhs.length; v1++ )
            for ( int v2 = 0; v2 < vhs.length; v2++ )
                System.out.println( vhs[v1] + " equals " + vhs[v2] + " => " +
                                   vhs[v1].equals( vhs[v2] ) );
        
        System.out.println();
        for ( int v1 = 0; v1 < vhs.length; v1++ )
            System.out.println( "Hashing: " + vhs[v1] + " => " +
                               vhs[v1].hashCode() );
        System.out.println();
        
        loadHash( "database.txt" );
    }
    
    /**
     * Method to be filled in later.
     *
     * @param fileInput Text-file to read data from.
     */
    private void loadHash( String fileInput )
    {
    	SeparateChainingHashTable<Vehicle> table = new SeparateChainingHashTable();
    	try{
    		File input = new File(fileInput);
    		Scanner parcer = new Scanner(input);
    		while(parcer.hasNextLine()){
    			table.insert(new Vehicle(parcer.next(),parcer.next(),parcer.nextInt(),parcer.nextInt()));
    		}
    		parcer.close();
    	}catch(IOException ex){
    		System.out.println("Error with File");
    	}
    	System.out.println(table.size());
    	table.printTable();
    }
    
    /**
     * Simple initiating method.
     *
     * @param args Not used.
     */
    public static void main( String[] args )
    {
        final Main m = new Main();
        m.procVehicles();
    }
    
}
