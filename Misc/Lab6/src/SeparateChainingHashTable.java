
/**
 * Separate chaining table implementation of hash tables.
 * Note that all "matching" is based on the equals method.
 *
 * @author Mark Allen Weiss
 * @author M. Allen
 */

import java.util.LinkedList;

public class SeparateChainingHashTable<T>
{
    private static final int DEFAULT_TABLE_SIZE = 101;
    
    /** The array of Lists. */
    private LinkedList<T>[] theLists;
    private int currentSize;
    
    /**
     * Construct the hash table.
     */
    public SeparateChainingHashTable()
    {
        this( DEFAULT_TABLE_SIZE );
    }
    
    /**
     * Construct the hash table.
     *
     * @param size approximate table size.
     */
    @SuppressWarnings( "unchecked" )
    public SeparateChainingHashTable( int size )
    {
        theLists = new LinkedList[nextPrime( size )];
        for ( int i = 0; i < theLists.length; i++ )
            theLists[i] = new LinkedList<T>();
    }
    
    /**
     * Insert into the hash table. If the item is
     * already present, then do nothing.
     *
     * @param x the item to insert.
     */
    public void insert( T x )
    {
        LinkedList<T> whichList = theLists[myhash( x )];
        if ( !whichList.contains( x ) )
        {
            whichList.add( x );
            currentSize++ ;
            
            // Rehash; see Section 5.5
            if ( currentSize > theLists.length )
                rehash();
        }
    }
    
    /**
     * Remove from the hash table.
     *
     * @param x the item to remove.
     */
    public void remove( T x )
    {
        LinkedList<T> whichList = theLists[myhash( x )];
        boolean removed = whichList.remove( x );
        if ( removed )
            currentSize-- ;
    }
    
    /**
     * Find an item in the hash table.
     *
     * @param x the item to search for.
     *
     * @return true if x is found.
     */
    public boolean contains( T x )
    {
        LinkedList<T> whichList = theLists[myhash( x )];
        return whichList.contains( x );
    }
    
    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty()
    {
        for ( int i = 0; i < theLists.length; i++ )
            theLists[i].clear();
        currentSize = 0;
    }
    
    /**
     * Return the size of the table.
     *
     * @return the size of the table.
     */
    public int size()
    {
        return currentSize;
    }
    
    /*
     * Internal method to rehash the table if needed,
     * making it larger as necessary.
     */
    @SuppressWarnings( "unchecked" )
    private void rehash()
    {
        LinkedList<T>[] oldLists = theLists;
        
        // Create new double-sized, empty table
        theLists = new LinkedList[nextPrime( 2 * theLists.length )];
        for ( int j = 0; j < theLists.length; j++ )
            theLists[j] = new LinkedList<T>();
        
        // Copy table over
        currentSize = 0;
        for ( int i = 0; i < oldLists.length; i++ )
            for ( T item : oldLists[i] )
                insert( item );
    }
    
    /*
     * Internal method to generate hash index to table.
     *
     * @param x Object of table type T.
     *
     * @return Hash index of object.
     */
    private int myhash( T x )
    {
        // uses Object x's own hashCode()
        int hashVal = x.hashCode();
        
        // adjust to fit into array properly
        hashVal = hashVal % theLists.length;
        if ( hashVal < 0 )   // hashVal's can be negative
            hashVal += theLists.length;
        
        return hashVal;
    }
    
    /*
     * Internal method to find a prime number at least as large as n.
     *
     * @param n Starting number (must be positive).
     *
     * @return Prime number larger than or equal to n.
     */
    private static int nextPrime( int n )
    {
        if ( n < 2 )
            return 2;
        
        if ( n < 3 )
            return 3;
        
        int p = 0;
        int[] primes = new int[n];
        primes[0] = 2;
        primes[1] = 3;
        
        // generate all primes from 5 to first prime >= p
        for ( int i = 2; i < primes.length; i++ )
        {
            p = primes[i - 1];
            boolean isPrime = false;
            
            while ( !isPrime )
            {
                p += 2;
                double sqrt = Math.sqrt( p );
                isPrime = true;
                int j = 1;
                while ( j < i && primes[j] <= sqrt )
                {
                    if ( p % primes[j] == 0 )
                    {
                        isPrime = false;
                        j = i;
                    }
                    j++ ;
                }
            }
            primes[i] = p;
            
            if ( p >= n )
                return p;
        }
        return p;
    }
    
    public void printTable(){
    	for(int i = 0; i < theLists.length; i++){
    		if(!theLists[i].isEmpty()){
    			System.out.println("Hash index: " + i);
    			for(T temp:theLists[i]){
    				System.out.println(temp.toString());
    			}
    		}
    	}
    }
}
