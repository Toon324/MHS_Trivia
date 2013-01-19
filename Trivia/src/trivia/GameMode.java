package trivia;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * 
 * @author Cody Swendrowski, Dan Miller
 * Abstract class that contains all of the necessary methods for GameModes.
 */

public abstract class GameMode {
	
	public ArrayList<Button> buttons;
	public GameEngine engine;
	protected Image background;
	
	/**
	 * Creates a new GameMode.
	 * @param eng GameEngine to pass data back to.
	 */
	public GameMode(GameEngine eng){
		engine = eng;
		try {
			background = ImageIO.read(MainMenu.class.getResourceAsStream("Resources\\space_background.jpg"));
		} catch (IOException e) {
			eng.log("Can not load background image.");
		}
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
			for(int i = 0; i < buttons.size(); i++){
				buttons.get(i).checkClick(x, y);
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
		
		g.drawImage(background, 0, 0, engine.windowWidth, engine.windowHeight, null);
		try{
			for(int i = 0; i < buttons.size(); i++){
				buttons.get(i).draw(g);
			}
		}catch(java.lang.NullPointerException e){
			engine.log("No defined buttons in " + this.toString());
		}
	}

}
