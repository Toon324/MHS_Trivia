package trivia;

import java.awt.Graphics;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
		mainGame = new MainGame(this);
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
		actors.drawActors(g);
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
	 * Uses AudioSystem to get a clip of name s and play it.
	 * 
	 * @param s
	 *            Name of sound clip to play
	 * @param loop If true, loops sound forever
	 */
	public void playSound(String s, boolean loop) {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(Actors.class
							.getResourceAsStream("Resources\\" + s));
			clip.open(inputStream);
			if (loop)
			{
				clip.loop(clip.LOOP_CONTINUOUSLY);
			}
			else
			{
				clip.start();
			}
			log("Now playing " + s);
		} catch (Exception e) {
			log("Could not load sound clip " + s + " Error: " + e.toString());
		}
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
