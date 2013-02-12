package trivia;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private ExecutorService threadPool = Executors.newCachedThreadPool();

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
		//engine.log("Added " + a.toString() + " to toAdd");
		toAdd.add(a);
	}

	private int evade;
	private boolean canFire;
	/**
	 * Moves and checks for death all actors which are alive. Removes all dead
	 * actors.
	 */
	public void handleActors(int ms) {
		ArrayList<Actor> toRemove = new ArrayList<Actor>(); // dead objects to be removed
		ArrayList<Point2D.Float> particles = new ArrayList<Point2D.Float>();

		// adds Actors toAdd
		Object[] addArray = toAdd.toArray();
		for (int x=0; x<addArray.length; x++) {
			Actor a = (Actor) addArray[x];
			a.setActors(this);
			actors.add(a);
		}
		toAdd.clear();
		
		// collects dead actors in an array and moves live ones, checking for collisions
		for (Object b : actors.toArray()) {
			Actor a = (Actor) (b);
			//engine.log("Handling " + a.toString());
			if (a.isDead())
			{
				toRemove.add(a);
				if (!(a instanceof Particle))
					particles.add(a.getCenter());
			}
			else {
				a.move(ms);
				if (a instanceof Triangle)
					((Triangle) a).setEvade(evade);
				if (a instanceof FightingActor && canFire)
					((FightingActor) a).fire();
				// check for collisions
				threadPool.execute(new CollisionThread(a,actors.toArray()));
			}
				
		}
		// removes dead actors from the main array
		for (Object b : toRemove.toArray()) {
			Actor a = (Actor) (b);
			if (!actors.remove(a))
				GameEngine.log("Error in removing actor " + a.toString());
		}
		
		// Spawns particle explosions
		for (Point2D.Float p : particles) {
			engine.particleEngine.spawnRandomExplosion(p);
		}
		canFire = false;
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
		for (Object a : actors.toArray()) {
			PainterThread p = new PainterThread((Actor)(a),g);
			threadPool.execute(p);
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

	public void addTriangle(int destination, int x, int y) {
		Triangle c = new Triangle(debugMode, engine, destination);
		c.setCenter(x, y);
		add(c);
	}

	public void addSquare(int destination, int x, int y) {
		Square c = new Square(debugMode, engine, destination);
		c.setCenter(x, y);
		add(c);
	}
	
	public void addParticle(Point2D.Float center, Point2D.Float vectorSpeed, Color c)
	{
		Particle p = new Particle(debugMode, engine, vectorSpeed, c);
		p.setCenter(center);
		add(p);
	}
	
	public void setEngine(GameEngine e)
	{
		engine = e;
	}
	
	public void setEvade(int e) {
		evade = e;
	}

	public void fireBullet(Point2D.Float center, Color drawClr, Point2D.Float velocity) {
		Bullet b = new Bullet(debugMode, engine, velocity, drawClr);
		b.setCenter(center.x, center.y);
		add(b);
	}

	public void fire() {
		canFire = true;	
	}
}
