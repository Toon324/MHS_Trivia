package trivia;

import java.awt.*;
import java.util.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Container for all Actors. Handles updating and drawing of all contained
 * Actors.
 * 
 * @author Cody Swendrowski
 */
public class Actors {
	private GameImage ship;
	private ArrayList<Actor> actors = new ArrayList<Actor>();
	private int pos, playerX, playerY;
	private Font normal = new Font ("Serif", Font.BOLD, 20);
	private Boolean debugMode = false;

	/*
	//Networking
	private InetAddress serverName;
    private int port = 324;
    private ServerSocket socket;
    private Socket connection;
    private DataInputStream input;
    private DataOutputStream output;
    private boolean hasConnection = false;
    */
	
    final int MAX = 50;

	private int score, width, height;

	/**
	 * Creates a new container of Actor.
	 */
	public Actors(Boolean debug) {
		pos = 0;
		playerX = 0;
		playerY = 0;
		debugMode = debug;
		try {
			ship = new GameImage("ship");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a created Actor to actors.
	 * 
	 * @param a
	 *            Actor to be added
	 */
	public void add(Actor a) {
		if (pos >= MAX) {
			return;
		}
		actors.add(a);
		pos++;
	}
	
	/**
	 * Moves and checks for death all actors which are alive. Removes all dead
	 * actors. Spawns explosions at newly dead actors.
	 */
	public void handleActors() {
		int update = 0; // used to update positions
		//Point[] toAdd = new Point[10]; //Actors to add
		//int add1 = 0; // manages adding
		ArrayList<Integer> toRemove = new ArrayList<Integer>(); // dead objects
																// to be removed
		
		// Moves objects and checks if dead
		for (Iterator<Actor> iter = actors.iterator(); iter.hasNext();) {
			Actor temp = iter.next();
			// updates the object's position in the array
			temp.setPos(temp.getPos() + update);

			// removes dead objects
			if (temp.isDead()) {
					update--;
			} 
			else // if object isn't dead, move it and check for collision
			{
				temp.move(width,height);
				} 

				// check for collisions
				for (Iterator<Actor> iter2 = actors.iterator(); iter2.hasNext();) {
					Actor o = iter2.next();
					if (!o.isDead()) {
						((Actor) temp).checkCollision(o);
					}
				}
			}

			// removes dead actors
			for (Iterator<Integer> iter = toRemove.iterator(); iter.hasNext();) {
				int temp = iter.next().intValue();
				try {
					actors.remove(temp);
					pos--;
					// System.out.println(actors.get(temp).toString() + " removed");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Calls the draw method of all objects.
	 * 
	 * @param g
	 *            Graphics to be drawn with
	 * @param i
	 *            ImageObserver to be reported to
	 */
	public void drawActors(Graphics g) {
		Actor[] sorted = new Actor[actors.size()];
		for (int x = 0; x < sorted.length; x++) {
			sorted[x] = null;
		}
		drawFloor(g);
		
		Point p = new Point(playerX, playerY);
		
		for (Iterator<Actor> iter = actors.iterator(); iter.hasNext();) {
			Actor temp = iter.next();
			findPlace(sorted, temp);
		}
		
		for (int x = 0; x < sorted.length; x++) {
			try {
				sorted[x].draw(p, g);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Helper method for Array sorting. Finds logical place for Actor based on it's relative X and Y position.
	 * @param sorted Array that contains Actors that were already sorted.
	 * @param temp Actor to find place for.
	 */
	private void findPlace(Actor[] sorted, Actor temp) {
		int x = temp.getX();
		int y = temp.getY();

		// System.out.println("Sorting:" + sorted.length + " items");

		for (int i = 0; i < sorted.length; i++) {
			// System.out.println("Sorting " + i + " " + sorted[i]);
			if (sorted[i] == null) {
				// System.out.println("Placed " + temp.toString() + " at " + i);
				sorted[i] = temp;
				return;
			}
			if ((y < sorted[i].getY())) {
				Actor temp2 = sorted[i];
				// System.out.println("Removed " + temp2.toString() + " at " +
				// i);
				sorted[i] = temp;
				// System.out.println("Placed " + temp.toString() + " at " + i);
				findPlace(sorted, temp2);
				return;
			}
			if ((!(y > sorted[i].getY())) && (x < sorted[i].getX())) {
				Actor temp2 = sorted[i];
				// System.out.println("Removed " + temp2.toString() + " at " +
				// i);
				sorted[i] = temp;
				// System.out.println("Placed " + temp.toString() + " at " + i);
				findPlace(sorted, temp2);
				return;
			}
		}
	}

	/**
	 * Draws a grid on the Graphics object.
	 * @param g Graphics to draw with.
	 */
	public void drawFloor(Graphics g) {
		Color temp = g.getColor();
		g.setColor(Color.gray);
		g.fillRect(0, 0, 800, 600);
		g.setColor(Color.black);
		int tempX = playerX;
		while (tempX > 100) {
			tempX -= 100;
		}
		while (tempX < 0) {
			tempX += 100;
		}
		for (int x = 0 - tempX; x < 800 + tempX; x += 25) {
			g.drawLine(x, 0, x, 600);
		}

		int tempY = playerY;
		while (tempY > 100) {
			tempY -= 100;
		}
		while (tempY < 0) {
			tempY += 100;
		}
		for (int y = 0 - tempY; y < 600 + tempY; y += 25) {
			g.drawLine(0, y, 800, y);
		}
		g.setColor(temp);
	}

	/**
	 * Returns current ArrayList of actors
	 * 
	 * @return actors
	 */
	public ArrayList<Actor> getArray() {
		return actors;
	}

	/**
	 * Returns score to add, then resets to 0.
	 * 
	 * @return score
	 */
	public int getScore() {
		int scr = score;
		score = 0;
		return scr;
	}

	/**
	 * Returns current ArrayList position.
	 * 
	 * @return pos
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * Used for debugging.
	 * 
	 * @param p
	 *            Position of Actor to return
	 * @return Actor p
	 */
	public Actor get(int p) {
		for (Iterator<Actor> iter = actors.iterator(); iter.hasNext();) {
			if (iter.next().getPos() == p) {
				return iter.next();
			}
		}
		return null;
	}

	/**
	 * Sets the known width and height of area to draw in.
	 * 
	 * @param w
	 *            Width
	 * @param h
	 *            Height
	 */
	public void setSize(int w, int h) {
		width = w;
		height = h;
	}

	/**
	 * Uses AudioSystem to get a clip of name s and play it.
	 * 
	 * @param s
	 *            Name of sound clip to play
	 */
	public void playSound(String s) {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(Actors.class
							.getResourceAsStream("audio\\" + s));
			clip.open(inputStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Debug tool.
	 * Used to print a String.
	 * @param s String to print.
	 */
	private void log(String s) {
		if (debugMode)
		{
			System.out.println(s);
		}
	}
}



