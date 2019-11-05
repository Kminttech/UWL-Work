import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int letters = 0;
        int numbers = 0;
        int other = 0;
        int lines = 0;
        int words = 0;
        ArrayList<Integer> lengths = new ArrayList<>();
        Scanner userInput = new Scanner(System.in);
        System.out.print("File Name: ");
        try {
            File inputFile = new File(userInput.next());
            FileInputStream inputStream = new FileInputStream(inputFile);
            InputStreamReader reader = new InputStreamReader(inputStream);
            int curChar = reader.read();
            lines++;
            while (curChar != -1){
                if(curChar >= 'A' && curChar <= 'Z' || curChar >= 'a' && curChar <= 'z'){
                    letters++;
                }else if(curChar >= '0' && curChar <= '9'){
                    numbers++;
                }else{
                    other++;
                    if(curChar == '\n'){
                        lines++;
                    }
                }
                curChar = reader.read();
            }
            Scanner fileScanner = new Scanner(inputFile);
            while(fileScanner.hasNext()){
                words++;
                String curWord = fileScanner.next();
                while (curWord.length() > lengths.size()){
                    lengths.add(0);
                }
                lengths.set(curWord.length()-1,lengths.get(curWord.length()-1)+1);
            }
            System.out.printf("Number of lines: %d\nNumber of characters (total): %d\nNumber of letters: %d\nNumber of figures: %d\nNumber of other characters: %d\nNumber of words: %d\n",
                    lines,letters+numbers+other,letters,numbers,other,words);
            if(!lengths.isEmpty() && lengths.get(0)!=0){
                System.out.printf("Number of 1 letter words: %d\n", lengths.get(0));
            }
            for(int i = 1; i < lengths.size(); i++){
                if(lengths.get(i)!=0){
                    System.out.printf("Number of %d letter words: %d\n", i+1, lengths.get(i));
                }
            }
            reader.close();
            inputStream.close();
        }catch (IOException e){
            System.out.println("Error occurred with File");
        }
    }
}
