/***************
 * Description: Creates a text Label with size set based
 * on an Integer input, with a randomly selected color.
 ***************/
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;

@SuppressWarnings( "serial" )
public class WordLabel extends JLabel
{
    // post: creates Label with randomly selected font color,
    // font size set relative to size input,
	// and getText() == s
	// and getBackground() == Color.white
	// s Word to be displayed.
	// size Parameter to ensure more common words appear larger.
    public WordLabel( String s, Integer size )
    {
        super( s );
        setBackground( Color.white );
        setForeground( getRandomColor() );
        int fontSize = 14 + ( 12 * ( size - 1 ) );
        Font font = new Font( "Monospace", Font.PLAIN, fontSize );
        setFont( font );
    }
    
    // returns 1 of 5 Color choices at random 
    private Color getRandomColor()
    {
        int r = (int) ( Math.random() * 4 );
        Color c = Color.black;
        switch ( r )
        {
            case 0:
                c = Color.blue;
                break;
            case 1:
                c = Color.green;
                break;
            case 2:
                c = Color.red;
                break;
            default:
                c = Color.black;
                break;
        }
        return c;
    }
}
