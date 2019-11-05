/**
 * Simple interface for a list-like generic collection.
 * Each such has a subset of the java.util.Collection and java.util.List
 * interfaces, and is Iterable.
 *
 * @author M. Allen
 */
public interface SimpleList<T> extends Iterable<T>
{
    /**
     * Adds an element to the end of the list.
     * Note: this increases size() of list.
     * 
     * @param x Element to add to the list.
     * 
     * @return true iff add() is performed successfully.
     */
    public boolean add( T x );

    /**
     * Adds a new element to list, with given value.
     * Given location must be in bounds: 0 <= idx <= (size - 1).
     * Note: this increases size() of list.
     * 
     * @param idx Index at which new element will be placed.
     * @param x Element to add to the list.
     */
    public void add( int idx, T x );

    /**
     * Empties the list.
     */
    public void clear();

    /**
     * Searches list for given object.
     * 
     * @param element An Object to be searched for in list; does not need to be
     *            of any particular conformant type. Method must be able to
     *            handle any type given.
     * 
     * @return true iff the element is contained at least once in list.
     */
    public boolean contains( Object element );

    /**
     * Returns an element from list.
     * Given location must be in bounds: 0 <= idx <= (size - 1).
     * Note: this does not change size() of list at all.
     * 
     * @param idx Index of element to return.
     * 
     * @return List element at given index.
     */
    public T get( int idx );

    /**
     * Searches list for given object, and returns index of first occurrence.
     * 
     * @param element An Object to be searched for in list; does not need to be
     *            of any particular conformant type. Method must be able to
     *            handle any type given.
     * 
     * @return The index of the first occurrence of the given element in the
     *         list (this may or may not be the only occurrence). This value is
     *         -1 if the element is not contained in the list, and means that
     *         contains(element) == false.
     */
    public int indexOf( Object element );

    /**
     * Determines if list contains any data.
     * 
     * @return true iff list is empty.
     */
    public boolean isEmpty();

    /**
     * Removes and returns an element from list.
     * Given location must be in bounds: 0 <= idx <= (size - 1).
     * Note: this reduces size() of list.
     * 
     * @param idx Index of element to remove and return.
     * 
     * @return The element removed from given position.
     */
    public T remove( int idx );

    /**
     * Sets an existing element of list to given value.
     * Given location must be in bounds: 0 <= idx <= (size - 1).
     * Note: this does not change size() of list.
     * 
     * @param idx Index of element to change in list.
     * @param newVal New value to insert at given index.
     * 
     * @return The old element contained at given index.
     */
    public T set( int idx, T newVal );

    /**
     * Gives size of list-like structure.
     *
     * @return Size of list-like structure.
     */
    public int size();
}
