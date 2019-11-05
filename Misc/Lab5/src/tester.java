import java.util.ArrayList;

public class tester {

	public static void main(String[] args) {
		ArrayList<Integer> nums = new ArrayList<Integer>();
		for(int i = 1; i <= 2500; i++){
			nums.add(i);
		}
		int bstBest = Integer.MAX_VALUE;
		int bstWorst = Integer.MIN_VALUE;
		int bstResults[] = new int[1000];
		for(int test = 0; test < bstResults.length; test++){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.addAll(nums);
			BinarySearchTree<Integer> testBST = new BinarySearchTree<Integer>();
			while(!temp.isEmpty()){
				testBST.insert(temp.remove((int)(Math.random()*temp.size())));
			}
			bstResults[test] = testBST.height();
			if(testBST.height() < bstBest){
				bstBest = testBST.height();
			}
			if(testBST.height() > bstWorst){
				bstWorst = testBST.height();
			}
		}
		int bstAverage = 0;
		for(int i = 0; i < bstResults.length; i++){
			bstAverage += bstResults[i];
		}
		bstAverage = bstAverage / bstResults.length;
		
		
		int avlBest = Integer.MAX_VALUE;
		int avlWorst = Integer.MIN_VALUE;
		int avlResults[] = new int[1000];
		for(int test = 0; test < avlResults.length; test++){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.addAll(nums);
			AvlTree<Integer> testAvl= new AvlTree<Integer>();
			while(!temp.isEmpty()){
				testAvl.insert(temp.remove((int)(Math.random()*temp.size())));
			}
			avlResults[test] = testAvl.height();
			if(testAvl.height() < avlBest){
				avlBest = testAvl.height();
			}
			if(testAvl.height() > avlWorst){
				avlWorst = testAvl.height();
			}
		}
		int avlAverage = 0;
		for(int i = 0; i < avlResults.length; i++){
			avlAverage += avlResults[i];
		}
		avlAverage = avlAverage / avlResults.length;
		
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.addAll(nums);
		BinarySearchTree<Integer> readdBST = new BinarySearchTree<Integer>();
		while(!temp.isEmpty()){
			readdBST.insert(temp.remove((int)(Math.random()*temp.size())));
		}
		int bstStartHeight = readdBST.height();
		for(int i = 0; i < Math.pow(2500, 2); i++){
			int reinsert = (int) (Math.random() * 2500);
			readdBST.remove(reinsert);
			readdBST.insert(reinsert);
		}
		int bstEndHeight = readdBST.height();
		
		temp.addAll(nums);
		AvlTree<Integer> readdAvl= new AvlTree<Integer>();
		while(!temp.isEmpty()){
			readdAvl.insert(temp.remove((int)(Math.random()*temp.size())));
		}
		int avlStartHeight = readdAvl.height();
		for(int i = 0; i < Math.pow(2500, 2); i++){
			int reinsert = (int) (Math.random() * 2500);
			readdAvl.remove(reinsert);
			readdAvl.insert(reinsert);
		}
		int avlEndHeight = readdAvl.height();
		
		System.out.println("1000 tests of BST with 2500 numbers:");
		System.out.println("Best Possible Height: 12");
		System.out.println("Best Height Achieved: " + bstBest);
		System.out.println("Worst Height Achieved: " + bstWorst);
		System.out.println("Average Height: " + bstAverage);
		System.out.println("");
		System.out.println("Height at start: " + bstStartHeight);
		System.out.println("Height after random removes and inserts: " + bstEndHeight);
		
		
		System.out.println("1000 tests of AVL with 2500 numbers:");
		System.out.println("Best Possible Height: 12");
		System.out.println("Best Height Achieved: " + avlBest);
		System.out.println("Worst Height Achieved: " + avlWorst);
		System.out.println("Average Height: " + avlAverage);
		System.out.println("");
		System.out.println("Height at start: " + avlStartHeight);
		System.out.println("Height after random removes and inserts: " + avlEndHeight);
	}
}
