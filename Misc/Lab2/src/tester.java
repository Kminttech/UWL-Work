
public class tester {
	
	public static void main(String[] args) {
		 tester driver = new tester();
		 driver.testLists();
	}
	
	public void testLists(){
		MyArrayList<Integer> testList1 = new MyArrayList<Integer>();
		for(int i = 0; i < 10; i++){
			testList1.add((int) (Math.random() * 1001));
		}
		System.out.print("List of Integers: ");
		printList(testList1);
		System.out.print("Convert to Integer: ");
		printList(toIntList(testList1));
		System.out.println("");
		MyArrayList<Number> testList2 = new MyArrayList<Number>();
		for(int i = 0; i < 10; i++){
			if((i%2) == 0){
				testList2.add((double) (Math.random() * 1001));
			}else{
				testList2.add((int) (Math.random() * 1001));
			}
		}
		System.out.print("List Of Numbers: ");
		testList2.addAll(testList1);
		printList(testList2);
		System.out.print("Convert to Integer: ");
		printList(toIntList(testList2));
	}
	
	public <T> void printList(MyArrayList<T> list){
		System.out.print("(");
		for(T value: list){
			System.out.print(" " + value);
		}
		System.out.println(" )");
	}
	
	public <T extends Number> MyArrayList<Integer> toIntList(MyArrayList<T> list){
		MyArrayList<Integer> tempList = new MyArrayList<Integer>(); 
		for(int i = 0; i < list.size(); i++){
			tempList.add(list.get(i).intValue());
		}
		return tempList;
	}
}
