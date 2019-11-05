// LatinSquaresSolver.java
// Kevin Minter
// CS 220 - 02 9:55
public class LatinSquaresSolver {
	//wrapper class for solve
	public boolean solve(int [][] puzzle) {
		if(isImpossible(puzzle)){
			return false;
		}else{
			return solve(puzzle,0,0);
		}
	}
	
	//uses recursion to solve puzzle
	private boolean solve(int[][] puzzle, int row, int col){
		//loops to next row if col is out of bounds
		if(col>=puzzle[row].length){
			row++;
			col=0;
			//returns true if entire grid has been looped through
			if(row>=puzzle.length){
				return true;
			}
		}
		//if input was preset then moves on otherwise checks for valid entries until no valid entries found and backtracks
		if(puzzle[row][col]>0){
			int temp = puzzle[row][col];
			puzzle[row][col] = 0;
			if(checkNumberValid(puzzle,row,col,temp)){
				puzzle[row][col] = temp;
				return solve(puzzle,row,col+1);
			}else{
				puzzle[row][col] = temp;
				return false;
			}
		}else{
			for(int i = 1; i <= puzzle.length; i++){
				if(checkNumberValid(puzzle, row, col, i)){
					puzzle[row][col] = i;
					if(solve(puzzle,row,col+1)){
						return true;
					}
				}
			}
			puzzle[row][col] = 0;
			return false;
		}
	}
	
	//checks if placing number in the specified row and col if it would remain valid
	private boolean checkNumberValid(int[][] puzzle, int row, int col, int num){
		for(int i = 0; i < puzzle.length;i++){
			if(puzzle[i][col] == num){
				return false;
			}
		}
		for(int i = 0; i < puzzle[row].length;i++){
			if(puzzle[row][i] == num){
				return false;
			}
		}
		return true;
	}
	
	//used to check input to see if any contradicting data is given to begin with
	private boolean isImpossible(int[][] puzzle){
		for(int row = 0; row < puzzle.length; row++){
			for(int col = 0; col < puzzle[row].length; col++){
				if(puzzle[row][col] > 0){
					int temp = puzzle[row][col];
					puzzle[row][col] = 0;
					if(checkNumberValid(puzzle,row,col,temp)){
						puzzle[row][col] = temp;
					}else{
						puzzle[row][col] = temp;
						return true;
					}
				}
			}
		}
		return false;
	}
}