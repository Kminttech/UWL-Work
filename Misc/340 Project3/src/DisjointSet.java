// Kevin Minter 3/8/16
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class DisjointSet<T>{
	
	private int numUnions = 0;
	private ArrayList<Node<T>> list = new ArrayList<Node<T>>();
	
	//Adds an element
	public void add(T item){
		if(!contains(item)){
			list.add(new Node<T>(item));
		}
	}
	
	//Returns Number of elements
	public int size(){
		return list.size();
	}
	
	//Returns numver of roots
	public int numSets(){
		return list.size() - numUnions;
	}
	
	//Checks if an item exits in set
	public boolean contains(T input){
		Node<T> cur = getNode(input);
		if(cur == null){
			return false;
		}
		return true;
	}
	
	//gets data in the root of given element
	public T getSet(T input){
		Node<T> cur = getNode(input);
		if(cur == null){
			throw new NoSuchElementException("Your set did not include the element " + input);
		}
		cur = getRoot(cur);
		return cur.data;
	}
	
	//checks if the 2 elements have same root
	public boolean inSameSet(T input1, T input2){
		if(getSet(input1).equals(getSet(input2))){
			return true;
		}
		return false;
	}

	//gets the size of the set that the element is in
	public int sizeOfSet(T input){
		Node<T> cur = getNode(input);
		if(cur == null){
			throw new NoSuchElementException("Your set did not include the element " + input);
		}
		cur = getRoot(cur);
		return cur.size;
	}
	
	//Puts both given elemnts in same tree
	public T union(T input1, T input2){
		Node<T> node1 = getNode(input1);
		Node<T> node2 = getNode(input2);
		if(node1 == null || node2 == null){
			if(node1 == null){
				if(node2 == null){
					throw new NoSuchElementException("Your set did not include the elements " + input1 + " or " + input2);
				}else{
					throw new NoSuchElementException("Your set did not include the element " + input1);
				}
			}else{
				throw new NoSuchElementException("Your set did not include the element " + input2);
			}
		}
		
		Node<T> root1 = getRoot(node1);
		Node<T> root2 = getRoot(node2);
		if(root1.data.equals(root2.data)){
			return root1.data;
		}
		if(root1.rank > root2.rank){
			root2.parent = root1;
			root1.size += root2.size;
			numUnions++;
			return root1.data;
		}else if(root2.rank > root1.rank){
			root1.parent = root2;
			root2.size += root1.size;
			numUnions++;
			return root2.data;
		}else{
			if((int)(Math.random()*2)==0){
				root2.parent = root1;
				root1.size += root2.size;
				root1.rank++;
				numUnions++;
				return root1.data;
			}else{
				root1.parent = root2;
				root2.size += root1.size;
				root2.rank++;
				numUnions++;
				return root2.data;
			}
		}
	}
	
	//Returns the node with given element
	private Node<T> getNode(T item){
		for(Node<T> cur : list){
			if(cur.data.equals(item)){
				return cur;
			}
		}
		return null;
	}
	
	//Returns the node that is the root of the Node given
	private Node<T> getRoot(Node<T> cur){
		ArrayList<Node<T>> traversedList = new ArrayList<Node<T>>();
		while(cur.parent != cur){
			traversedList.add(cur);
			cur = cur.parent;
		}
		for(Node<T> update : traversedList){
			update.parent = cur;
		}
		return cur;
	}
	
	private class Node<T>{
		public T data;
		public Node<T> parent;
		public int rank;
		public int size;
		
		//Constructor for Node
		public Node(T data){
			this.data = data;
			this.parent = this;
			this.rank = 0;
			this.size = 1;
		}
	}
}