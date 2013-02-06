package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * @author Cody Swendrowski, Dan Miller Handles all the game logic and painting
 *         based on the current game mode.
 */
public class GameEngine {

	private GameMode mode;
	public MainMenu mainMenu;
	public MainGame mainGame;
	public Instructions instructions;
	public EndGame endGame;
	public Sandbox sandbox;
	public ParticleEngine particleEngine;
	Actors actors;
	private long millis, lastMillis, lastBench;
	int windowWidth, windowHeight;
	protected int score;
	private boolean debugMode;
	Font large = new Font("Serif", Font.BOLD, 30);

	ArrayList<Long> stepTimes;
	double FPS;
	int frames;

	/**
	 * Creates a new GameEngine
	 * 
	 * @param actors
	 *            Array of actors to pass logic to.
	 * @param debug
	 *            If true, prints out debug messages.
	 */
	public GameEngine(Actors actors, Boolean debug) {
		this.actors = actors;
		actors.setEngine(this);
		debugMode = debug;
		mainMenu = new MainMenu(this);
		endGame = new EndGame(this);
		sandbox = new Sandbox(this);
		particleEngine = new ParticleEngine(this);
		instructions = new Instructions(this);
		mode = mainMenu;
		windowWidth = 800;
		windowHeight = 600;
		millis = System.currentTimeMillis();
		lastMillis = millis;
		lastBench = millis;
		stepTimes = new ArrayList<Long>();
		stepTimes.add(millis);
		score = 0;
	}

	public ParticleEngine getParticleEngine() {
		if (particleEngine == null)
		{
			log("PE is null");
			particleEngine = new ParticleEngine(this);
		}
		return particleEngine;
	}

	/**
	 * Runs the game logic based on the current game mode.
	 */
	public void run() {
		millis = System.currentTimeMillis();
		stepTimes.add(millis - lastMillis);
		stepTimes.add(millis - lastMillis);
		if (stepTimes.size() > 10)
			stepTimes.remove(0);

		mode.run((int) (millis - lastMillis));
		lastMillis = millis;
		frames++;
		if (millis - lastBench >= 1000)
		{
			FPS = frames;
			frames = 0;
			lastBench = millis;
		}	
	}

	private double average(ArrayList<Long> list) {
		double avg = 0;
		for (long l : list) {
			avg += l;
		}
		return (avg / list.size());
	}

	/**
	 * Paints the game based on the current game mode.
	 * 
	 * @param g
	 *            Graphics to paint with.
	 */
	public void paint(Graphics g) {
		mode.paint(g);
		g.setFont(large);
		g.setColor(Color.blue);
		if(debugMode){
			g.drawString(String.format("%6.2f", FPS), 10, 30);
		}
	}

	/**
	 * Sets the current game mode.
	 * 
	 * @param newMode
	 */
	public void setMode(GameMode newMode) {
		mode = newMode;
	}

	/**
	 * Called when a click occurs, sends the click to the current gameMode.
	 * 
	 * @param x
	 * @param y
	 */
	public void clickedAt(MouseEvent e) {
		mode.clicked(e.getX(), e.getY());
	}

	public void MouseMoved(MouseEvent e) {
		mode.mouseMoved(e.getX(), e.getY());
	}

	public void setWindowSize(int width, int height) {
		windowWidth = width;
		windowHeight = height;
	}

	/**
	 * Uses AudioSystem to get a clip of name s and play it.
	 * 
	 * @param s
	 *            Name of sound clip to play
	 * @param loop
	 *            If true, loops sound forever
	 */
	public void playSound(String s, boolean loop) {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(Actors.class
							.getResourceAsStream("Resources\\" + s));
			clip.open(inputStream);
			if (loop) {
				clip.loop(clip.LOOP_CONTINUOUSLY);
			} else {
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
