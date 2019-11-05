import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class FSM {
	
	static final int NEXTSTATE = 0;
	static final int OUTPUT = 1;
	
	static int [][][] fsm;
	
	public static void initFSM(String filePath) {
		try{
			File inputFile = new File(filePath);
			FileReader fReader = new FileReader(inputFile);
			BufferedReader reader = new BufferedReader(fReader);
			
			String firstLine = reader.readLine();
			try{
				int numStates =  Integer.parseInt(firstLine);
				fsm = new int[numStates][128][2];
			}catch(Exception e){
				System.out.println("First Line not Interger");
				reader.close();
				fReader.close();
				return;
			}
			
			String line = reader.readLine();
			while(line!=null){
				Scanner scanner = new Scanner(line);
				
				int fromState = scanner.nextInt();
				char inputChar = scanner.next().charAt(0);
				int toState = scanner.nextInt();
				fsm[fromState][inputChar][NEXTSTATE] = toState;
				
				if(scanner.hasNext()){
					char outputChar = scanner.next().charAt(0);
					fsm[fromState][inputChar][OUTPUT] = outputChar;
				}
				line = reader.readLine();
			}
			
			reader.close();
			fReader.close();
		}catch(Exception e){
			System.out.println("Error: " + e.getMessage());
			return;
		}
	}
	
	public static void main(String[] args) {
		
		initFSM(args[0]);
		
		//Scanner scanner = new Scanner(System.in);
		try{
		File inputFile = new File(args[1]);
		FileReader fReader = new FileReader(inputFile);
		BufferedReader reader = new BufferedReader(fReader);
		File outputFile = new File(args[2]);
		PrintWriter writer = new PrintWriter(outputFile);
		
		int curState=0;
		
		int charAsInt = reader.read();
		
		while(charAsInt != -1){
			writer.println((char)(fsm[curState][charAsInt][OUTPUT]));
			curState = fsm[curState][charAsInt][NEXTSTATE];	
			charAsInt = reader.read();
		}
		reader.close();
		writer.close();
		}catch(Exception e){
			System.out.println("Error: " + e.getMessage());
			return;
		}
		
		/**
		while(true) {
			
			// read one char input from customer
			char input = scanner.next().charAt(0);
			
			// output something?
			System.out.println((char)(fsm[curState][input][OUTPUT]));
			
			// transition to new state
			curState = fsm[curState][input][NEXTSTATE];	
		}
		*/
	}

}
