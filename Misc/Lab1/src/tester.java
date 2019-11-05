import java.util.ArrayList;
import java.util.Random;

public class tester {

	public static void main(String[] args) {
		ArrayList<Datum> intList = new ArrayList<Datum>();
		ArrayList<Datum> doubleList = new ArrayList<Datum>();
		Random rnd = new Random();
		
		for(int i = 0; i < 10; i++){
			IntegerDatum toAddI = new IntegerDatum();
			toAddI.set(rnd.nextInt(100));
			intList.add(toAddI.copyOf());
			DoubleDatum toAddD = new DoubleDatum();
			toAddD.set(rnd.nextDouble()*100);
			doubleList.add(toAddD.copyOf());
		}
		printDatumList(intList);
		printDatumList(doubleList);
	}
	
	public static <T> void printList(ArrayList<T> ls){
		for(int i = 0; i < ls.size(); i++){
			System.out.print(ls.get(i) + " ");
		}
		System.out.println("");
	}
	
	public static <T extends Datum<?>> void printDatumList(ArrayList<T> ls){
		for(int i = 0; i < ls.size(); i++){
			System.out.print(ls.get(i).get() + " ");
		}
		System.out.println("");
	}
}
