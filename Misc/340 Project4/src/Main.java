import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author K. Minter
 */
public class Main{  
    /**
     * Takes input file to output connections
     * @param args[0] input file
     */
    public static void main(String[] args){
    	DisjointSet<String> connections = new DisjointSet<String>(); //Keeps track of all connections
    	ArrayList<String> directConnections = new ArrayList<String>(); //Keeps track of direct connections
        try{
        	File input = new File(args[0]);
        	Scanner parcer = new Scanner(input);
        	
        	while(parcer.hasNextLine()){
        		String curCommand = parcer.next();
        		String user1 = parcer.next() + " " + parcer.next() + " " + parcer.next();
        		String user2 = parcer.next() + " " + parcer.next() + " " + parcer.next();
    			connections.add(user1);
    			connections.add(user2);
        		if(curCommand.charAt(0) == 'C'){
        			connections.union(user1, user2);
        			directConnections.add(user1 + user2);
        		}else if(curCommand.charAt(0) == 'Q'){
        			if(connections.inSameSet(user1, user2)){
        				if(directConnections.contains(user1+user2) || directConnections.contains(user2+user1)){
        					System.out.println(user1.toString() + " " + user2.toString() + ": ARE CONNECTED (DIRECT CONTACT)");
        				}else{
        					System.out.println(user1.toString() + " " + user2.toString() + ": ARE CONNECTED (INDIRECT CONTACT)");
        				}
        			}else{
        				System.out.println(user1.toString() + " " + user2.toString() + ": NOT CONNECTED");
        			}
        		}else{
        			System.out.println("Error");
        		}
        		parcer.nextLine();
        	}
        	parcer.close();
        	System.out.println("Total contact groups: " + connections.numSets());
        }catch(FileNotFoundException ex){
        	System.out.println("File was not valid");
        }
    }
}
