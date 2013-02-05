package trivia;

import java.awt.*;
import java.awt.geom.Point2D;
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
	private ArrayList<Actor> toAdd = new ArrayList<Actor>();
	private Boolean debugMode = false;
	private GameEngine engine;

	/*
	 * //Networking private InetAddress serverName; private int port = 324;
	 * private ServerSocket socket; private Socket connection; private
	 * DataInputStream input; private DataOutputStream output; private boolean
	 * hasConnection = false;
	 */

	/**
	 * Creates a new container of Actor.
	 */
	public Actors(Boolean debug) {
		debugMode = debug;
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
		//engine.log("Added " + a.toString() + " to toAdd");
		toAdd.add(a);
	}

	/**
	 * Moves and checks for death all actors which are alive. Removes all dead
	 * actors.
	 */
	public void handleActors(int ms) {
		ArrayList<Actor> toRemove = new ArrayList<Actor>(); // dead objects to be removed
		ArrayList<Point2D.Float> particles = new ArrayList<Point2D.Float>();
		
		// adds Actors toAdd
		for (Actor a : toAdd) {
			//engine.log("Adding " + a.toString());
			a.setActors(this);
			actors.add(a);
		}
		
		// collects dead actors in an array and moves live ones, checking for collisions
		for (Actor a : actors) {
			if (a.isDead())
			{
				toRemove.add(a);
				if (!(a instanceof Particle))
					particles.add(a.getCenter());
			}
			else {
				a.move(ms);
				// check for collisions
				for (Actor b : actors) {
					if (!a.equals(b));
						a.checkCollision(b);
				}
			}
				
		}
		// removes dead actors from the main array
		for (Actor a : toRemove) {
			if (!actors.remove(a))
				log("Error in removing actor " + a);
		}
		
		// Spawns particle explosions
		for (Point2D.Float p : particles) {
			engine.particleEngine.spawnRandomExplosion(p);
		}

		//engine.log("-----------------------------------------");
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

		for (Actor a : actors) {
			a.draw(g);
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

	public void addTriangle(int x, int y) {
		Triangle c = new Triangle(debugMode, engine);
		c.setCenter(x, y);
		add(c);
	}

	public void addSquare(int x, int y) {
		Square c = new Square(debugMode, engine);
		c.setCenter(x, y);
		add(c);
	}
	
	public void addParticle(Point2D.Float center, Point2D.Float vectorSpeed, double alphaDecayRate, double speedDecayRate, Color c)
	{
		Particle p = new Particle(debugMode, engine, vectorSpeed, alphaDecayRate, speedDecayRate, c);
		p.setCenter(center);
		add(p);
	}
	
	public void setEngine(GameEngine e)
	{
		engine = e;
	}

	/**
	 * Debug tool. Used to print a String.
	 * 
	 * @param s
	 *            String to print.
	 */
	private void log(String s) {
		if (debugMode) {
			System.out.println(s);
		}
	}
}
