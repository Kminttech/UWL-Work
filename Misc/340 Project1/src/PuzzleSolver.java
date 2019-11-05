/**
 * Interface for the word-puzzle helper class that solves the word-search
 * puzzle.
 * 
 * @author M. Allen
 */
import java.io.File;

public interface PuzzleSolver
{
    /**
     * After running this method, the PuzzleSolver implementing class will have
     * access to the given file.
     * 
     * @param inFile A text-file, presumed to be in word-search puzzle format.
     */
    public void readFile( File inFile );

    /**
     * Prints out results of the word-search puzzle, in specified format.
     */
    public void solvePuzzle();
}
