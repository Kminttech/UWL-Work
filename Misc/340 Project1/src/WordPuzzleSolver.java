import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

//Kevin Minter WordPuzzleSolver.java
public class WordPuzzleSolver implements PuzzleSolver {
	
	char[][] puzzle;
	LinkedList<String> words;
	
	@Override
	public void readFile(File inFile) {
		try{
			Scanner input = new Scanner(inFile);
			words = new LinkedList<String>();
			
			int height = input.nextInt();
			int length = input.nextInt();
			puzzle = new char[height][length];
			for(int i = 0; i < height; i++){
				String line = input.next();
				for(int j = 0; j < length; j++){
					puzzle[i][j] = line.charAt(j);
				}
			}
			
			while(input.hasNext()){
				words.add(input.next());
			}
			
			input.close();
		}catch(IOException e){
			System.out.println("Invalid Input");
		}
	}
	
	@Override
	public void solvePuzzle() {
		boolean loopOut = false;
		for(String word:words){
			for(int x = 0; x < puzzle.length; x++){
				for(int y = 0; y < puzzle[x].length; y++){
					if(puzzle[x][y] == word.charAt(0)){
						String dir = wordAtLocation(word,x,y);
						if(dir != null){
							System.out.println(word + " was found at row " + x + " and column " + y + " going " + dir);
							loopOut = true;
							break;
						}
					}
				}
				if(loopOut){
					break;
				}
			}
			if(loopOut){
				loopOut = false;
			}else{
				System.out.println(word + " was not found");
			}
		}
	}
	
	public String wordAtLocation(String str, int x, int y){
		char word[] = str.toCharArray();
		int dX = -1;
		int dY = 1;
		int check = 1;
		while(x+dX >= 0 && y+dY < puzzle[x].length && puzzle[x+dX][y+dY] == word[check]){
			dX--;
			dY++;
			check++;
			if(check >= word.length){
				return "Up And Left";
			}
		}
		dX = -1;
		dY = 0;
		check = 1;
		while(x+dX >= 0 && puzzle[x+dX][y] == word[check]){
			dX--;
			check++;
			if(check >= word.length){
				return "Up";
			}
		}
		dX = -1;
		dY = -1;
		check = 1;
		while(x+dX >= 0 && y+dY >= 0 && puzzle[x+dX][y+dY] == word[check]){
			dX--;
			dY--;
			check++;
			if(check >= word.length){
				return "Up and Right";
			}
		}
		dX = 0;
		dY = -1;
		check = 1;
		while(y+dY >= 0 && puzzle[x][y+dY] == word[check]){
			dY--;
			check++;
			if(check >= word.length){
				return "Left";
			}
		}
		dX = 0;
		dY = 1;
		check = 1;
		while(y+dY < puzzle[x].length && puzzle[x][y+dY] == word[check]){
			dY++;
			check++;
			if(check >= word.length){
				return "Right";
			}
		}
		dX = 1;
		dY = -1;
		check = 1;
		while(x+dX < puzzle.length && y+dY >= 0 && puzzle[x+dX][y+dY] == word[check]){
			dX++;
			dY--;
			check++;
			if(check >= word.length){
				return "Down and Left";
			}
		}
		dX = 1;
		dY = 0;
		check = 1;
		while(x+dX < puzzle.length && puzzle[x+dX][y] == word[check]){
			dX++;
			check++;
			if(check >= word.length){
				return "Down";
			}
		}
		dX = 1;
		dY = 1;
		check = 1;
		while(x+dX < puzzle.length && y+dY < puzzle[x].length && puzzle[x+dX][y+dY] == word[check]){
			dX++;
			dY++;
			check++;
			if(check >= word.length){
				return "Down And Right";
			}
		}
		return null;
	}
}