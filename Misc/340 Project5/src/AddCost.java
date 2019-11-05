import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Scanner;
//@author Kevin Minter
public class AddCost {
	
	public static void main(String[] args){
		AddCost user = new AddCost();
		File input = new File(args[0]);
		user.computeFile(input);
	}
	
	public void computeFile(File input){
		try{
			Scanner inputScan = new Scanner(input);
			while(inputScan.hasNextLine()){
				int numValues = inputScan.nextInt();
				if(numValues > 1){
					int[] values = new int[numValues];
					for(int i = 0; i < numValues; i++){
						values[i] = inputScan.nextInt();
					}
					minCostAdd(values);
				}else if(numValues == 1){
					System.out.println("Sum: " + inputScan.nextInt() + ", Cost: 0");
				}
			}
			inputScan.close();
		}catch(IOException ex){
			System.out.println("Error with File");
		}
	}

	
	public void minCostAdd(int[] nums){
		PriorityQueue<Integer> addition = new PriorityQueue<Integer>();
		for(int temp:nums){
			addition.add(temp);
		}
		while(!addition.isEmpty()){
			int lower = addition.remove();
			int sum = lower + addition.remove();
			if(addition.isEmpty()){
				System.out.println("Sum: " + sum + ", Cost: " + (sum + lower));
				break;
			}
			addition.add(sum);
		}
	}
}
