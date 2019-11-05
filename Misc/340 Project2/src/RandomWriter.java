/**
 * Creates a window containing GUI for the RandomText project. Code must be
 * added to read in a selected text-file and send it to a StringMaker object.
 * The text returned by such an object will then be displayed in the window.
 *
 * @author M. Allen
 * @author YOUR NAME HERE
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.Scanner;

public class RandomWriter implements ActionListener
{
    private JFrame window;
    private JButton choose, run;
    private JTextArea textArea;
    private Label fileField;
    private JFileChooser chooser;
    private File inFile;
    private JComboBox<String> levelPicker;
    private int level = 0;
    
    private final int WINDOW_WIDTH = 650;
    private final int WINDOW_HEIGHT = 650;
    
    /**
     * Standard initiating main().
     *
     * @param args Not used.
     */
    public static void main( String[] args )
    {
        RandomWriter rando = new RandomWriter();
        rando.makeWindow();
    }
    
    /*
     * post: creates window with text fields for file-name and text,
     * and action buttons to handle the text input
     */
    public void makeWindow()
    {
        // set up the basic window
        window = new JFrame( "Mix It Up!" );
        window.setVisible( true );
        window.setResizable( false );
        window.setLayout( null );
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        window.setBounds( 50, 50, WINDOW_WIDTH + window.getInsets().left + window.getInsets().right,
                         WINDOW_HEIGHT + window.getInsets().top + window.getInsets().bottom );
        window.getContentPane().setBackground( Color.lightGray );
        
        // add label and field for FileName
        Label fLabel = new Label( "  Filename: " );
        fLabel.setBackground( Color.lightGray );
        fLabel.setBounds( 5, 15, 75, 20 );
        window.add( fLabel, 0 );
        fileField = new Label( "<NONE SELECTED>   " );
        fileField.setBackground( Color.lightGray );
        fileField.setBounds( 85, 15, 250, 20 );
        window.add( fileField, 0 );
        
        // chooser for files
        chooser = new JFileChooser();
        chooser.setCurrentDirectory( new java.io.File( "." ) );
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                                                     "Text Files",
                                                                     "txt" );
        chooser.setFileFilter( filter );
        
        // label and option-boxes for parameters
        Label pLabel = new Label( "  Analysis: " );
        pLabel.setBackground( Color.lightGray );
        pLabel.setBounds( 5, 50, 75, 20 );
        window.add( pLabel, 0 );
        String[] levelArray = { "0: Single characters", "1: Character pairs",
            "2: Character triples",
            "3: Character 4-tuples",
            "4: Character 5-tuples",
            "5: Character 6-tuples",
            "6: Character 7-tuples",
            "7: Character 8-tuples",
            "8: Character 9-tuples",
            "9: Character 10-tuples",
            "10: Character 11-tuples",
            "11: Character 12-tuples",
            "12: Character 13-tuples" };
        levelPicker = new JComboBox<>( levelArray );
        levelPicker.setSelectedIndex( 0 );
        levelPicker.setMaximumRowCount( levelArray.length );
        levelPicker.addActionListener( this );
        levelPicker.setBounds( 85, 50, 250, 20 );
        window.add( levelPicker, 0 );
        
        // create and add buttons
        choose = new JButton( "Choose..." );
        choose.setBounds( 340, 10, 100, 30 );
        choose.addActionListener( this );
        window.add( choose, 0 );
        run = new JButton( "Run" );
        run.setBounds( 340, 45, 100, 30 );
        run.addActionListener( this );
        window.add( run, 0 );
        
        // add text display area
        textArea = new JTextArea();
        textArea.setEditable( false );
        textArea.setLineWrap( true );
        textArea.setWrapStyleWord( true );
        textArea.setBounds( 5, 90, WINDOW_WIDTH - 10, WINDOW_HEIGHT - 120 );
        window.add( textArea, 0 );
        
        window.repaint();
    }
    
    // post: inFile == file chosen by user (if any)
    private void openFile()
    {
        int returnVal = chooser.showOpenDialog( window );
        if ( returnVal == JFileChooser.APPROVE_OPTION )
        {
            inFile = chooser.getSelectedFile();
            fileField.setText(inFile.getName());
        }
    }
    
    /**
     * Responds to ActionEvent from GUI elements.
     * From interface ActionListener.
     */
    @Override
    public void actionPerformed( ActionEvent e )
    {
        if (e.getSource() == choose ){
            openFile();
        }else if (e.getSource() == levelPicker){
            level = levelPicker.getSelectedIndex();
        }else if (e.getSource() == run){
        	try{
            	Scanner input = new Scanner(inFile);
            	String fileString = "";
            	while(input.hasNextLine()){
            		fileString = fileString + input.nextLine();
            	}
            	TextMaker generator = new TextMaker(fileString,level);
            	textArea.setText(generator.getOutput());
            }catch(IOException ex){
            	System.out.println("File invalid");
            }
        }
    }
}