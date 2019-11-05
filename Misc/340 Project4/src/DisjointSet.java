/**
 * Implements a version of the disjoint-set structure (also sometimes called a
 * Union-Find structure). This allows us to relatively efficiently keep track of
 * a collection of different data-objects.
 *
 * Each object is contained in some subset of the overall set of objects,
 * represented as a tree. We can check if two objects are in the checking
 * whether the roots of the trees to which they belong are the same (and so they
 * are in the same tree/subset). We also implement relatively efficient ways of
 * checking the size of sets, creating unions of two subsets into a common one,
 * etc., while implementing "path compression," which means that any time we
 * search for the root of a tree, we re-structure the tree so that any nodes
 * between the starting point and the root are all now directly connected to the
 * root as children, meaning that another search for the root of the same object
 * can be done in constant time, O(1).
 *
 * @author M. Allen
 *
 * @param <T> The type of the data elements contained by the structure.
 */
import java.util.HashMap;
import java.util.NoSuchElementException;

public class DisjointSet<T>
{
    // Each node in the overall set of subsets will also be accessible via a
    // hashmap, allowing us to find nodes before doing other operations.
	private HashMap<T, Node> nodes = new HashMap<>();
    
    // The number of distinct sets in the overall collection.
    private int numSets = 0;
    
    /**
     * Returns overall size of collection of subsets.
     *
     * @return Number of data-elements in collection.
     */
    public int size()
    {
        return nodes.size();
    }
    
    /**
     * Returns number of sets contained by collection.
     *
     * @return Number of distinct subsets in collection.
     */
    public int numSets()
    {
        return numSets;
    }
    
    /**
     * Adds a given data element to the set of subsets.
     * Each new element added starts off in a set by itself.
     * Enforces uniqueness: objects only occur at most once.
     *
     * @param obj Data of type T to be added to collection.
     */
    public void add( T obj )
    {
        // Any distinct object causes creation of a new tree-set.
        // We count any such sets as we go.
        if (!contains( obj ))
        {
            nodes.put(obj, new Node(obj));
            numSets++;
        }
    }
    
    /**
     * Checks for existence of an object in collection.
     *
     * @param obj Data of type T to be look for in collection.
     *
     * @return true if and only if obj is in some set in collection.
     */
    public boolean contains( T obj )
    {
        if(nodes.containsKey(obj)){
        	return true;
        }
        return false;
    }
    
    /**
     * Identifies the set to which an element belongs (if any such).
     * Throws NoSuchElementException if object not part of collection.
     *
     * @param obj Data of type T to look for in collection.
     *
     * @return Data of type T that is the "representative" for the set
     *         containing input obj.
     */
    public T getSet( T obj )
    {
    	Node temp = nodes.get(obj);
    	if(temp!=null){
    		return getRoot(temp).data;
    	}
        // If here, no such data object; throw exception.
        throw new NoSuchElementException( "Sets do not contain object: " + obj );
    }
    
    /**
     * Identifies whether or not two elements are in the same set together.
     * Throws NoSuchElementException if either or both objects are not part of
     * the collection.
     *
     * @param obj1 Data of type T to look for in collection.
     * @param obj2 Data of type T to look for in collection.
     *
     * @return true if and only if obj1 and obj2 are both in the same subset.
     *         That is, inSameSet( x, y ) == getSet( x ).equals( getSet( y ) ).
     *         This is always true if x.equals( y )
     */
    public boolean inSameSet( T obj1, T obj2 )
    {
        // Find the root of each object; if either is not contained, the root
        // value will be null, and we throw an exception.
        Node root1 = getRoot(nodes.get(obj1));
        Node root2 = getRoot(nodes.get(obj2));
        if ( root1 == null && root2 == null )
            throw new NoSuchElementException( "Sets do not contain either object: " + obj1 + ", " + obj2 );
        if ( root1 == null )
            throw new NoSuchElementException( "Sets do not contain object: " + obj1 );
        if ( root2 == null )
            throw new NoSuchElementException( "Sets do not contain object: " + obj2 );
        // If here, then have non-null root for each input; check if same Node.
        return root1 == root2;
    }
    
