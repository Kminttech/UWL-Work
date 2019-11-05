/**
 * Implementation of Shell sort, using Shell's original
 * increments (1/2 array length) -> 1/4 -> 1/8 -> ... -> 1.
 *
 * To be modified: implement Hibbard's sorting increments, which produce a more
 * efficient worst-case and average-case run-time for the sorting.
 *
 * @author M. Allen
 * @author YOUR NAME HERE
 */

public class Shell
{
    public static <T extends Comparable<? super T>> void sort( T[] arr )
    {
    	int k = (int)(Math.log(arr.length) / Math.log(2));
        // start with 1/2 arr.length -> 1/4 -> 1/8 -> ... -> 1
        for ( int gap = (int)Math.pow(2, k) - 1; gap > 0; gap /= 2 )
        {
            // sort all elements from the initial point to the end of the array
            for ( int i = gap; i < arr.length; i++ )
            {
                // current element to insert
                T tmp = arr[i];
                // loop downward, comparing every element that is <gap> places
                // apart
                // (until below gap or have found a place to insert into)
                int j;
                for ( j = i; j >= gap && tmp.compareTo( arr[j - gap] ) < 0; j -= gap )
                    arr[j] = arr[j - gap];
                // insert the original element at new gap-sorted location
                arr[j] = tmp;
            }
        }
    }
}
