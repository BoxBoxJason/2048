package Game;

import Grid.Grid;
import Resources.Images;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;

public class GameInterface {
	/*
	 * Game interface class, contains all game related methods and attributes
	 * Also contains main
	 */
	
	//2048 Grid object
	private Grid grid;
	//Gameboard JPanel
	private GameFrame gameframe;
	//Array of all placed numbers JLabels
	private JLabel[][] labels;
	//Grid size
	private int size;
	//Game score
	private int score;
	//Game score JLabel
	private JLabel scoreLabel;
	

	public GameInterface(int size) {
		/*
		 * Class constructor, creates the grid layout and the game grid
		 */
		this.gameframe = new GameFrame();
		this.gameframe.setLayout(new GridLayout(size,size,0,0));
		this.grid = new Grid(size,this);
		this.size = size;
		this.score = 0;
		this.labels = new JLabel[size][size];
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				JLabel square = new JLabel();
				this.labels[i][j] = square;
				this.gameframe.add(square);
			}
		}
		this.gameframe.repaint();
	}
	
	
	public static void main(String[] args) {
		JFrame D2048 = new JFrame("2048");
		ImageIcon icon = new ImageIcon(Images.class.getResource("2048.png"));
		D2048.setIconImage(icon.getImage());
		//Creating game interface
		GameInterface game = new GameInterface(4); //Change grid size here
		
		//Creating game frame
		D2048.addKeyListener(new shiftListener(game));
		D2048.setLayout(new GridBagLayout());
		D2048.getContentPane().setBackground(new Color(33, 0, 46));
		GridBagConstraints layoutConstraints = new GridBagConstraints();
		layoutConstraints.fill = GridBagConstraints.VERTICAL;
		layoutConstraints.gridx = 0;
		
		//Creating score variables
		JLabel score = new JLabel();
		game.scoreLabel = score;
		game.addToScore(0);
		
		//Adding elements to layout and packing frame
		layoutConstraints.gridy = 0;
		layoutConstraints.weighty = 0.1;
		D2048.add(score,layoutConstraints);
		
		layoutConstraints.gridy = 1;
		layoutConstraints.weighty = 0.9;
		D2048.add(game.gameframe,layoutConstraints);
		D2048.pack();
		game.updateGridInterface();
		
		D2048.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		D2048.setVisible(true);
	}
	
	
	public void updateGridInterface() {
		/*
		 * updates grid display interface according to the square of the Grid object
		 */
		
		//Update new grid
		for(int i=0;i<this.size;i++) {
			for(int j=0;j<this.size;j++) {
				int number = this.grid.getSquare(i, j);
				String imagePath = "";
				if(number != 0) {
					imagePath += String.valueOf(number) + ".png";
				}
				else {
					imagePath += "Square.png";
				}
				BufferedImage tmpImage = null;
				try {
					tmpImage = ImageIO.read(Images.class.getResource(imagePath));
				}
				catch (IOException e) {
				    e.printStackTrace();
				}
				Image resizedImage = tmpImage.getScaledInstance(this.labels[i][j].getWidth(),
						this.labels[i][j].getHeight(),Image.SCALE_SMOOTH);
				ImageIcon icon = new ImageIcon(resizedImage);
				this.labels[i][j].setIcon(icon);
			}
		}
		this.gameframe.repaint();
	}
	
	
	public void shiftLeft() {this.grid.shiftLeft();}
	public void shiftRight() {this.grid.shiftRight();}
	public void shiftUp() {this.grid.shiftUp();}
	public void shiftDown() {this.grid.shiftDown();}

	public Grid getGrid() {return(this.grid);}
	
	public void addToScore(int toAdd) {
		/*
		 * Updates score display
		 */
		this.score += toAdd;
		String labelText = "<html><font color='white'><font size='+1'>Score: <font size='+1'><strong>" + String.valueOf(this.score) + "</font></strong></html>";
		this.scoreLabel.setText(labelText);
		this.gameframe.repaint();
	}
}
