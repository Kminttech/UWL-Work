/**
 * Implements the Bucket sorting algorithm,
 * in two forms, one with a MAX bound, and
 * one with both MAX and MIN bounds.
 *
 * @author Kevin Minter
 */

public class Bucket
{
    /**
     * Simple integer-sorting linear time algorithm.
     *
     * @param arr Array of integers, with values in [0,max]. Sorted on return.
     * @param max Maximum value of any integer stored in array.
     */
    public static void sort( int[] arr, int max )
    {
        sort( arr, max, 0);
    }
    
    /**
     * Simple integer-sorting linear time algorithm.
     *
     * @param arr Array of integers, with values in [min,max]. Sorted on return.
     * @param max Maximum value of any integer stored in array.
     * @param min Minimum value of any integer stored in array.
     */
    public static void sort( int[] arr, int max, int min )
    {
    	int[] temp = new int[max - min + 1];
    	for(int i = 0; i < arr.length; i++){
        	temp[arr[i]-min]++;
        }
    	
    	int step = 0;
    	for(int i = 0; i < temp.length; i++){
    		for(int j = 0; j < temp[i]; j++){
    			arr[step++] = i + min;
    		}
    	}
    }
}