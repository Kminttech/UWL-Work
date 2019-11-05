import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

//Kevin Minter Assembler.java
public class Assembler implements ActionListener {
	
	private JFrame window;
	private JButton chooseButton;
	private JLabel fileNameLabel;
	private JFileChooser chooser;
	
	private int intA;
	private int intB;
	    
	public static void main(String[] args) {
		Assembler tester = new Assembler();
        tester.makeWindow();
	}
	
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
	
	
	@Override
    public void actionPerformed( ActionEvent e )
    {
        int returnVal = chooser.showOpenDialog( window );
        if ( returnVal == JFileChooser.APPROVE_OPTION )
        {
            File inFile = chooser.getSelectedFile();
            fileNameLabel.setText( " " + inFile.getName() );
            readFile(inFile);
        }
    }
	
	public void readFile(File inFile){
		try{
			Scanner input = new Scanner(inFile);
			LinkedList<String> codeLines = new LinkedList<String>();
			int line = 0;
			while(input.hasNextLine()){
				codeLines.add(input.nextLine());
			}
			
			while(line >= 0 && line < codeLines.size()){
				line += processLine(codeLines.get(line)); 
			}
			
			System.out.println("A: " + intA + ",B: " + intB);
			input.close();
		}catch(IOException e){
			System.out.println("No File Selected.");
		}
	}
	
	public int processLine(String line){
		Scanner lineScan = new Scanner(line);
		String action = lineScan.next();
		if(action.equals("ADD")){
			String register = lineScan.next();
			int change = lineScan.nextInt();
			if(register.equals("A")){
				if(intA + change < 0){
					if(change < 0){
						intA = Integer.MAX_VALUE;
					}else{
						intA = 0;
					}
				}else{
					intA = intA + change;
				}
			}else if(register.equals("B")){
				if(intB + change < 0){
					if(change < 0){
						intB = Integer.MAX_VALUE;
					}else{
						intB = 0;
					}
				}else{
					intB = intB + change;
				}
			}
		}else if(action.equals("INC")){
			String register = lineScan.next();
			if(register.equals("A")){
				if(intA + 1 < 0){
					intA = Integer.MAX_VALUE;
				}else{
					intA++;
				}
			}else if(register.equals("B")){
				if(intB + 1 < 0){
					intB = Integer.MAX_VALUE;
				}else{
					intB++;
				}
			}
		}else if(action.equals("JIG")){
			String register1 = lineScan.next();
			String register2 = lineScan.next();
			int lineChange = lineScan.nextInt();
			if(register1.equals("A")){
				if(register2.equals("A")){
					lineScan.close();
					return lineChange;
				}else if(register2.equals("B")){
					if(intA >= intB){
						lineScan.close();
						return lineChange;
					}
				}
			}else if(register1.equals("B")){
				if(register2.equals("B")){
					lineScan.close();
					return lineChange;
				}else if(register2.equals("A")){
					if(intB >= intA){
						lineScan.close();
						return lineChange;
					}
				}
			}
		}else if(action.equals("SET")){
			String register = lineScan.next();
			int value = lineScan.nextInt();
			if(register.equals("A")){
				intA = value;
			}else if(register.equals("B")){
				intB = value;
			}
		}
		lineScan.close();
		return 1;
	}
}
