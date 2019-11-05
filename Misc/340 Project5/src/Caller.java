import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Scanner;
//@author Kevin Minter
public class Caller {

	public static void main(String[] args) {
		Caller user = new Caller();
		File input = new File(args[0]);
		user.computeFile(input);
	}
	
	public void computeFile(File input){
		try{
			PriorityQueue<Candidate> calls = new PriorityQueue<Candidate>();
			Scanner inputScan = new Scanner(input);
			int candidates = inputScan.nextInt();
			for(int i = 0; i < candidates; i++){
				calls.add(new Candidate(inputScan.next(), inputScan.nextInt()));
			}
			int numCalls = inputScan.nextInt();
			for(int j = 0; j < numCalls; j++){
				Candidate cur = calls.poll();
				System.out.println(cur.name);
				cur.nextCall += cur.callRate;
				calls.add(cur);
			}
			inputScan.close();
		}catch(IOException ex){
			System.out.println("Error with File");
		}
	}
	
	private class Candidate implements Comparable<Candidate>{
		private String name;
		private int callRate;
		private int nextCall;
		
		public Candidate(String name, int callRate){
			this.name = name;
			this.callRate = callRate;
			this.nextCall = 0;
		}

		@Override
		public int compareTo(Candidate arg0) {
			if(nextCall > arg0.nextCall){
				return 1;
			}else if(nextCall < arg0.nextCall){
				return -1;
			}else{
				return name.compareTo(arg0.name);
			}
		}	
	}
}
