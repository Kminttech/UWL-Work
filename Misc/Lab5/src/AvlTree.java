/**
 * Implements an AVL tree without duplicates. Rotations are used to return the
 * tree to balance if necessary.
 *
 * Note that all equality and ordering of elements is based on the compareTo()
 * method provided by the Comparable elements themselves.
 *
 * @author Mark Allen Weiss
 * @author Martin Allen
 *
 * @param <T> Data type of tree elements; must be Comparable for natural
 *            ordering to apply.
 */

public class AvlTree<T extends Comparable<? super T>>
{
    private AvlNode<T> root;
    
    /**
     * Construct the tree with null root.
     */
    public AvlTree()
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
     * @return true if and only if x is found.
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
     * Determines whether or not tree is empty.
     *
     * @return true if and only if the tree contains no elements.
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
        return root.height;
    }
    
    /**
     * Determine number of tree-nodes (and data elements) in tree.
     *
     * @return Size of tree in nodes; 0 if empty.
     */
    public int size()
    {
        return size( root );
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
    private AvlNode<T> insert( T x, AvlNode<T> t )
    {
        if ( t == null )
            return new AvlNode<T>( x, null, null );
        
        int compareResult = x.compareTo( t.element );
        if ( compareResult < 0 )
        {
            // insert on the left (x < root element)
            t.left = insert( x, t.left );
            if ( height( t.left ) - height( t.right ) == 2 )
            {
                // unbalanced tree: single (LL) or double (LR) rotation
                if ( x.compareTo( t.left.element ) < 0 )
                    t = rotateWithLeftChild( t );
                else
                    t = doubleWithLeftChild( t );
            }
        }
        else if ( compareResult > 0 )
        {
            // insert on the right (x > root element)
            t.right = insert( x, t.right );
            if ( height( t.right ) - height( t.left ) == 2 )
            {
                // unbalanced tree: single (RR) or double (RL) rotation
                if ( x.compareTo( t.right.element ) > 0 )
                    t = rotateWithRightChild( t );
                else
                    t = doubleWithRightChild( t );
            }
        }
        // update height as needed and return
        t.height = Math.max( height( t.left ), height( t.right ) ) + 1;
        return t;
    }
    
    /*
     * Internal method to remove from a subtree.
     *
     * @param x Item to remove.
     *
     * @param t Node that roots the subtree.
     *
     * @return New root of the subtree.
     */
    private AvlNode<T> remove( T x, AvlNode<T> t )
    {
        // Item not found at leaf; do nothing
        if ( t == null )
            return t;
        
        // Binary Seach Tree recursive removal methodology:
        // see BinarySearchTree.java for more information.
        int compareResult = x.compareTo( t.element );
        if ( compareResult < 0 )
            t.left = remove( x, t.left );
        else if ( compareResult > 0 )
            t.right = remove( x, t.right );
        else if ( t.left != null && t.right != null )
        {
            t.element = findMin( t.right ).element;
            t.right = remove( t.element, t.right );
        }
        else if ( t.left != null )
            t = t.left;
        else
            t = t.right;
        
        // After BST-style deletion (here, or anywhere below this node on some
        // other recursive call), we re-balance tree if necessary
        if ( t != null )
        {
            t = rebalance( t );
            t.height = Math.max( height( t.left ), height( t.right ) ) + 1;
        }
        return t;
    }
    
    /*
     * Internal method to re-balance following a delete.
     *
     * @param t Node that may need to be re-balanced
     */
    private AvlNode<T> rebalance( AvlNode<T> t )
    {
        // over-large on left
        if ( height( t.left ) - height( t.right ) == 2 )
        {
            // 2 cases: Left-Left and Left-Right imbalance
            if ( height( t.left.left ) > height( t.left.right ) )
                t = rotateWithLeftChild( t );
            else
                t = doubleWithLeftChild( t );
            
        }
        
        // over-large on right
        if ( height( t.right ) - height( t.left ) == 2 )
        {
            // 2 cases: Right-Right and Right-Left imbalance
            if ( height( t.right.right ) > height( t.right.left ) )
                t = rotateWithRightChild( t );
            else
                t = doubleWithRightChild( t );
        }
        
        return t;
    }
    
    /*
     * Internal method to find the least item in a subtree.
     *
     * @param t Node that roots the subtree.
     *
     * @return Node containing the least item.
     */
    private AvlNode<T> findMin( AvlNode<T> t )
    {
        if ( t == null )
            return t;
        
        while ( t.left != null )
            t = t.left;
        return t;
    }
    
    /*
     * Internal method to find the maximum item in a subtree.
     *
     * @param t Node that roots the subtree.
     *
     * @return Node containing the maximum item.
     */
    private AvlNode<T> findMax( AvlNode<T> t )
    {
        if ( t == null )
            return t;
        
        while ( t.right != null )
            t = t.right;
        return t;
    }
    
    /*
     * Internal method to find an item in a subtree.
     *
     * @param x Data item to search for.
     *
     * @param t Node that roots the subtree.
     *
     * @return true if and only if item x is found in subtree.
     */
    private boolean contains( T x, AvlNode<T> t )
    {
        while ( t != null )
        {
            int compareResult = x.compareTo( t.element );
            if ( compareResult < 0 )
                t = t.left;
            else if ( compareResult > 0 )
                t = t.right;
            else
                return true;
        }
        return false;
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
    private void printTree( AvlNode<T> t, StringBuilder builder )
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
     * Internal method to return the height of subtree rooted at a node.
     *
     * @return Maximal length of path from node t to any leaf;
     * -1 if t == null.
     */
    private int height( AvlNode<T> t )
    {
        return t == null ? -1 : t.height;
    }
    
    /*
     * Internal method to return the size of subtree rooted at a node.
     *
     * @return Number of nodes in tree rooted at t; 0 if empty.
     */
    private int size( AvlNode<T> t )
    {
        if ( t == null )
            return 0;
        else
            return 1 + size( t.left ) + size( t.right );
    }
    
    /*
     * Rotate binary tree node with single rotation,
     * for case of left-side outer rotation (LL).
     * Update heights, then return new root.
     *
     * @param oldRoot Root of deepest unbalanced subtree.
     *
     * @return Root of subtree that results from rotation.
     */
    private AvlNode<T> rotateWithLeftChild( AvlNode<T> oldRoot )
    {
        AvlNode<T> newRoot = oldRoot.left;
        oldRoot.left = newRoot.right;
        newRoot.right = oldRoot;
        oldRoot.height = Math.max( height( oldRoot.left ),
                                  height( oldRoot.right ) ) + 1;
        newRoot.height = Math.max( height( newRoot.left ), oldRoot.height ) + 1;
        return newRoot;
    }
    
    /*
     * Rotate binary tree node with single rotation,
     * for case of right-side outer rotation (RR).
     * Update heights, then return new root.
     *
     * @param oldRoot Root of deepest unbalanced subtree.
     *
     * @return Root of subtree that results from rotation.
     */
    private AvlNode<T> rotateWithRightChild( AvlNode<T> oldRoot )
    {
        AvlNode<T> newRoot = oldRoot.right;
        oldRoot.right = newRoot.left;
        newRoot.left = oldRoot;
        oldRoot.height = Math.max( height( oldRoot.left ),
                                  height( oldRoot.right ) ) + 1;
        newRoot.height = Math.max( height( newRoot.right ), oldRoot.height ) + 1;
        return newRoot;
    }
    
    /*
     * Double-rotate binary tree node: first rotate L child with R child,
     * then rotate the oldRoot node with new L child.
     * For case of left-side inner rotation (LR).
     * Update heights, then return new root.
     *
     * @param oldRoot Root of deepest unbalanced subtree.
     *
     * @return Root of subtree that results from rotation.
     */
    private AvlNode<T> doubleWithLeftChild( AvlNode<T> oldRoot )
    {
        // first single-rotate oldRoot's left child with its right child
        oldRoot.left = rotateWithRightChild( oldRoot.left );
        // then rotate with the (new) left child of oldRoot
        return rotateWithLeftChild( oldRoot );
    }
    
    /*
     * Double-rotate binary tree node: 1st rotate R child with L child,
     * then rotate the oldRoot node with new R child.
     * For case of right-side inner rotation (RL).
     * Update heights, then return new root.
     * 
     * @param oldRoot Root of deepest unbalanced subtree.
     * 
     * @return Root of subtree that results from rotation.
     */
    private AvlNode<T> doubleWithRightChild( AvlNode<T> oldRoot )
    {
        // first single-rotate oldRoot's right child with its left child
        oldRoot.right = rotateWithLeftChild( oldRoot.right );
        // then rotate with the (new) right child of oldRoot
        return rotateWithRightChild( oldRoot );
    }
    
    /*
     * Private static class for tree nodes.
     * Each contains a data element,
     * links to right- and left-subtrees,
     * along with height in the tree (for balance).
     */
    private static class AvlNode<T>
    {
        private T element;
        private AvlNode<T> right;
        private AvlNode<T> left;
        private int height;
        
        /*
         * Constructor for AvlNodes; sets data and subtree links.
         * 
         * @param theElement Data element that Node contains.
         * 
         * @param lt Root of left-hand subtree.
         * 
         * @param rt Root of right-hand subtree.
         */
        private AvlNode( T theElement, AvlNode<T> lt, AvlNode<T> rt )
        {
            element = theElement;
            left = lt;
            right = rt;
            height = 0;
        }
    }
}
