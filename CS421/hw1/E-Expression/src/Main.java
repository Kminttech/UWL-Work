import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input;
        try {
            input = new Scanner(new File(args[0]));
        }catch (IOException e){
            System.out.println("Error with input file");
            return;
        }
        while(input.hasNextLine()){
            String curLine = input.nextLine();
            Scanner curInput = new Scanner(curLine);
            System.out.printf("%s\n", evaluate(curInput, new String[26]));
        }
    }

    public static String evaluate(Scanner input, String[] vars){
        String tok = input.next();
        while (tok.equals("(")){
            tok = input.next();
        }
        String ans = tok;
        if(tok.equals("+") || tok.equals("-") || tok.equals("*") || tok.equals("/") || tok.equals("^") || tok.equals("%")){
            int value1 = 0;
            int value2 = 0;
            boolean valid = true;
            try{
                value1 = Integer.parseInt(evaluate(input, vars));
            }catch (NumberFormatException e){
                valid = false;
                ans = "undefined";
            }
            try{
                value2 = Integer.parseInt(evaluate(input, vars));
            }catch (NumberFormatException e){
                valid = false;
                ans = "undefined";
            }
            if(valid){
                if(tok.equals("+")){
                    ans = "" + (value1 + value2);
                }else if(tok.equals("-")){
                    ans = "" + (value1 - value2);
                }else if(tok.equals("*")){
                    ans = "" + (value1 * value2);
                }else if(tok.equals("/")){
                    if(value2 == 0){ input.next(); return "undefined"; }
                    ans = "" + (value1 / value2);
                }else if(tok.equals("^")){
                    ans = "" + ((int)Math.pow(value1, value2));
                }else if(tok.equals("%")) {
                    if(value2 == 0){ input.next(); return "undefined"; }
                    ans = "" + (value1 % value2);
                }
            }
            input.next();
        }else if(tok.equals("block")){
            String[] newVars = new String[26];
            System.arraycopy(vars, 0, newVars, 0, vars.length);
            input.next();
            String check = input.next();
            while (check.equals("(")){
                String temp = input.next();
                String temp2 = evaluate(input, vars);
                if(!temp2.equals("undefined")){
                    newVars[temp.charAt(0)-'a'] = temp2;
                }
                input.next();
                check = input.next();
            }
            ans = evaluate(input, newVars);
            input.next();
        }
        if(!ans.equals("undefined")){
            try{
                Integer.parseInt(ans);
            }catch (NumberFormatException e){
                ans = vars[ans.charAt(0)-'a'];
                if(ans == null){
                    return "undefined";
                }
            }
        }
        return ans;
    }
}
