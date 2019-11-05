import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
//@author Kevin Minter
public class Checker {

	public static void main(String[] args) {
		File input = new File(args[0]);
		try{
			Scanner inputScan = new Scanner(input);
			while(inputScan.hasNextLine()){
				PriorityQueue<Integer> priorityTest = new PriorityQueue<Integer>(Collections.reverseOrder());
				Stack<Integer> stackTest = new Stack<Integer>();
				Queue<Integer> queueTest = new LinkedList<Integer>();
				boolean priorityPossible = true, stackPossible = true, queuePossible = true;
				int lines = inputScan.nextInt();
				for(int i = 0; i < lines; i++){
					String command = inputScan.next();
					int value = inputScan.nextInt();
					if(command.equals("add")){
						priorityTest.add(value);
						stackTest.add(value);
						queueTest.add(value);
					}else if(command.equals("rem")){
						if(!(priorityTest.poll() == value)){
							priorityPossible = false;
						}
						if(!(stackTest.pop() == value)){
							stackPossible = false;
						}
						if(!(queueTest.poll() == value)){
							queuePossible = false;
						}
					}
				}
				int numPossible = 0;
				if(priorityPossible){
					numPossible++;
				}
				if(stackPossible){
					numPossible++;
				}
				if(queuePossible){
					numPossible++;
				}
				if(numPossible == 0){
					System.out.println("impossible");
				}else if(numPossible > 1){
					System.out.println("not sure");
				}else{
					if(priorityPossible){
						System.out.println("priority queue");
					}
					if(stackPossible){
						System.out.println("stack");
					}
					if(queuePossible){
						System.out.println("queue");
					}
				}
				inputScan.nextLine();
			}
			inputScan.close();
		}catch(IOException ex){
			System.out.println("Error with File");
		}
	}
}