    /**
     * Identifies size of subset containing some data element.
     * Throws NoSuchElementException if object not part of collection.
     *
     * @param obj Data of type T to look for in collection.
     *
     * @return Size of subset containing input obj.
     */
    public int sizeOfSet( T obj )
    {
    	Node temp = nodes.get(obj);
    	if(temp!=null){
    		return getRoot(temp).size;
    	}
    	
        // If here, no such such data object; throw exception.
        throw new NoSuchElementException( "Sets do not contain object: " + obj );
    }
    
    /**
     * Forms the union of the sets containing input data-values.
     * Throws NoSuchElementException if either or both objects are not part of
     * the collection.
     *
     * @param obj1 Data of type T to look for in collection.
     * @param obj2 Data of type T to look for in collection.
     *
     * @return The data-value that is "representative" of the union of the
     *         original two sets containing the input data. If these were
     *         already in the same set, then returns the common "representative"
     *         they already shared.
     */
    public T union( T obj1, T obj2 )
    {
        // Find the root of each object; if either is not contained, the root
        // value will be null, and we throw an exception.
        Node root1 = getRoot(nodes.get(obj1));
        Node root2 = getRoot(nodes.get(obj2));
        if ( root1 == null && root2 == null )
            throw new NoSuchElementException( "Sets do not contain either object: " + obj1 + ", " + obj2 );
        if ( root1 == null )
            throw new NoSuchElementException( "Sets do not contain object: " + obj1 );
        if ( root2 == null )
            throw new NoSuchElementException( "Sets do not contain object: " + obj2 );
        // If already were in same set, just return data from the root of that
        // set.
        if ( root1 == root2 )
            return root1.data;
        // If not already, then doing union reduces overall number of sets.
        numSets-- ;
        // We use root2 as the new parent if either (a) the trees containing
        // both inputs have same rank and a "coin toss" settles on this case,
        // or (b) the tree containing obj1 has lower rank.
        if ( ( root1.rank == root2.rank && Math.random() < 0.5 ) || root1.rank < root2.rank )
        {
            // When we link root1 to root2, size of set under root2 inreases.
            root1.parent = root2;
            root2.size += root1.size;
            
            // When we union two sets of same rank, new root gets higher rank.
            if ( root1.rank == root2.rank )
                root2.rank++ ;
            
            return root2.data;
        }
        else
            // We use root1 as the new parent if either (a) the trees containing
            // both inputs have same rank and a "coin toss" settles on this
            // case, or (b) the tree containing obj2 has lower rank.
        {
            // When we link root1 to root2, size of set under root2 inreases.
            root2.parent = root1;
            root1.size += root2.size;
            
            // When we union two sets of same rank, new root gets higher rank.
            if ( root1.rank == root2.rank )
                root1.rank++ ;
            
            return root1.data;
        }
    }
    
    /**
     * Utility method that returns the Node that is at root of any tree-set
     * containing input Node. As a side-effect, does path compression, so that
     * on any call, all nodes between input node and root are made into direct
     * children of root. This will mean that on two subsequent calls to the same
     * method, for the same input (without any new union() operations), the
     * first loop to find the root will be O(n), but the second will be O(1).
     *
     * @param node Node in a tree-set (will also be in list of all Nodes).
     *
     * @return Root node of tree-set containing input node; may be that node
     *         itself, if it is already root.
     */
    private Node getRoot( Node node )
    {
        // Base case: we are at root already. Simply return node.
        if (node.parent.equals(node))
            return node;
        // Recursive case: not at root, then find root, linking everything to
        // that root recursively on the way back. (This line does the path
        // compression part of the whole process.) When done, can return parent,
        // since that will now be overall tree root, wherever we started.
        node.parent = getRoot( node.parent );
        return node.parent;
    }
    
    /**
     * An inner private class for holding data elements; allows the
     * tree-structures of each set to be built, alongside the list of all nodes.
     * 
     * Picks up the data-type T from the containing class instance.
     */
    private class Node
    {
        // Node stores data and bottom-up link to its immediate parent.
        private T data;
        private Node parent;
        
        // Node also stores rank in tree-set (an upper bound on the height of
        // the tree below it for which it is the root); initially rank == 0.
        private int rank = 0;
        
        // Finally, Node stores size of subset for which it is the root;
        // initially size == 1.
        private int size = 1;
        
        /**
         * Each node created stores data, and has self as parent.
         * 
         * @param data_ Data of type T to store in node.
         */
        private Node( T data_ )
        {
            data = data_;
            parent = this;
        }
    }
}
