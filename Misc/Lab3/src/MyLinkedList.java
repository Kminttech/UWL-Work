/**
 * Linked List class with elementary methods.
 *
 * @author Mark Allen Weiss
 * @author M. Allen
 *
 * @param <T> Type of data collection
 */

public class MyLinkedList<T> implements Iterable<T>, SimpleList<T>
{
    private int theSize;
    private Node<T> head;
    private Node<T> tail;
    
    /**
     * Construct an empty LinkedList.
     */
    public MyLinkedList()
    {
        clear();
    }
    
    /**
     * Change the size of this collection to zero.
     * Allows garbage-collection of any prior data-nodes.
     */
    public void clear()
    {
        head = new Node<T>( null, null, null );
        tail = new Node<T>( null, head, null );
        head.next = tail;
        
        theSize = 0;
    }
    
    /**
     * Returns the size of this collection.
     *
     * @return the number of items in this collection.
     */
    public int size()
    {
        return theSize;
    }
    
    /**
     * Indiates if list is empty.
     *
     * @return true iff list is empty.
     */
    public boolean isEmpty()
    {
        return theSize == 0;
    }
    
    /**
     * Adds an item to this collection, at the end of the list order.
     *
     * @param x any object of type conformant to collection type.
     *
     * @return true.
     */
    public boolean add( T x )
    {
        add( theSize, x );
        return true;
    }
    
    /**
     * Adds an item to this collection, at specified position.
     *
     * @param x any object of type conformant to collection type.
     * @param idx position at which to add the new object.
     *
     * @throws IndexOutOfBoundsException if ( idx < 0 ) (or idx > theSize ).
     */
    public void add( int idx, T x )
    {
        addBefore( getNode( idx, 0, theSize ), x );
    }
    
    /**
     * Utility method to add an object into list order before a given node.
     */
    private void addBefore( Node<T> p, T x )
    {
        Node<T> newNode = new Node<T>( x, p.prev, p );
        newNode.prev.next = newNode;
        p.prev = newNode;
        theSize++ ;
    }
    
    /**
     * Returns the item at a given position.
     *
     * @param idx the index at which to look for the object.
     *
     * @return Object of collection type from given index position.
     *
     * @throws IndexOutOfBoundsException if index is out of range.
     */
    public T get( int idx )
    {
        return getNode( idx ).data;
    }
    
    /**
     * Changes the item at a given position.
     *
     * @param idx the index at which to change an object.
     * @param newVal a new object of conformant type to collection.
     *
     * @return a reference to the object formerly at given index.
     *
     * @throws IndexOutOfBoundsException if index is out of range.
     */
    public T set( int idx, T newVal )
    {
        Node<T> p = getNode( idx );
        T oldVal = p.data;
        
        p.data = newVal;
        return oldVal;
    }
    
    /**
     * Utility method used to access a particular list data-node.
     */
    private Node<T> getNode( int idx )
    {
        return getNode( idx, 0, theSize - 1 );
    }
    
    /**
     * Accesses Node at given position, within bounds.
     *
     * @param idx index at which to access object.
     * @param lower lowest valid index.
     * @param upper highest valid index.
     *
     * @return internal node corresponding to input index.
     *
     * @throws IndexOutOfBoundsException if ( idx < lower ) or ( idx > upper ).
     */
    private Node<T> getNode( int idx, int lower, int upper )
    {
        Node<T> p;
        
        if ( idx < lower || idx > upper )
            throw new IndexOutOfBoundsException( "getNode index: " + idx +
                                                "; size: " + theSize );
        
        // optimization to cut expected search time in half: start from end of
        // list that is numerically closest to desired object index
        if ( idx < theSize / 2 )
        {
            p = head.next;
            for ( int i = 0; i < idx; i++ )
                p = p.next;
        }
        else
        {
            p = tail;
            for ( int i = theSize; i > idx; i-- )
                p = p.prev;
        }
        
        return p;
    }
    
    /**
     * Removes an item from this collection.
     *
     * @param idx the index of the object to remove.
     *
     * @return the item was removed from the collection.
     *
     * @throws IndexOutOfBoundsException if index is out of range.
     */
    public T remove( int idx )
    {
        return remove( getNode( idx ) );
    }
    
