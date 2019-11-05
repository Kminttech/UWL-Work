/**
 * Simple list interface. This interface must be implemented by a generic list
 * type used to store characters during text analysis.
 *
 * @author M. Allen
 */
import java.util.NoSuchElementException;

public interface RandomizedList<T>
{
    /**
     * Adds a single element to the list.
     * 
     * @param input Object of type conformant with that of list itself.
     */
    public void add( T input );

    /**
     * Returns size of list.
     * 
     * @return Number of elements in list.
     */
    public int size();

    /**
     * Generates a randomly selected element of the list.
     * 
     * @return Some element of the list, chosen uniformly at random.
     * 
     * @throws NoSuchElementException if this list is empty.
     */
    public T randomElement() throws NoSuchElementException;
}
