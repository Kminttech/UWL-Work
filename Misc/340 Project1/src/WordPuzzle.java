/**
 * Initiating program for Word-Search puzzle program. Creates the GUI that
 * allows user to choose puzzle files to open. These will be sent to a helper
 * class for solving.
 *
 * @author M. Allen
 * @author YOUR NAME HERE
 */
import java.awt.Color;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class WordPuzzle implements ActionListener
{
    private JFrame window;
    private JButton chooseButton;
    private JLabel fileNameLabel;
    private JFileChooser chooser;
    
    /**
     * Simple initiating main.
     *
     * @param args Not used.
     */
    public static void main( String[] args )
    {
        WordPuzzle puzzle = new WordPuzzle();
        puzzle.makeWindow();
    }
    
    /**
     * Creates graphical interface elements.
     */
    private void makeWindow()
    {
        window = new JFrame( "Choose an Input File" );
        window.getContentPane().setBackground( Color.lightGray );
        window.setLayout( null );
        window.setResizable( false );
        window.setVisible( true );
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        window.setLocation( 50, 50 );
        window.setSize( 400 + window.getInsets().left + window.getInsets().right,
                       150 );
        
        // interactive GUI elements
        chooseButton = new JButton( "Choose" );
        chooseButton.setBounds( 20, window.getHeight() / 2 - 30, 100, 30 );
        chooseButton.addActionListener( this );
        window.add( chooseButton, 0 );
        
        fileNameLabel = new JLabel( " <NO FILE SELECTED>" );
        fileNameLabel.setBackground( Color.white );
        fileNameLabel.setOpaque( true );
        int labX = chooseButton.getX() + chooseButton.getWidth() + 20;
        int labWidth = window.getWidth() - labX - 20;
        fileNameLabel.setBounds( labX, chooseButton.getY(), labWidth, chooseButton.getHeight() );
        window.add( fileNameLabel, 0 );
        
        // file-chooser for choosing input files to send to other classes
        chooser = new JFileChooser();
        chooser.setCurrentDirectory( new File( "." ) );
        chooser.setFileFilter( new FileNameExtensionFilter( "Text Files", "txt" ) );
    }
    
    /**
     * Performs word-search process on file.
     *
     * @param inFile Text-file, presumed to be in correct word-puzzle format.
     */
    private void solve( File inFile )
    {
        WordPuzzleSolver solver = new WordPuzzleSolver();
        solver.readFile(inFile);
        solver.solvePuzzle();
    }
    
    /**
     * Called by event, stemming from interface button.
     * Opens file-chooser dialog window; if a file is chosen, will call method
     * solve() on that file, to do the word-search process.
     *
     * @param e Event from button press.
     */
    @Override
    public void actionPerformed( ActionEvent e )
    {
        int returnVal = chooser.showOpenDialog( window );
        if ( returnVal == JFileChooser.APPROVE_OPTION )
        {
            File inFile = chooser.getSelectedFile();
            fileNameLabel.setText( " " + inFile.getName() );
            solve( inFile );
        }
    }
}
