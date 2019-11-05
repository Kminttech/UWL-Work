
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

@SuppressWarnings("serial")
public class Project1 extends JFrame implements ActionListener, ChangeListener {

	public static void main(String[] args) {
		Project1 frame = new Project1();
		frame.setVisible(true);
	}
	
	private CAPanel caPanel;
	public JSlider gridSizeSlider;
	private JButton save, load;
	private JButton clear, random, step, run;
	private JPanel ctrlPanel;
	private JPanel topPad, rightPad;
	java.util.Timer timer;
	
	public Project1() {
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		pack();
		setSize(569, 549);
		this.setTitle("Project 1: Cellular Automata");
		
		caPanel = new CAPanel(50);
		caPanel.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().add(caPanel, BorderLayout.CENTER);
		
		gridSizeSlider = new JSlider(JSlider.VERTICAL, 5, 100, caPanel.getGridSize());
		gridSizeSlider.setMajorTickSpacing(10);
		gridSizeSlider.setMinorTickSpacing(5);
		gridSizeSlider.setPaintTicks(true);
		gridSizeSlider.setPaintLabels(true);
		gridSizeSlider.addChangeListener(this);
		this.add(gridSizeSlider, BorderLayout.WEST);
				
		ctrlPanel = new JPanel();
		ctrlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		clear = new JButton("Clear");
		clear.addActionListener(this);
		ctrlPanel.add(clear);
		
		random = new JButton("Random");
		random.addActionListener(this);
		ctrlPanel.add(random);
		
		step = new JButton("Step");
		step.addActionListener(this);
		ctrlPanel.add(step);
		
		run = new JButton("Run");
		run.addActionListener(this);
		ctrlPanel.add(run);

		save = new JButton("Save");
		save.addActionListener(this);
		ctrlPanel.add(save);
		
		load = new JButton("Load");
		load.addActionListener(this);
		ctrlPanel.add(load);
		
		this.add(ctrlPanel, BorderLayout.SOUTH);
		
		topPad = new JPanel();
		this.add(topPad, BorderLayout.NORTH);
		rightPad = new JPanel();
		this.add(rightPad, BorderLayout.EAST);
		
	}

	private void run() {
		timer = new java.util.Timer();
		caPanel.setInteractive(false);
		timer.scheduleAtFixedRate(
			new TimerTask() {
				public void run() {
					caPanel.step();
				}
			}, 0, 200);
		this.run.setText("Pause");
		this.save.setEnabled(false);
		this.load.setEnabled(false);
		this.gridSizeSlider.setEnabled(false);
		this.clear.setEnabled(false);
		this.random.setEnabled(false);
		this.step.setEnabled(false);
	}
	
	private void pause() {
		if(timer!=null) {
			timer.cancel();
		}
		this.run.setText("Run");
		this.save.setEnabled(true);
		this.load.setEnabled(true);
		this.gridSizeSlider.setEnabled(true);
		this.clear.setEnabled(true);
		this.random.setEnabled(true);
		this.step.setEnabled(true);
		this.caPanel.setInteractive(true);
	}
	///////////////////////////////////////////////////////////////////////////
	// Implement the ActionListener interface.
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==this.clear) {
			caPanel.clear();
		} else if(e.getSource()==this.random) {
			caPanel.random();
		} else if(e.getSource()==this.step) {
			caPanel.step();
		} else if(e.getSource()==this.save) {
			caPanel.save();
		} else if(e.getSource()==this.load) {
			caPanel.load();
			this.gridSizeSlider.setValue(this.caPanel.getGridSize());
			this.gridSizeSlider.invalidate();
			this.gridSizeSlider.repaint();
		} else if(e.getSource()==this.run) {
			if(this.run.getText().equals("Run")) {
				run();
			} else {
				pause();
			}
		}
		this.repaint();
	}
	// Done implementing the ActionListener interface.
	///////////////////////////////////////////////////////////////////////////
	
	///////////////////////////////////////////////////////////////////////////
	// Implement the ChangeListener interface.
	public void stateChanged(ChangeEvent e) {
		if(e.getSource()==this.gridSizeSlider) {
			this.caPanel.setGridSize(this.gridSizeSlider.getValue());
		} 
	}
	// Done implementing the ChangeListener interface.
	///////////////////////////////////////////////////////////////////////////
}

@SuppressWarnings("serial")
class CAPanel extends JPanel implements MouseListener {

	CA ca;

	private Color gridColor = new Color(230, 230, 230);
	private Color cellColor = Color.black;

	public CAPanel(int gridSize){
		ca = new CA(gridSize);
		setInteractive(true);
	}

	public void paintComponent(Graphics g) {

		int size = ca.getGridSize();
		double xGridSize = getWidth() / (double)size;
		double yGridSize = getHeight() / (double)size;

		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(gridColor);
		for (int i = 0; i <= size; i++) {
			g.drawLine((int) (i * xGridSize), 0, (int) (i * xGridSize), (int) (yGridSize * size));
		}
		for (int i = 0; i <= size; i++) {
			g.drawLine(0, (int) (i * yGridSize), (int) (xGridSize * size), (int) (i * yGridSize));
		}

		g.setColor(cellColor);
		for (int i = 0; i < size; i++) { // columns
			for (int j = 0; j < size; j++) {  // rows
				if (ca.isAlive(j, i)) {
					g.fill3DRect((int) (i * xGridSize + 2), (int) (j * yGridSize + 2), (int) (xGridSize - 2), (int) (yGridSize - 2), true);
				}
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	// Grid Size
	public int getGridSize() { return ca.getGridSize(); }
	
	public void setGridSize(int size) {
		ca.setGridSize(size);
		repaint();
	}
	
///////////////////////////////////////////////////////////////////////////
	
	
	///////////////////////////////////////////////////////////////////////////
	// operations on panel
	public void clear() {
		this.ca.clear();
		repaint();
	}
	
	public void random() {
		this.ca.random();
		repaint();
	}
	
	public void step() {
		this.ca.step(); 
		repaint();
	}
	
	public void save() {
		this.ca.save();
		repaint();
	}
	
	public void load() {
		this.ca.load();
		this.setGridSize(this.ca.getGridSize());
		repaint();
	}
	
	public void setInteractive(boolean flag) {
		if(flag) {
			this.addMouseListener(this);
		} else {
			this.removeMouseListener(this);
		}
	}
	// done with operations on panel
	///////////////////////////////////////////////////////////////////////////
	
	///////////////////////////////////////////////////////////////////////////
	// Implement the MouseListenerInterface
	public void mouseClicked(MouseEvent e) {
		invert(e.getX(), e.getY());
		repaint();
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	// Done implementing MouseListenerInterface
	///////////////////////////////////////////////////////////////////////////

	// utility used to process mouse coordinates
	private void invert(int x, int y) {
		int size = ca.getGridSize();
		int xIndex = x * size / getWidth();
		int yIndex = y * size / getHeight();

		if (ca.isAlive(yIndex, xIndex)) {
			ca.setDead(yIndex, xIndex);
		} else {
			ca.setAlive(yIndex, xIndex);
		}
	}
}

