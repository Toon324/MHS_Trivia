package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Handles all the game logic and painting
 *         based on the current game mode.
 *         
 * @author Cody Swendrowski, Dan Miller 
 */
public class GameEngine {

	private GameMode mode;
	public MainMenu mainMenu;
	public MainGame mainGame;
	public Instructions instructions;
	public EndGame endGame;
	public Sandbox sandbox;
	private long millis;
	int windowWidth, windowHeight;
	protected int score;
	Font large = new Font("Serif", Font.BOLD, 30);
	
	public static Point envSize = new Point(0, 0);
	public static PrintWriter debugWriter;
	public static boolean debugMode;
	
	ArrayList<Long> stepTimes;
	double FPS;
	int frames;

	static{
		debugMode = true;
		File file = new File("src\\trivia\\Resources\\log.txt");
		try {
			if(!file.exists()) file.createNewFile();
			debugWriter = new PrintWriter(new FileWriter(file.getAbsoluteFile()));
		} catch (IOException e) {
			System.out.println("Error creating debug output stream\n" + System.getProperty("user.dir"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new GameEngine. Must use setMode before execution
	 * 
	 * @param debug
	 *            If true, prints out debug messages.
	 */
	public GameEngine(boolean debug) {
		Actor.setEngine(this);
		debugMode = debug;
		mainMenu = new MainMenu(this);
		endGame = new EndGame(this);
		sandbox = new Sandbox(this);
		instructions = new Instructions(this);
		windowWidth = 800;
		windowHeight = 600;
		millis = System.currentTimeMillis();
		stepTimes = new ArrayList<Long>();
		stepTimes.add(millis);
		score = 0;
	}

	/**
	 * Runs the game logic based on the current game mode.
	 */
	public void run() {
		long lastMillis = millis;
		millis = System.currentTimeMillis();
		stepTimes.add(millis - lastMillis);
		if (stepTimes.size() > 10)
			stepTimes.remove(0);

		mode.run((int) (millis - lastMillis));
		FPS = 1000F/average(stepTimes);	
	}

	private double average(ArrayList<Long> list) {
		double avg = 0;
		int num = 0;
		for (long l : list) {
			avg += l;
			num++;
		}
		return (avg / num);
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
					.getAudioInputStream(this.getClass()
							.getResourceAsStream("Resources\\" + s));
			clip.open(inputStream);
			if (loop) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				clip.start();
			}
			log("Now playing " + s);
		} catch (Exception e) {
			log("Could not load sound clip " + s + " Error: " + e.toString());
		}
	}
	
	public void onClose(){
		log("Closing writer");
		debugWriter.close();
	}

	/**
	 * Debug tool. Used to print a String if Debug mode is enabled.
	 * 
	 * @param s
	 *            String to print.
	 */
	public static void log(String s) {
		if (debugMode) {
			System.out.println(s);
		}
		debugWriter.println(s);
	}
}