    /**
     * Utility method to remove a data-node and return its data.
     */
    private T remove( Node<T> p )
    {
        p.next.prev = p.prev;
        p.prev.next = p.next;
        p.next = null;
        p.prev = null;
        theSize-- ;
        
        return p.data;
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
        return indexOf( element ) != -1;
    }
    
    /**
     * Returns index of given element, if present.
     *
     * @param Object for which to search.
     *
     * @return index of that item (or -1 if not present)
     */
    public int indexOf( Object element )
    {
        java.util.Iterator<T> iter = iterator();
        int count = 0;
        
        while ( iter.hasNext() )
        {
            if ( iter.next().equals( element ) )
                return count;
            
            count++ ;
        }
        
        return -1;
    }
    
    /**
     * Generates Object array containing contents of this list.
     *
     * @return Object[] containing elements from this list.
     */
    public Object[] toArray()
    {
        Object[] items = new Object[theSize];
        
        int i = 0;
        for ( T element : this )
        {
            items[i] = element;
            i++ ;
        }
        return items;
    }
    
    /**
     * Returns array of type matching that of this list, containing its
     * elements.
     *
     * @param a an array of type T[] conformant with type of this.
     *
     * @return array of type identical to that of input array, containing
     *         elements from this.
     */
    @SuppressWarnings( "unchecked" )
    public T[] toArray( T[] a )
    {
        if ( a.length < theSize )
            a = (T[]) java.lang.reflect.Array.newInstance( a.getClass().getComponentType(),
                                                          theSize );
        
        int i = 0;
        for ( T element : this )
        {
            a[i] = element;
            i++ ;
        }
        
        if ( a.length > theSize )
            a[theSize] = null;
        
        return a;
    }
    
    /**
     * Returns a String representation of this collection.
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder( "[ " );
        
        for ( T x : this )
            sb.append( x + " " );
        sb.append( "]" );
        
        return new String( sb );
    }
    
    public <E extends T> void addAll(SimpleList<E> ls){
    	if ( ls == this )
            return;
        for ( E elt : ls )
            add( elt );
    }
    
    /**
     * Obtains an Iterator object used to traverse the collection.
     * @return an iterator positioned prior to the first element.
     */
    public java.util.Iterator<T> iterator()
    {
        return new LinkedListIterator();
    }
    
    /**
     * This is the implementation of the LinkedListIterator.
     * It maintains a notion of a current position and of
     * course the implicit reference to the MyLinkedList.
     */
    private class LinkedListIterator implements java.util.Iterator<T>
    {
        // when created, the initial position is immediately after the head
        // sentinel node in list order
        private Node<T> current = head.next;
        private boolean okToRemove = false;
        
        /**
         * Returns true if and only if have not reached tail sentinel.
         */
        public boolean hasNext()
        {
            return current != tail;
        }
        
        /**
         * Iterates to next node (if exits).
         * Throws NoSuchElementException if at tail sentinel.
         */
        public T next()
        {
            if ( !hasNext() )
                throw new java.util.NoSuchElementException();
            
            T nextItem = current.data;
            current = current.next;
            okToRemove = true;
            return nextItem;
        }
        
        /**
         * Removes element returned by immediately prior call to next().
         * Throws IllegalStateException if called without a successful next()
         * immediately prior (i.e., cannot be called twice in a row).
         */
        public void remove()
        {
            if ( !okToRemove )
                throw new IllegalStateException();
            
            MyLinkedList.this.remove( current.prev );
            okToRemove = false;
        }
    }
    
    /**
     * This is the doubly-linked list node.
     */
    private static class Node<T>
    {
        private T data;
        private Node<T> prev;
        private Node<T> next;
        
        /**
         * Constructs basic list node.
         *
         * @param d The data element held by the list node.
         * @param p A Node that occurs immediately before this in list order.
         * @param n A node that occurs immediately after this in list order.
         */
        private Node( T d, Node<T> p, Node<T> n )
        {
            data = d;
            prev = p;
            next = n;
        }
    }
}