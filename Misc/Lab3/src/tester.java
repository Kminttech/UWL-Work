import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class tester {

	public static void main(String[] args) {
		MyArrayList<Integer> intArray = new MyArrayList<Integer>();
		MyLinkedList<Double> doubleLinked = new MyLinkedList<Double>();
		try{
			Scanner input = new Scanner(new File("nums.txt"));
			while(input.hasNext()){
				if(input.hasNextInt()){
					intArray.add(input.nextInt());
				}else{
					doubleLinked.add(input.nextDouble());
				}
			}
			input.close();
		}catch(IOException ex){
			System.out.println("File Invalid");
		}
		printSubList(intArray,3);
		printSubList(doubleLinked,3);
		MyArrayList<Integer> intArray2 = new MyArrayList<Integer>();
		intArray2.addAll(intArray);
		printSubList(intArray2, intArray2.size()-1);
		intArray.addAll(intArray);
		printSubList(intArray,intArray.size()-1);
		MyLinkedList<Double> doubleLinked2 = new MyLinkedList<Double>();
		doubleLinked2.addAll(doubleLinked);
		printSubList(doubleLinked2,doubleLinked2.size()-1);
	}
	
	public static void printSubList(SimpleList list, int n){
		if(n < 0 || n >= list.size()){
			throw new IndexOutOfBoundsException();
		}
		Iterator subIter = list.iterator();
		for(int i = 0; i < n; i++){
			System.out.println(subIter.next());
		}
	}
}
