/**
 * Implements a binary heap.
 * Note that all "matching" is based on the compareTo() method.
 *
 * @author Mark Allen Weiss
 * @author M. Allen
 */

public class BinaryHeap<T extends Comparable<? super T>>
{
    private static final int DEFAULT_CAPACITY = 10;
    
    private int currentSize;      // Number of elements in heap
    private T[] array; // The heap array
    
    /**
     * Construct empty binary heap with default capacity.
     */
    public BinaryHeap()
    {
        this( DEFAULT_CAPACITY );
    }
    
    /**
     * Construct the binary heap with given capacity.
     *
     * @param capacity Capacity of the binary heap.
     */
    @SuppressWarnings( "unchecked" )
    public BinaryHeap( int capacity )
    {
        currentSize = 0;
        array = (T[]) new Comparable[capacity + 1];
    }
    
    /**
     * Construct the binary heap given an array of items.
     *
     * @param items Array of items to use as initial base of heap.
     */
    @SuppressWarnings( "unchecked" )
    public BinaryHeap( T[] items )
    {
        currentSize = items.length;
        // to avoid immediate resizing, we set the actual array to be
        // slightly larger than is absolutely needed for our elements
        array = (T[]) new Comparable[( currentSize + 2 ) * 11 / 10];
        
        // the "root node" at index 0 in array is always kept empty,
        // so actual data begins with index == 1
        int i = 1;
        for ( T item : items )
        {
            array[i] = item;
            i++ ;
        }
        // since items in array are not necessarily sorted at all, we run the
        // utility method buildHeap() to re-order as needed
        buildHeap();
    }
    
    /**
     * Insert into the priority queue, maintaining heap order.
     * Duplicates are allowed.
     *
     * @param x Item to insert.
     */
    public void insert( T x )
    {
        // when extra space is needed, we double the array size
        if ( currentSize == array.length - 1 )
            enlargeArray( array.length * 2 + 1 );
        
        // Percolate up
        currentSize++ ;
        int hole = currentSize;
        while ( hole > 1 && x.compareTo( array[hole / 2] ) < 0 )
        {
            array[hole] = array[hole / 2];
            hole /= 2;
        }
        array[hole] = x;
    }
    
    /* Inner utility method to re-size base array when needed. */
    @SuppressWarnings( "unchecked" )
    private void enlargeArray( int newSize )
    {
        T[] old = array;
        array = (T[]) new Comparable[newSize];
        for ( int i = 0; i < old.length; i++ )
            array[i] = old[i];
    }
    
    /**
     * Find the smallest item (first in priority order) in the queue.
     *
     * @return The smallest item, or null if empty.
     */
    public T findMin()
    {
        if ( isEmpty() )
            return null;
        
        // the least element is always the starting data element in array
        return array[1];
    }
    
    /**
     * Remove the smallest item (first in priority order) from the queue.
     *
     * @return The smallest item, or null if empty.
     */
    public T deleteMin()
    {
        if ( isEmpty() )
            return null;
        
        T minItem = array[1];
        // simply move the last thing added to replace the root item, and then
        // percolate down until it reaches the appropriate spot in the tree
        array[1] = array[currentSize];
        currentSize-- ;
        percolateDown( 1 );
        
        return minItem;
    }
    
    /**
     * Establish heap order property from an arbitrary
     * arrangement of items. Runs in linear time.
     */
    private void buildHeap()
    {
        // we can start at (currentSize / 2), since everything "below" that
        // point is a leaf, and will not need to be moved down on its own
        for ( int i = currentSize / 2; i > 0; i-- )
            percolateDown( i );
    }
    
    /**
     * Returns the number of elements in the heap.
     *
     * @return Number of elements in the heap.
     */
    public int size()
    {
        return currentSize;
    }
    
    /**
     * Test if the priority queue is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty()
    {
        return currentSize == 0;
    }
    
    /**
     * Make the priority queue logically empty.
     */
    public void makeEmpty()
    {
        currentSize = 0;
    }
    
    /**
     * Internal method to percolate down in the heap.
     *
     * @param hole Index at which the percolation begins.
     */
    private void percolateDown( int hole )
    {
        int child;
        T tmp = array[hole];
        
        while ( hole * 2 <= currentSize )
        {
            // jump to point in array where children of index hole begin
            child = hole * 2;
            
            // find least child (first in priority order)
            if ( child != currentSize &&
                array[child + 1].compareTo( array[child] ) < 0 )
                child++ ;
            
            // if least child has lower priority, we move the child up to the
            // parent's location (at the index hole), and repeat the process for
            // the next level down (children of the child, and so on)
            if ( array[child].compareTo( tmp ) < 0 )
            {
                array[hole] = array[child];
                hole = child;
            }
            else
            {
                // if the least child has higher priority, then there is no need
                // to continue, as hole index is now in proper location
                array[hole] = tmp;
                return;
            }
        }
        // this last clause still needed for cases where we get to end of array
        // in while loop, having found no smaller (higher-priority) child, and
        // the object is inserted at the final active position in
        // the data array (hole == currentSize)
        array[hole] = tmp;
    }
}
