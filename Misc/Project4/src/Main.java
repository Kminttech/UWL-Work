import javax.swing.JFileChooser;

// Description: Runs WordCloud() to create a word cloud
// display of words from a given input file; words and counts
// written to given output file (in word-sorted order).

public class Main
{
    public static void main( String[] args )
    {
        final String commonWordsFile = "commonest.txt"; 
		JFileChooser loader = new JFileChooser();
		loader.setDialogTitle("Chose Source");
		int reply = loader.showOpenDialog(null);
		if(reply == JFileChooser.APPROVE_OPTION){
	        WordCloud wc = new WordCloud();
	        wc.makeCloud( commonWordsFile, loader.getSelectedFile().getAbsolutePath());
		}else{
			System.out.println("Source file not Selected");
		}
    }
}