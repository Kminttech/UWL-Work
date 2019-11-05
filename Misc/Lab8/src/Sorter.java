/**
 * Class for shell-sorting lab.
 *
 * @author: M. Allen
 * @author YOUR NAME HERE
 *
 */

import java.io.*;
import javax.swing.*;

public class Sorter
{
    private String inFile;
    private JTextArea output;
    private Player[] play;
    
    /**
     * Basic constructor. Loads data from file, displays it into window.
     *
     * @param fin String with name of input data text-file.
     * @param out Text-area for output display.
     */
    public Sorter( String fin, JTextArea out )
    {
        inFile = fin;
        output = out;
        loadData();
    }
    
    /*
     * pre: inFile must name an accessible text-file containing player data.
     * post: Reads in data to Player[] array play.
     */
    private void loadData()
    {
        try
        {
            BufferedReader reader = new BufferedReader( new FileReader( inFile ) );
            
            // count number of lines
            String s = reader.readLine();
            int count = 0;
            while ( s != null )
            {
                count++ ;
                s = reader.readLine();
            }
            reader.close();
            
            // load data to array
            play = new Player[count];
            reader = new BufferedReader( new FileReader( inFile ) );
            s = reader.readLine();
            int idx = 0;
            while ( s != null )
            {
                String[] data = s.split( "\\s+" );
                int number = Integer.parseInt( data[0] );
                String firstName = data[1];
                String lastName = data[2];
                String position = data[3];
                int weight = Integer.parseInt( data[6] );
                
                Player p = new Player( number, firstName, lastName, position,
                                      weight );
                play[idx] = p;
                idx++ ;
                
                s = reader.readLine();
            }
        }
        catch ( IOException e )
        {
            System.out.println( "Error loading data: " + e.getMessage() );
        }
    }
    
    /**
     * pre: Player[] array play is filled with player data, output text-area is
     * displayed on screen.
     * 
     * post: Displays all Player[] array content to text-area output.
     */
    public void displayData()
    {
        output.setText( "" );
        for ( int i = 0; i < play.length; i++ )
            output.append( play[i] + "\n" );
    }
    
    public void changeSort(int sortType){
    	for(Player p:play){
    		p.selectSort(sortType);
    	}
    	Shell.sort(play);
    }
}
