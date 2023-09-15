package Game;

import java.awt.Dimension;

import javax.swing.JPanel;

public class GameFrame extends JPanel {
	/*
	 * Square shaped JPanel, used to display the 2048 game
	 */
	public void reshape(int x, int y, int width, int height) {
		int minTwo = Math.min(width, height);
		super.reshape(x, y, minTwo, minTwo);
	}
	
	public Dimension getMinimumSize() {
		return(new Dimension(400,400));
	}
	
	public Dimension getPreferredSize() {
		return(new Dimension(700,700));
	}
}
