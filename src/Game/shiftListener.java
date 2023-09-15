package Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class shiftListener extends KeyAdapter {

	private GameInterface game;
	
	public shiftListener(GameInterface game) {
		this.game = game;
	}


	public void keyPressed(KeyEvent e){
		switch( e.getKeyCode() ) { 
        case KeyEvent.VK_UP:
            this.game.shiftUp();
            this.game.updateGridInterface();
            break;
        case KeyEvent.VK_DOWN:
        	this.game.shiftDown();
        	this.game.updateGridInterface();
            break;
        case KeyEvent.VK_LEFT:
        	this.game.shiftLeft();
        	this.game.updateGridInterface();
            break;
        case KeyEvent.VK_RIGHT :
        	this.game.shiftRight();
        	this.game.updateGridInterface();
            break;
     }
	}
}
