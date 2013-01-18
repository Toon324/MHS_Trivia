package trivia;

import java.awt.Graphics;

/**
 * @author Cody Swendrowski, Dan Miller
 * Handles all the game logic and painting based on the current game mode.
 */
public class GameEngine {

	private GameMode mode;
	public MainMenu mainMenu;
	public MainGame mainGame;
	private Actors actors;
	private int timer = 0;
	int windowWidth, windowHeight;
	private boolean debugMode;

	/**
	 * Creates a new GameEngine
	 * @param actors Array of actors to pass logic to.
	 * @param debug If true, prints out debug messages.
	 */
	public GameEngine(Actors actors, Boolean debug) {
		this.actors = actors;
		debugMode = debug;
		mainMenu = new MainMenu(this);
		mode = mainMenu;
		windowWidth = 800;
		windowHeight = 600;
	}

	/**
	 * Runs the game logic based on the current game mode.
	 */
	public void run() {
		timer++;
		mode.run();
		actors.handleActors();
	}

	/**
	 * Paints the game based on the current game mode.
	 * @param g Graphics to paint with.
	 */
	public void paint(Graphics g) {
		mode.paint(g);
	}

	/**
	 * Sets the current game mode.
	 * @param newMode
	 */
	public void setMode(GameMode newMode) {
		mode = newMode;
	}

	/**
	 * Called when a click occurs, sends the click to the current
	 * gameMode.
	 * 
	 * @param x
	 * @param y
	 */
	public void clickedAt(int x, int y) {
		mode.clicked(x, y);
	}
	
	public void setWindowSize(int width, int height)
	{
		windowWidth = width;
		windowHeight = height;
	}

	/**
	 * Debug tool. Used to print a String if Debug mode is enabled.
	 * 
	 * @param s
	 *            String to print.
	 */
	public void log(String s) {
		if (debugMode) {
			System.out.println(s);
		}
	}
}
