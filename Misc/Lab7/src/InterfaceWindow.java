/**
 * This class extends the JFrame class to create a window
 * containing inputs and buttons for the Heap Sorting lab to use.
 *
 * @author M. Allen
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.*;

@SuppressWarnings( "serial" )
public class InterfaceWindow extends JFrame implements ActionListener
{
    private JButton choose, up, down;
    private JTextArea textArea;
    private Label fileField;
    private JFileChooser chooser;
    private File inFile;
    private Sorter sort;
    
    private final int WINDOW_WIDTH = 650;
    private final int WINDOW_HEIGHT = 650;
    
    /**
     * post: creates window with text fields for file-name and text,
     * and action buttons to handle the text input
     */
    public void createWindow()
    {
        // set up the basic window
        setTitle( "IP Usage Data" );
        setBounds( 10, 10, WINDOW_WIDTH, WINDOW_HEIGHT );
        setVisible( true );
        setResizable( false );
        setLayout( null );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        getContentPane().setBackground( Color.lightGray );
        
        // add label and field for FileName
        Label fLabel = new Label( "   Filename: " );
        fLabel.setBackground( Color.lightGray );
        fLabel.setBounds( 5, 15, 85, 20 );
        add( fLabel, 0 );
        fileField = new Label( "<NONE SELECTED>   " );
        fileField.setBackground( Color.lightGray );
        fileField.setBounds( 95, 15, 165, 20 );
        add( fileField, 0 );
        
        // chooser for files
        chooser = new JFileChooser();
        chooser.setCurrentDirectory( new java.io.File( "." ) );
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                                                     "Text Files", "txt" );
        chooser.setFileFilter( filter );
        
        // create and add buttons
        choose = new JButton( "Choose..." );
        choose.setBounds( 250, 10, 100, 30 );
        choose.addActionListener( this );
        add( choose, 0 );
        up = new JButton( "Upload" );
        up.setBounds( 360, 10, 100, 30 );
        up.addActionListener( this );
        add( up, 0 );
        down = new JButton( "Download" );
        down.setBounds( 470, 10, 100, 30 );
        down.addActionListener( this );
        add( down, 0 );
        
        // add text display area
        textArea = new JTextArea();
        textArea.setEditable( false );
        textArea.setLineWrap( true );
        textArea.setWrapStyleWord( true );
        textArea.setBounds( 5, 90, WINDOW_WIDTH - 10, WINDOW_HEIGHT - 120 );
        JScrollPane scroll = new JScrollPane( textArea );
        scroll.setBounds( 5, 45, textArea.getWidth(), textArea.getHeight() );
        add( scroll, 0 );
        
        repaint();
    }
    
    // post: inFile == file chosen by user (if any)
    private void openFile()
    {
        int returnVal = chooser.showOpenDialog( this );
        if ( returnVal == JFileChooser.APPROVE_OPTION )
        {
            inFile = chooser.getSelectedFile();
            fileField.setText( inFile.getName() );
            textArea.setText( "" );
        }
    }
    
    /* Responds to events; required by ActionListener. */
    public void actionPerformed( ActionEvent e )
    {
        if ( e.getSource() == choose )
        {
            openFile();
            if ( inFile != null )
                sort = new Sorter( inFile, textArea );
        }
        else if ( sort != null )
            if ( e.getSource() == up )
                sort.printSortUp();
            else if ( e.getSource() == down )
                sort.printSortDown();
    }
}
