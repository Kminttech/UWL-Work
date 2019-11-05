import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;

// CA.java
// Kevin Minter
// CS 220 - 02 9:55

public class CA {
	
	private boolean[][] lifeStates;

	// Constructor
	public CA(int gridSize){
		lifeStates = new boolean[gridSize][gridSize];
	}
	
	public int getGridSize() { return lifeStates.length; }

	// Check and set Statements taking a row and a column to interact with lifeStates
	public boolean isAlive(int row, int col) { return lifeStates[row][col]; }
	public void setAlive(int row, int col) {lifeStates[row][col] = true;}
	public void setDead(int row, int col) {lifeStates[row][col] = false;}
	
	//Sets all cells to dead in lifeStates
	public void clear(){
		for(int x = 0; x < getGridSize();x++){
			for(int y = 0; y < getGridSize();y++){
				setDead(x,y);
			}
		}
	}

	// Fills lifeStates with random cells
	public void random(){
		for(int x = 0; x < getGridSize();x++){
			for(int y = 0; y < getGridSize();y++){
				if(flipCoin()){
					setAlive(x,y);
				}else{
					setDead(x,y);
				}
			}
		}
	}
	
	//Generates Random Boolean
	public boolean flipCoin(){
		return Math.random() >= .5;
	}
	
	// Moves cells forward one generation following set rules
	public void step(){
		boolean[][] oldLifeStates = new boolean[getGridSize()][getGridSize()];
		for(int x = 0; x < getGridSize();x++){
			for(int y = 0; y < getGridSize();y++){
				oldLifeStates[x][y] = lifeStates[x][y];
			}
		}
		for(int x = 0; x < getGridSize();x++){
			for(int y = 0; y < getGridSize();y++){
				int numNeighbors = countLiveNeighbors(x,y,oldLifeStates);
				if(numNeighbors == 3){
					setAlive(x,y);
				}else if(numNeighbors < 2 || numNeighbors > 3){
					setDead(x,y);
				}
			}
		}
	}
	
	//Returns how many neighbors cell has in passed in lifeStates
	public int countLiveNeighbors(int row, int col, boolean[][] lifeStates){
		int numNeighbors = 0;
		int xCheck;
		int yCheck;
		for(int dX = -1; dX <= 1;dX++){
			for(int dY = -1; dY <= 1;dY++){
				if(!(dX==0&&dY==0)){
					xCheck = loopAdd(row,dX);
					yCheck = loopAdd(col,dY);
					if(lifeStates[xCheck][yCheck]){
						numNeighbors++;
					}
				}
			}
		}
		return numNeighbors;
	}
	
	//Used to loop the grid back around while checking neighbors
	public int loopAdd(int num1,int num2){
		int sum = num1+num2;
		if(sum<0){
			return getGridSize() - 1;
		}else if(sum >= getGridSize()){
			return 0;
		}
		return sum;
	}
	
	// Sets getGridSize() while retaining all information that remains on screen
	public void setGridSize(int gridSize){
		int formerGridSize = getGridSize();
		boolean[][] formerLifeStates = lifeStates;
		lifeStates = new boolean[gridSize][gridSize];
		if(gridSize < formerGridSize){
			for(int x = 0; x < gridSize;x++){
				for(int y = 0; y < gridSize;y++){
					lifeStates[x][y] = formerLifeStates[x][y];
				}
			}
		}else{
			for(int x = 0; x < formerGridSize;x++){
				for(int y = 0; y < formerGridSize;y++){
					lifeStates[x][y] = formerLifeStates[x][y];
				}
			}
		}
	}

	// Saves current layout using FileChooser
	public void save() {
		try{
			JFileChooser saver = new JFileChooser(System.getProperty("user.dir"));
			saver.setDialogTitle("Save Current Layout");
			int reply = saver.showSaveDialog(null);
			if(reply == JFileChooser.APPROVE_OPTION){
				File file = new File(saver.getSelectedFile().getAbsolutePath());
				FileOutputStream outFileStream = new FileOutputStream(file);
				DataOutputStream outDataStream = new DataOutputStream(outFileStream);
				outDataStream.writeInt(getGridSize());
				for(int x = 0; x < getGridSize();x++){
					for(int y = 0; y < getGridSize();y++){
						outDataStream.writeBoolean(lifeStates[x][y]);
					}
				}				
				outDataStream.close();
				outFileStream.close();
			}
		}catch(IOException e){
			System.out.println("Error");
		}
	}
	
	// Loads current layout using FileChooser
	public void load(){
		try{
			JFileChooser loader = new JFileChooser();
			loader.setDialogTitle("Load Layout");
			int reply = loader.showOpenDialog(null);
			if(reply == JFileChooser.APPROVE_OPTION){
				File file = new File(loader.getSelectedFile().getAbsolutePath());
				FileInputStream inFileStream = new FileInputStream(file);
				DataInputStream inDataStream = new DataInputStream(inFileStream);
				int loadedGridSize = inDataStream.readInt();
				this.setGridSize(loadedGridSize);
				for(int x = 0; x < getGridSize();x++){
					for(int y = 0; y < getGridSize();y++){
						if(inDataStream.readBoolean()){
							setAlive(x,y);
						}else{
							setDead(x,y);
						}
					}
				}
				inDataStream.close();
				inFileStream.close();
			}
		}catch(IOException e){
			System.out.println("Error");
		}
	}
}