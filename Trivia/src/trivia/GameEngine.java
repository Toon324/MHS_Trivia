package trivia;

import java.awt.Graphics;

/**
 * @author Cody Swendrowski
 *
 */
public class GameEngine {

	private GameMode mode;
	public static final MainMenu MAIN_MENU = new MainMenu();
	private Actors actors;
	private int timer = 0;
	private boolean debugMode;

	public interface GameMode{
		public void run();
		public void clicked(int x, int y);
		public void paint(Graphics g);
	}
	
	
	public GameEngine(Actors actors, Boolean debug)
	{
		mode = MAIN_MENU;
		this.actors = actors;
		debugMode = debug;
	}
	
	public void run() {
		timer++;
		mode.run();
		
	}

	public void paint(Graphics g) {
		
		mode.paint(g);
	}
	
	public void setMode(GameMode newMode)
	{
		mode = newMode;
	}
	
	/**
	 * Called when a click occurs, meant to send the update to the current gameMode
	 * @param x
	 * @param y
	 */
	public void clickedAt(int x, int y){
		mode.clicked(x, y);
	}
	
	/**
	 * Debug tool.
	 * Used to print a String if Debug mode is enabled.
	 * @param s String to print.
	 */
	private void log(String s) {
		if (debugMode)
		{
			System.out.println(s);
		}
	}
}
