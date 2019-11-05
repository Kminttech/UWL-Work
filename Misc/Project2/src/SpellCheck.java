import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;

//SpellCheck.java
//Kevin Minter
//CS 220 - 02 9:55

public class SpellCheck {

	public static void main(String[] args) {
		String[] dict = loadDict();
		String[] checkText = loadText();
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < checkText.length; i++){
			if(!(inDict(checkText[i],dict))){
				System.out.println(checkText[i]);
			}
		}
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("Time in milliseconds: " + elapsedTime);
	}
	
	public static boolean inDict(String str, String[] dict){
		int low = 0, high = dict.length - 1;
		while(low<=high) {
			int mid = low + (high-low)/2;
			
			if(str.equals(dict[mid])){
				return true;
			}
			if(laterAlpha(str,dict[mid])){
				low = mid + 1;
			}else{
				high = mid - 1;
			}
			
		}
		return false;
	}
	
	public static boolean laterAlpha(String str1, String str2){
		if(str1.compareTo(str2) > 0){
			return true;
		}
		return false;
	}
	
	private static String[] loadDict(){
		String[] text = new String[1];
		int count = 0;
		try{
			JFileChooser loader = new JFileChooser();
			loader.setDialogTitle("Load Dictionary");
			int reply = loader.showOpenDialog(null);
			if(reply == JFileChooser.APPROVE_OPTION){
				File file = new File(loader.getSelectedFile().getAbsolutePath());
				Scanner input = new Scanner(file);
				while(input.hasNext()){
					text[count++] = input.next();
					if(count >= text.length){
						String[] temp = text;
						text = new String[text.length*2];
						for(int i = 0; i < temp.length;i++){
							text[i] = temp[i];
						}
					}
				}
				String[] temp = text;
				text = new String[count];
				for(int i = 0; i < text.length;i++){
					text[i] = temp[i];
				}
				input.close();
			}
			return text;
		}catch(IOException e){
			return text;
		}
	}
	
	private static String[] loadText(){
		String[] text = new String[1];
		int count = 0;
		try{
			JFileChooser loader = new JFileChooser();
			loader.setDialogTitle("Load Text");
			int reply = loader.showOpenDialog(null);
			if(reply == JFileChooser.APPROVE_OPTION){
				File file = new File(loader.getSelectedFile().getAbsolutePath());
				Scanner input = new Scanner(file);
				while(input.hasNext()){
					String str = removeSpecialChars(input.next());
					if(str.equals(str.toLowerCase()) && str.length() > 1){
						text[count++] = str;
					}
					if(count >= text.length){
						String[] temp = text;
						text = new String[text.length*2];
						for(int i = 0; i < temp.length;i++){
							text[i] = temp[i];
						}
					}
				}
				String[] temp = text;
				text = new String[count];
				for(int i = 0; i < text.length;i++){
					text[i] = temp[i];
				}
				input.close();
			}
			return text;
		}catch(IOException e){
			return text;
		}
	}
	
	public static String removeSpecialChars(String str){
		for(int i = 0; i < str.length(); i++){
			if((int)str.charAt(i) > (int)'z' || (int)str.charAt(i) < (int)'a'){
				return str.substring(0, i);
			}
		}
		return str;
	}
}
