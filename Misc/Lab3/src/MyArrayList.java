/**
 * Partial implementation of generic ArrayList.
 *
 * @author Mark Allen Weiss
 * @author M. Allen
 *
 * @param <T> : type of object stored in list.
 */

public class MyArrayList<T> implements Iterable<T>, SimpleList<T>
{
    // basis of list is an array
    private T[] theItems;
    
    // to track actual number of items stored
    private int theSize;
    
    // initial capacity (size of theItems array)
    private static final int DEFAULT_CAPACITY = 10;
    
    /**
     * Construct an empty ArrayList.
     */
    public MyArrayList()
    {
        clear();
    }
    
    /**
     * Change the size of collection to zero.
     */
    public void clear()
    {
        theSize = 0;
        ensureCapacity( DEFAULT_CAPACITY );
    }
    
    /**
     * Gives the size of the collection.
     *
     * @return the number of items in this collection.
     */
    public int size()
    {
        return theSize;
    }
    
    /**
     * Indicates whether or not collection is empty.
     *
     * @return true if and only if this collection is empty.
     */
    public boolean isEmpty()
    {
        return theSize == 0;
    }
    
    /**
     * Returns the item at a given position in list.
     *
     * @param idx : the index of the object requested.
     *
     * @return Object of type T at given position.
     *
     * @throws IndexOutOfBoundsException if idx is out of range (idx < 0 || idx
     *             >= theSize ).
     */
    public T get( int idx )
    {
        if ( idx < 0 || idx >= theSize )
            throw new IndexOutOfBoundsException( "Index: " + idx +
                                                "; size: " + theSize );
        return theItems[idx];
    }
    
    /**
     * Sets the item at a given position.
     *
     * @param idx the index position at which to place new object.
     * @param newVal the new object to be placed in list.
     *
     * @return the object formerly at the input position.
     *
     * @throws IndexOutOfBoundsException if index is out of range.
     */
    public T set( int idx, T newVal )
    {
        if ( idx < 0 || idx >= theSize )
            throw new IndexOutOfBoundsException( "Index: " + idx +
                                                "; size: " + theSize );
        T old = theItems[idx];
        theItems[idx] = newVal;
        
        return old;
    }
    
    /*
     * Internal method for re-sizing array when needed.
     * post: (theItems.length == newCapacity)
     */
    @SuppressWarnings( "unchecked" )
    private void ensureCapacity( int newCapacity )
    {
        if ( newCapacity < theSize )
            return;
        
        // we cannot instantiate a generic T[] array,
        // so we must instantiate with Object[], then cast
        // (an operation that cannot generally be guaranteed runtime safe,
        // although it is OK here, by construction of the class)
        T[] old = theItems;
        theItems = (T[]) new Object[newCapacity];
        for ( int i = 0; i < theSize; i++ )
            theItems[i] = old[i];
    }
    
    /**
     * Adds an item to this collection, at the end of the current list of items.
     *
     * @param x any object.
     *
     * @return true (if successful, without exception).
     */
    public boolean add( T x )
    {
        // simply call indexed version of add(x)
        add( theSize, x );
        return true;
    }
    
    /**
     * Add element at supplied index.
     *
     * @param idx the index at which to add object.
     * @param x the object to add to collection.
     *
     * @throws IndexOutOfBoundsException if idx out of range.
     */
    public void add( int idx, T x )
    {
        if ( idx < 0 || idx > theSize )
            throw new IndexOutOfBoundsException( "Index: " + idx +
                                                "; size: " + theSize );
        
        // if we have filled the array, we double its size
        // (can be time intensive)
        if ( theItems.length == theSize )
            ensureCapacity( theSize * 2 + 1 );
        
        // if we need to, shuffle elements up in array to fit new element
        // (can be time intensive)
        for ( int i = theSize; i > idx; i-- )
            theItems[i] = theItems[i - 1];
        
        theItems[idx] = x;
        theSize++ ;
    }
    
