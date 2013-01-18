package trivia;

import java.awt.Graphics;

/**
 * 
 * @author Cody Swendrowski, Dan Miller
 * Abstract class that contains all of the necessary methods for GameModes.
 */

public abstract class GameMode {
	
	public Button[] buttons;
	public GameEngine engine;
	
	/**
	 * Creates a new GameMode.
	 * @param eng GameEngine to pass data back to.
	 */
	public GameMode(GameEngine eng){
		engine = eng;
	}
	
	/**
	 * Runs the logic of the GameMode.
	 */
	public void run(){
	}
	
	/**
	 * Recieves MouseClick data.
	 * @param x MouseClick X value
	 * @param y MouseClick Y value
	 */
	public void clicked(int x, int y){
		try{
			for(int i = 0; i < buttons.length; i++){
				buttons[i].checkClick(x, y);
			}
		}catch(java.lang.NullPointerException e){
			engine.log("No defined buttons in " + this.toString());
		}
	}
	
	/**
	 * Paints the necessary components in GameMode.
	 * @param g Graphics to paint with
	 */
	public void paint(Graphics g){
		try{
			for(int i = 0; i < buttons.length; i++){
				buttons[i].draw(g);
			}
		}catch(java.lang.NullPointerException e){
			engine.log("No defined buttons in " + this.toString());
		}
	}

}
