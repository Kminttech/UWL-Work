/**
 * Simple main class for Bucket Sort lab.
 *
 * @author M. Allen
 */

public class Sorts
{
    // the bounds on array values
    final int MAX = 25;
    final int MIN = -25;
    
    /**
     * Simple method to generate int[] arrays of random, bounded numbers,
     * send them for sorting, and display the results.
     */
    private void runSorts()
    {
        // do a bucket sort on integers 0...MAX
        int[] ints = new int[25];
        
        for(int i = 0; i < ints.length; i++){
        	ints[i] = (int)(Math.random() * (MAX + 1));
        }
        
        for ( int i = 0; i < ints.length; i++ )
            System.out.print( ints[i] + " " );
        System.out.println();
        
        Bucket.sort( ints, MAX );
        
        for ( int i = 0; i < ints.length; i++ )
            System.out.print( ints[i] + " " );
        System.out.println( "\n" );
        
        // now do a bucket sort on integers MIN...MAX
        
		for(int i = 0; i < ints.length; i++){
			ints[i] = (int)(Math.random() * (MAX - MIN + 1)) + MIN;
		}
        
        for ( int i = 0; i < ints.length; i++ )
            System.out.print( ints[i] + " " );
        System.out.println();
        
        Bucket.sort( ints, MAX, MIN );
        
        for ( int i = 0; i < ints.length; i++ )
            System.out.print( ints[i] + " " );
        System.out.println( "\n" );
    }
    
    /**
     * Simple initiating main().
     *
     * @param args Not used.
     */
    public static void main( String[] args )
    {
        final Sorts s = new Sorts();
        s.runSorts();
    }
}
