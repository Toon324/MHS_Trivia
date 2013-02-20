package trivia;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Actors.Actor;


/**
 * 
 * @author Cody Swendrowski, Dan Miller Abstract class that contains all of the
 *         necessary methods for GameModes.
 */

public abstract class GameMode {

	public ArrayList<Button> buttons;
	public GameEngine engine;
	protected Image background;

	/**
	 * Creates a new GameMode.
	 * 
	 * @param eng
	 *            GameEngine to pass data back to.
	 */
	public GameMode(GameEngine eng) {
		engine = eng;
		buttons = new ArrayList<Button>();
		try {
			background = ImageIO.read(MainMenu.class
					.getResourceAsStream("Resources\\space_background.jpg"));
		} catch (IOException e) {
			GameEngine.log("Can not load background image.");
		}
	}

	/**
	 * Runs the logic of the GameMode.
	 */
	public void run(int ms) {
		//intentionally modifies the object, rather than re-instantianizing
		GameEngine.setEnvSize(engine.windowWidth, engine.windowHeight);
		Actor.handleActors(ms);
	}

	public void mouseMoved(int x, int y) {
	}

	/**
	 * Recieves MouseClick data.
	 * 
	 * @param x
	 *            MouseClick X value
	 * @param y
	 *            MouseClick Y value
	 */
	public void clicked(MouseEvent e) {
		try {
			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).checkClick(e.getX(), e.getY());
			}
		} catch (java.lang.NullPointerException except) {
			GameEngine.log("No defined buttons in " + this.toString());
		}
	}

	/**
	 * Paints the necessary components in GameMode.
	 * 
	 * @param g
	 *            Graphics to paint with
	 */
	public void paint(Graphics g) {
		try {
			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).draw(g);
			}
		} catch (java.lang.NullPointerException e) {
			GameEngine.log("No defined buttons in " + this.toString());
		}
	}

}
