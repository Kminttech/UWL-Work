/**
 * Lab 04: Using stacks to convert elementary arithmetic expressions from
 * infix to postfix, and evaluate them.
 *
 * @author M. Allen
 * @author YOUR NAME HERE
 */
import java.util.*;
import java.io.*;

public class Parser
{
    /**
     * Generates new Parser object and uses it to convert some expressions,
     * reading data from input file.
     *
     * @param args Not used.
     */
    public static void main( String[] args )
    {
        String fileName = "input.txt";
        Scanner scan;
        try
        {
            scan = new Scanner( new File( fileName ) );
            
            while ( scan.hasNextLine() )
            {
                String infix = scan.nextLine();
                System.out.println( "Infix:   " + infix );
                String postfix = toPostfix(infix);
                System.out.println( "Postfix:   " + postfix);
                int result = evaluate(postfix);
                System.out.println( "Result = " + result);
                System.out.println();
            }
        }
        catch ( FileNotFoundException e )
        {
            System.err.println( "Error on processing file " + fileName );
            e.printStackTrace();
        }
    }
    
    public static String toPostfix(String input){
    	String output = "";
    	Stack<Character> storage = new Stack<Character>();
    	
    	for(int i = 0; i < input.length(); i++){
    		char cur = input.charAt(i);
    		if(cur == '+' || cur == '-' || cur == '*' || cur == '/'){
    			storage.push(cur);
    		}else if(Character.isDigit(cur)){
    			output = output + cur;
    		}else if(cur == ')'){
    			output = output + storage.pop();
    		}
    	}
    	return output;
    }
    
    public static int evaluate(String input){
    	Stack<Integer> operands = new Stack<Integer>();
    	for(int i = 0; i < input.length(); i++){
    		char cur = input.charAt(i);
    		if(Character.isDigit(cur)){
    			operands.push(Character.digit(cur, 10));
    		}else{
    			if(cur == '+'){
    				operands.push(operands.pop() + operands.pop());
    			}else if(cur == '-'){
    				int temp = operands.pop();
    				operands.push(operands.pop() - temp);
    			}else if(cur == '*'){
    				operands.push(operands.pop() * operands.pop());
    			}else if(cur == '/'){
    				int temp = operands.pop();
    				operands.push(operands.pop() / temp);
    			}
    		}
    	}
        return operands.pop();
    }
}
