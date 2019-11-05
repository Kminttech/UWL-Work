/**
 * This class extends the JFrame class to create a window
 * containing inputs and buttons for the Shell Sorting lab to use.
 *
 * @author M. Allen
 * @author YOUR NAME HERE
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings( "serial" )
public class InterfaceWindow extends JFrame implements ActionListener
{
    private JButton name, num, wgt;
    private JTextArea textArea;
    private Sorter sort;
    
    private final int WINDOW_WIDTH = 650;
    private final int WINDOW_HEIGHT = 650;
    
    /**
     * post: creates window with text fields for file-name and text,
     * and action buttons to handle the text input
     */
    public void buildWindow()
    {
        // set up the basic window
        setTitle( "Rosterizer" );
        setBounds( 10, 10, WINDOW_WIDTH, WINDOW_HEIGHT );
        setVisible( true );
        setResizable( false );
        setLayout( null );
        getContentPane().setBackground( Color.lightGray );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        // create and add buttons
        name = new JButton( "Name" );
        name.setBounds( WINDOW_WIDTH / 6 - 50, 10, 100, 30 );
        name.addActionListener( this );
        add( name, 0 );
        num = new JButton( "Number" );
        num.setBounds( WINDOW_WIDTH / 2 - 50, 10, 100, 30 );
        num.addActionListener( this );
        add( num, 0 );
        wgt = new JButton( "Weight" );
        wgt.setBounds( 5 * WINDOW_WIDTH / 6 - 50, 10, 100, 30 );
        wgt.addActionListener( this );
        add( wgt, 0 );
        
        // add text display area
        textArea = new JTextArea();
        textArea.setEditable( false );
        textArea.setLineWrap( true );
        textArea.setWrapStyleWord( true );
        textArea.setFont( new Font( "Monospaced", Font.PLAIN, 12 ) );
        textArea.setBounds( 5, 90, WINDOW_WIDTH - 10, WINDOW_HEIGHT - 120 );
        JScrollPane scroll = new JScrollPane( textArea );
        scroll.setBounds( 5, 45, textArea.getWidth(), textArea.getHeight() );
        add( scroll, 0 );
        
        repaint();
        
        // load data
        sort = new Sorter( "playerData.txt", textArea );
        sort.displayData();
    }
    
    /* post: Responds to action events from buttons. */
    public void actionPerformed( ActionEvent e )
    {
        if ( e.getSource() == name )
        {
            sort.changeSort(2);
        }
        if ( e.getSource() == num )
        {
            sort.changeSort(1);
        }
        if ( e.getSource() == wgt )
        {
            sort.changeSort(3);
        }
        sort.displayData();
    }
}