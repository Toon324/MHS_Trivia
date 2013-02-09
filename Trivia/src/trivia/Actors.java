package trivia;

import java.awt.*;
import java.util.*;

/**
 * Container for all Actors. Handles updating and drawing of all contained
 * Actors.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class Actors {

	private final int MAX_ACTORS = 1000;
	private ArrayList<Actor> actors = new ArrayList<Actor>();

	/*
	 * //Networking private InetAddress serverName; private int port = 324;
	 * private ServerSocket socket; private Socket connection; private
	 * DataInputStream input; private DataOutputStream output; private boolean
	 * hasConnection = false;
	 */

	/**
	 * Creates a new container of Actor.
	 */
	public Actors() {
	}

	/**
	 * Adds a created Actor to actors.
	 * 
	 * @param a
	 *            Actor to be added
	 */
	public void add(Actor a) {
		if (actors.size() >= MAX_ACTORS) {
			return;
		}
		actors.add(a);
		a.setActors(this);
	}

	/**
	 * Moves and checks for death all actors which are alive. Removes all dead
	 * actors.
	 */
	public void handleActors(int ms) {
		ArrayList<Actor> toRemove = new ArrayList<Actor>(); // dead objects
															// to be removed
		// collects dead actors in an array
		for (Actor a : actors) {
			if (a.isDead())
				toRemove.add(a);
		}
		// removes dead actors from the main array
		for (Actor a : toRemove) {
			if (!actors.remove(a))
				GameEngine.log("Error in removing actor " + a);
		}

		// Moves objects and checks for collisions
		for (Actor a : actors) {
			a.move(ms);
			// check for collisions
			for (Actor b : actors) {
				a.checkCollision(b);
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
}
