/**
 * Generic Binary Search Tree (BST) class.
 * Implements an unbalanced binary search tree in which duplicates are NOT allowed.
 * (Note that all "matching" is based on the compareTo() of the data objects.)
 *
 * @author Mark Allen Weiss.
 * @author M. Allen.
 *
 * @param <T> Type of object to place in tree; must be appropriately Comparable
 *            to allow the sorting of data for efficient search purposes.
 */

public class BinarySearchTree<T extends Comparable<? super T>>
{
    // The tree root.
    private BinaryNode<T> root;
    
    /**
     * Construct the tree with null root.
     */
    public BinarySearchTree()
    {
        root = null;
    }
    
    /**
     * Insert into the tree; duplicates are ignored.
     *
     * @param x the item to insert.
     */
    public void insert( T x )
    {
        root = insert( x, root );
    }
    
    /**
     * Remove from the tree. Nothing is done if x is not found.
     *
     * @param x the item to remove.
     */
    public void remove( T x )
    {
        root = remove( x, root );
    }
    
    /**
     * Return the least element in the tree, in natural order.
     *
     * @return Least data item (null if empty).
     */
    public T findMin()
    {
        if ( isEmpty() )
            return null;
        
        return findMin( root ).element;
    }
    
    /**
     * Return the maximum element in the tree, in natural order.
     *
     * @return Maximum data item (null if empty).
     */
    public T findMax()
    {
        if ( isEmpty() )
            return null;
        
        return findMax( root ).element;
    }
    
    /**
     * Find an item in the tree.
     *
     * @param x Item to search for.
     *
     * @return true if and only if item found.
     */
    public boolean contains( T x )
    {
        return contains( x, root );
    }
    
    /**
     * Make the tree logically empty.
     */
    public void makeEmpty()
    {
        root = null;
    }
    
    /**
     * Test if the tree is logically empty.
     *
     * @return true if and only if empty.
     */
    public boolean isEmpty()
    {
        return root == null;
    }
    
    /**
     * Determine height of tree.
     *
     * @return Longest path-length from root to any leaf node; -1 if empty.
     */
    public int height()
    {
        return height( root );
    }
    
    /**
     * Print the tree contents in natural order.
     */
    public void printTree()
    {
        if ( isEmpty() )
            System.out.println( "<EMPTY>" );
        else
        {
            StringBuilder builder = new StringBuilder();
            printTree( root, builder );
            System.out.println( builder.toString() );
        }
    }
    
    /*
     * Internal method to insert into a subtree.
     *
     * @param x Item to insert.
     *
     * @param t Node that roots the subtree.
     *
     * @return New root of the subtree.
     */
    private BinaryNode<T> insert( T x, BinaryNode<T> t )
    {
        if ( t == null )
            return new BinaryNode<T>( x, null, null );
        
        int compareResult = x.compareTo( t.element );
        
        if ( compareResult < 0 )
            t.left = insert( x, t.left );
        else if ( compareResult > 0 )
            t.right = insert( x, t.right );
        
        // If here, then x is a duplicate item;
        // simply return original tree
        return t;
    }
    
    /*
     * Internal method to remove from a subtree.
     *
     * @param x Item to remove.
     *
     * @param t Node that roots a subtree.
     *
     * @return New root of the subtree.
     */
    private BinaryNode<T> remove( T x, BinaryNode<T> t )
    {
        // if item not found, return null
        if ( t == null )
            return t;
        
        int compareResult = x.compareTo( t.element );
        if ( compareResult < 0 )
        {
            // element x is less than subtree root, so
            // recursively remove x on the left side
            t.left = remove( x, t.left );
        }
        else if ( compareResult > 0 )
        {
            // element x is greater than subtree root, so
            // recursively remove x on the right side
            t.right = remove( x, t.right );
        }
        else if ( t.left != null && t.right != null )
        {
            // root element equals x, root has 2 children
            t.element = findMin( t.right ).element;
            t.right = remove( t.element, t.right );
        }
        else if ( t.left != null )
        {
            // root element equals x, has only left child
            t = t.left;
        }
        else
        {
            // root element equals x, has only right child
            t = t.right;
        }
        
        // return subtree root (t) when done
        return t;
    }
    
    /*
     * Internal method to find the least item in a subtree.
     *
     * @param t Node that roots the subtree.
     *
     * @return Node containing the least item.
     */
    private BinaryNode<T> findMin( BinaryNode<T> t )
    {
        if ( t == null )
            return null;
        else if ( t.left == null )
            return t;
        return findMin( t.left );
    }
    
    /*
     * Internal method to find the maximum item in a subtree.
     *
     * @param t Node that roots the subtree.
     *
     * @return Node containing the maximum item.
     */
    private BinaryNode<T> findMax( BinaryNode<T> t )
    {
        if ( t != null )
            while ( t.right != null )
                t = t.right;
        
        return t;
    }
    
    /*
     * Internal method to find an item in a subtree.
     *
     * @param x Item to search for.
     *
     * @param t Node that roots the subtree.
     *
     * @return true if and only if x is contained in subtree rooted at t.
     */
    private boolean contains( T x, BinaryNode<T> t )
    {
        if ( t == null )
            return false;
        
        int compareResult = x.compareTo( t.element );
        
        if ( compareResult < 0 )
            return contains( x, t.left );
        else if ( compareResult > 0 )
            return contains( x, t.right );
        else
            return true;
    }
    
    /**
     * Internal recursive method to compute height of a subtree.
     *
     * @param t the node that roots the subtree.
     */
    private int height( BinaryNode<T> t )
    {
        if ( t == null )
            return -1;
        else
            return 1 + Math.max( height( t.left ), height( t.right ) );
    }
    
    /*
     * Internal method to build a representation of tree in sorted order,
     * using an in-order recursive traversal. When complete, the passed
     * StringBuilder will contain representation of the tree.
     *
     * @param t Node that roots the subtree.
     *
     * @param builder StringBuilder to construct String
     * (much more efficient than concatenation (+) on large data-sets).
     */
    private void printTree( BinaryNode<T> t, StringBuilder builder )
    {
        if ( t != null )
        {
            printTree( t.left, builder );
            builder.append( t.element );
            builder.append( " " );
            printTree( t.right, builder );
        }
    }
    
    /*
     * Private static class for tree nodes.
     * Each contains a data element, and
     * links to right- and left-subtrees.
     */
    private static class BinaryNode<T>
    {
        
        private T element;
        private BinaryNode<T> left;
        private BinaryNode<T> right;
        
        private BinaryNode( T theElement, BinaryNode<T> lt, BinaryNode<T> rt )
        {
            element = theElement;
            left = lt;
            right = rt;
        }
    }
    
}
