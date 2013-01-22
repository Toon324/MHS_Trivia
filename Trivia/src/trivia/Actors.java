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
 * @author Cody Swendrowski, Dan Miller
 */
public class Actors {
	
	private final int MAX_ACTORS = 50;
	private ArrayList<Actor> actors = new ArrayList<Actor>();
	private int pos, width, height;
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

	/**
	 * Creates a new container of Actor.
	 */
	public Actors(Boolean debug) {
		pos = 0;
		debugMode = debug;
	}

	/**
	 * Adds a created Actor to actors.
	 * 
	 * @param a Actor to be added
	 */
	public void add(Actor a) {
		if (pos >= MAX_ACTORS) {
			return;
		}
		actors.add(a);
		pos++;
	}
	
	/**
	 * Moves and checks for death all actors which are alive. Removes all dead
	 * actors.
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
		
		for (Iterator<Actor> iter = actors.iterator(); iter.hasNext();) {
			Actor temp = iter.next();
			temp.draw(g);
		}
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
	
	public void addTriangle() {
		Triangle c = new Triangle(debugMode, pos);
		c.setCorner(200,200);
		c.setAngle(90);
		add(c);
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

	public void addSinglePoint() {
		SinglePoint sp = new SinglePoint(debugMode, pos);
		sp.setAngle(90);
		add(sp);
	}
}



