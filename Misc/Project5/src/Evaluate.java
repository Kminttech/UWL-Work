import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import javax.swing.JFileChooser;

public class Evaluate {

	public static void main(String[] args) {
		Stack<Character> operators = new Stack<Character>();
		Stack<Integer> operands = new Stack<Integer>();
		try{
			JFileChooser loader = new JFileChooser();
			loader.setDialogTitle("Chose Source");
			int reply = loader.showOpenDialog(null);
			if(reply == JFileChooser.APPROVE_OPTION){
				File file = new File(loader.getSelectedFile().getAbsolutePath());
				FileReader fileReader = new FileReader(file);
	    		BufferedReader bufReader = new BufferedReader(fileReader);
				String expresion = bufReader.readLine();
				while(expresion!=null){
					for(int i = 0; i < expresion.length();i++){
						char cur = expresion.charAt(i);
						if(cur == '+' || cur == '-' || cur == '*' || cur == '/' || cur == '<' || cur == '>' || cur == '('){
							while(!operators.isEmpty() && lowerPrecedence(cur,operators.peek())){
								operands.push(evaluate(operators.pop(), operands.pop(), operands.pop()));
							}
							operators.push(cur);
						}else if(cur == ')'){
							while(operators.peek() != '('){
								operands.push(evaluate(operators.pop(), operands.pop(), operands.pop()));
							}
							operators.pop();
						}else{
							operands.push(Character.digit(cur, 10));
						}
					}
					while(!operators.isEmpty()){
						operands.push(evaluate(operators.pop(), operands.pop(), operands.pop()));
					}
					System.out.println(expresion + "==" + operands.pop());
					expresion = bufReader.readLine();
				}
	    		bufReader.close();
	    		fileReader.close();
			}
		}catch(IOException ex){
			System.out.println("IO Error");
		}
	}
	
	public static int evaluate(char operator, int num2, int num1){
		switch(operator){
			case '+' : return num1 + num2;
			case '-' : return num1 - num2;
			case '*' : return num1 * num2;
			case '/' : return num1 / num2;
			case '<' : return Math.min(num1 , num2);
			case '>' : return Math.max(num1, num2);
		}
		return 0;
	}
	
	public static boolean lowerPrecedence(char ch1,char ch2){
		if(ch1 == '(' || ch2 == '('){
			return false;
		}
		int precedence1 = precedenceValue(ch1);
		int precedence2 = precedenceValue(ch2);
		if(precedence1 <= precedence2){
			return true;
		}
		return false;
	}
	
	public static int precedenceValue(char ch){
		if(ch == '+' || ch == '-'){
			return 1;
		}else if(ch == '*' || ch == '/'){
			return 2;
		}else if(ch == '<' || ch == '>'){
			return 3;
		}
		return 0;
	}
}
