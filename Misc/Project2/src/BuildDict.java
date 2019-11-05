import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.JFileChooser;

//BildDict.java
//Kevin Minter
//CS 220 - 02 9:55

public class BuildDict {

	public static void main(String[] args) {
		String[] dict = new String[2];
		int used = 0;
		try{
			JFileChooser loader = new JFileChooser();
			loader.setDialogTitle("Chose Source");
			int reply = loader.showOpenDialog(null);
			while(reply == JFileChooser.APPROVE_OPTION){
				File file = new File(loader.getSelectedFile().getAbsolutePath());
				Scanner input = new Scanner(file);
				
				while(used == 0){
					if(input.hasNext()){
						String str = removeSpecialChars(input.next());
						if(str.equals(str.toLowerCase()) && str.length() > 1){
							dict[used] = str;
							used++;
						}
					}else{
						break;
					}
				}
				
				while(input.hasNext()){
					String str = removeSpecialChars(input.next());
					if(str.equals(str.toLowerCase()) && str.length() > 1){
						if(checkUnique(str,dict,used - 1)){
							dict[used] = str;
							int pushedBack = 0;
							while(dict[used - pushedBack - 1].compareTo(dict[used - pushedBack]) > 0){
								String temp = dict[used - pushedBack - 1];
								dict[used - pushedBack - 1] = dict[used - pushedBack];
								dict[used - pushedBack] = temp;
								pushedBack++;
								if(pushedBack == used){
									break;
								}
							}
							used++;
							if(used >= dict.length){
								String[] temp = dict;
								dict = new String[dict.length*2];
								for(int i = 0; i < temp.length;i++){
									dict[i] = temp[i];
								}
							}
						}
					}
				}
				input.close();
				reply = loader.showOpenDialog(null);
			}
			outputDict(dict, used);
		}catch(IOException e){
			System.out.println("Error");
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
	
	public static boolean checkUnique(String str, String[] dict, int high){
		int low = 0;
		while(low<=high) {
			int mid = low + (high-low)/2;
			
			if(str.equals(dict[mid])){
				return false;
			}
			if(str.compareTo(dict[mid]) > 0){
				low = mid + 1;
			}else{
				high = mid - 1;
			}
		}
		return true;
	}
	
	public static void outputDict(String[] dict, int numOfOutput){
		try{
			JFileChooser saver = new JFileChooser();
			saver.setDialogTitle("Chose Dictionary Output File");
			int reply = saver.showSaveDialog(null);
			if(reply == JFileChooser.APPROVE_OPTION){
				File file = new File(saver.getSelectedFile().getAbsolutePath());
				FileOutputStream outFileStream = new FileOutputStream(file);
				PrintWriter outStream = new PrintWriter(outFileStream);
				
				for(int i = 0; i < numOfOutput;i++){
					outStream.println(dict[i]);
				}
				
				outStream.close();
				outFileStream.close();
			}
		}catch(IOException e){
			System.out.println("error");
		}
	}
}