    /**
     * Adds all elements of a given list to this list.
     *
     * @param ls MyArrayList containing elements of a type that conforms to the
     *            type of this MyArrayList.
     */
    public <E extends T> void addAll(SimpleList<E> ls )
    {
        // NOTE: If we just ran the for loop below and called the method using
        // the same list as both calling object and input parameter, then the
        // loop would be infinite, since we would keep adding to the end of this
        // list, then adding THOSE new things back in AGAIN, etc.
        // This would lead to a heap overflow very quickly!
        //
        // To avoid this, if the input list is this list, we do nothing.
        // NOTE: some Collections in Java do it this way, and others do other
        // things. Some will throw ConcurrentModificationException errors,
        // whereas others, like the java.util.ArrayList, will just create a list
        // that has 2 copies of the original data, back to back.
        // Read the documentation of what a Collection class actually does
        // before trying to run addAll() like this, so you know what to expect!
        if ( ls == this )
            return;
        
        for ( E elt : ls )
            add( elt );
    }
     
    /**
     * Removes item at given index.
     *
     * @param idx the index of the object to remove.
     *
     * @return the item removed from the collection.
     *
     * @throws IndexOutOfBoundsException if idx out of range.
     */
    public T remove( int idx )
    {
        if ( idx < 0 || idx >= theSize )
            throw new IndexOutOfBoundsException( "Index: " + idx +
                                                "; size: " + theSize );
        
        T removedItem = theItems[idx];
        
        // slide each object above removed down one position in array
        for ( int i = idx; i < theSize - 1; i++ )
            theItems[i] = theItems[i + 1];
        theSize-- ;
        
        return removedItem;
    }
    
    /**
     * Indicates whether or not a given object is located in the list.
     *
     * @param element An Object to be searched for in list.
     *
     * @return true if and only if element is included in this list.
     */
    public boolean contains( Object element )
    {
        for ( int i = 0; i < theSize; i++ )
            if ( theItems[i].equals( element ) )
                return true;
        
        return false;
    }
    
    /**
     * Returns index of first occurrence of a given element (-1 if not present).
     *
     * @param element Object to search for (using equals()).
     *
     * @return First index of matching element (-1 if no such element).
     */
    public int indexOf( Object element )
    {
        for ( int i = 0; i < theSize; i++ )
            if ( theItems[i].equals( element ) )
                return i;
        
        return -1;
    }
  
    /**
     * Returns String representation of this object.
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( "[ " );
        for ( T x : this )
            sb.append( x + " " );
        sb.append( "]" );
        
        return new String( sb );
    }
    
    /**
     * Obtains an Iterator<T> object used to traverse the collection.
     */
    @Override
    public java.util.Iterator<T> iterator()
    {
        return new ArrayListIterator();
    }
    
    /**
     * This is the implementation of the ArrayListIterator.
     * It maintains a notion of a current position and of
     * course the implicit reference to the MyArrayList.
     */
    private class ArrayListIterator implements java.util.Iterator<T>
    {
        private int current = 0;
        private boolean okToRemove = false;
        
        /**
         * Returns true if and only if there exist further elements accessible
         * in the list.
         */
        @Override
        public boolean hasNext()
        {
            return current < theSize;
        }
        
        /**
         * Returns the next element in the iteration order, advancing the
         * iterator on one position. Will throw exception where not possible.
         */
        @Override
        public T next()
        {
            if ( !hasNext() )
                throw new java.util.NoSuchElementException();
            
            T item = theItems[current];
            current++ ;
            okToRemove = true;
            return item;
        }
        
        /**
         * Removes the object accessed by the most recent call to next(). Will
         * throw exception if next() has not been called since last remove().
         */
        @Override
        public void remove()
        {
            if ( !okToRemove )
                throw new IllegalStateException( "Cannot remove without advancing Iterator to legal object first." );
            
            current-- ;
            MyArrayList.this.remove( current );
            okToRemove = false;
        }
    }
}