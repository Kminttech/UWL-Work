// Description: Creates a resizable, scrollable window, in
// which WordLabel objects can be placed.
import java.awt.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings( "serial" )
public class CloudWindow extends JFrame
{
    
    private static int WIDTH = 600;
    private static int HEIGHT = 450;
    
    // pre: should be given hash-map with words and counts
    // post: produces window with scrollable content, displaying
    // WordLabels corresponding to contents of input hash-map
    public void makeWindow( LinkedList<Counter> counts )
    {
        // create the window with scroll-bars, etc.
        setTitle( "I Have a Dream" );
        setLayout( new BorderLayout() );
        JPanel panel = new JPanel();
        panel.setBackground( Color.white );
        panel.setPreferredSize( new Dimension( WIDTH, HEIGHT * 5 ) );
        JScrollPane scroll = new JScrollPane( panel );
        scroll.setPreferredSize( new Dimension( WIDTH + 20, HEIGHT ) );
        scroll.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
        getContentPane().add( scroll, BorderLayout.CENTER );
        
        // add the WordLabel objects to the panel in alphabetical order
        // by first converting list of keys from HashSet to an array & sorting
        WordCounter[] countArray = counts.toArray( new WordCounter[counts.size()] );
        Arrays.sort( countArray );
        // each word in the sorted array is then turned into a WordLabel
        // (using its frequency number to set the size)
        for ( int i = 0; i < countArray.length; i++ )
        {
            String word = countArray[i].getWord();
            int num = countArray[i].getCount();
            WordLabel wl = new WordLabel( word, num );
            panel.add( wl );
        }
        
        // finish setting up window
        pack();
        setMinimumSize( new Dimension( WIDTH + 20, HEIGHT - 100 ) );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setVisible( true );
        repaint();
    }
}