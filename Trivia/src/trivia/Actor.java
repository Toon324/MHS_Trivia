package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import aiControls.*;

/**
 * Generic class for all objects in the game.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public abstract class Actor {

	protected Point2D.Float center;
	protected Color drawClr;

	protected double angle;
	protected double rotateVel;// radians/s
	protected Point2D.Float vectVel;// pixels/s

	protected Polygon basePoly, drawPoly;

	protected boolean remove;

	/**
	 * Creates a new Actor.
	 * 
	 * @param p
	 *            Position in ArrayList
	 */

	public Actor() {
		basePoly = new Polygon();
		vectVel = new Point2D.Float(0, 0);
		rotateVel = 0;
		angle = 0;
		center = new Point2D.Float(0, 0);
		drawClr = Color.cyan;
		remove = false;
	}

	public void setBasePoly(Polygon poly) {
		basePoly = poly;
		drawPoly = new Polygon(poly.xpoints, poly.ypoints, poly.npoints);
	}

	public Polygon getDrawnPoly() {
		return drawPoly;
	}

	public Point2D.Float getCenter() {
		return center;
	}

	public Point2D.Float getVelocity() {
		return vectVel;
	}

	public void accelerateRotation(double accel) {
		rotateVel += accel;
	}

	public double getRotateVel() {
		return rotateVel;
	}

	public abstract float getMaxAccel();

	/**
	 * Draws the Actor.
	 * 
	 * @param p
	 *            Corner of the screen to draw in reference to
	 * @param g
	 *            Graphics to be drawn with
	 */
	public void draw(Graphics g) {
		g.setColor(drawClr);
		if (drawPoly == null)
			drawPoly = basePoly;

		g.drawPolygon(drawPoly);
	}

	/**
	 * Moves the Actor.
	 * 
	 * @param w
	 *            Width of window to draw in
	 * @param h
	 *            Height of window to draw in
	 */
	public void move(int ms) {
		setCenter(center.x + (ms / 1000F) * vectVel.x, center.y + (ms / 1000F)
				* vectVel.y);
		if (rotateVel != 0)// used to prevent unnecessary use of polygonal rotation
			rotate((ms / 1000F) * rotateVel);
	}

	/**
	 * Checks to see if Actor is colliding with another Actor.
	 * 
	 * @param other
	 *            Actor to check collision against
	 */
	public void checkCollision(Actor other) {
		return;
	}

	public void setCenter(float x, float y) {
		if (drawPoly != null)
			drawPoly.translate((int) -center.x, (int) -center.y);
		basePoly.translate((int) -center.x, (int) -center.y);
		center = new Point2D.Float(x, y);
		if (drawPoly != null)
			drawPoly.translate((int) center.x, (int) center.y);
		basePoly.translate((int) center.x, (int) center.y);
	}

	/**
	 * Returns the direction that the Actor is facing in radians.
	 * 
	 * @return dir
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Sets the direction that the Actor is facing in radians.
	 * 
	 * @param d
	 *            Direction to set as.
	 */
	public void setAngle(double d) {
		d %= (2 * Math.PI);
		// log("Angle: " + Math.toDegrees(d));
		drawPoly = rotate(basePoly, d);
		angle = d;
	}

	public void rotate(double d) {
		d %= (2 * Math.PI);
		angle += d;
		angle %= (2 * Math.PI);
		drawPoly = rotate(basePoly, angle);
	}

	private Polygon rotate(Polygon myPoly, double d) {
		return applyAffineTransform(myPoly,
				AffineTransform.getRotateInstance(d, center.x, center.y));
	}

	/**
	 * Allows System to print name of object. Returns the name of the Actor
	 * 
	 * @return "Actor"
	 */
	public String toString() {
		return "Actor";
	}

	private static Polygon applyAffineTransform(Polygon poly,
			AffineTransform trans) {
		Point[] points = new Point[poly.npoints];
		for (int i = 0; i < poly.npoints; i++) {
			points[i] = new Point(poly.xpoints[i], poly.ypoints[i]);
		}
		trans.transform(points, 0, points, 0, points.length);
		Polygon newPoly = new Polygon();
		for (int i = 0; i < points.length; i++) {
			newPoly.addPoint(points[i].x, points[i].y);
		}
		return newPoly;
	}

	public static float getAccelToReach(float xDist, float currentVel, float MAX) {
		/*
		 * Time for velocity to reach 0 if it started to slow down:
		 * (currentVel/MAX_ACCEL)
		 * 
		 * Distance to the next solution for y = 0 that will be traveled in t
		 * time if acceleration is reversed: currentVel * t - (MAX_ACCEL/2) *
		 * t^2
		 */
		byte velSign = (byte) ((((Float.floatToIntBits(currentVel) >> 31) & (1)) * 2 - 1) * -1);
		double t = currentVel / MAX;
		float possibleDist = (float) (currentVel * t - (MAX / 2)
				* Math.pow(t, 2));

		if (possibleDist >= xDist * velSign)
			velSign *= -1;
		if (Math.abs(xDist) <= 2 && currentVel <= MAX)
			return -currentVel;
		return MAX * velSign;
	}

	protected static void drawPolyCenterLines(Graphics g, Polygon p,
			Point thisCenter) {
		if (p == null)
			return;

		int x = 0, y = 0, firstX = 0, firstY = 0;
		int res;
		float array[] = new float[6];
		PathIterator iter = p.getPathIterator(new AffineTransform());
		while (!iter.isDone()) {
			res = iter.currentSegment(array);
			switch (res) {
			case PathIterator.SEG_CLOSE:
				array[0] = firstX;
				array[1] = firstY;
			case PathIterator.SEG_LINETO:
				g.drawLine(x, y, (int) thisCenter.x, (int) thisCenter.y);

				x = (int) array[0];
				y = (int) array[1];
				break;
			case PathIterator.SEG_MOVETO:
				x = (int) array[0];
				y = (int) array[1];
				firstX = x;
				firstY = y;
				break;
			}
			iter.next();
		}
	}

	protected static final int MAX_ACTORS = 1000;
	protected static ArrayList<Actor> actors = new ArrayList<Actor>();
	private static ArrayList<Actor> toAdd = new ArrayList<Actor>();
	protected static GameEngine engine;
	private static ExecutorService threadPool = Executors.newCachedThreadPool();

	public static void add(Actor a) {
		if (actors.size() >= MAX_ACTORS) {
			return;
		}
		// engine.log("Added " + a.toString() + " to toAdd");
		toAdd.add(a);
	}

	/**
	 * Moves and checks for death all actors which are alive. Removes all dead
	 * actors.
	 */
	public static void handleActors(int ms) {
		ArrayList<Actor> toRemove = new ArrayList<Actor>();

		// adds Actors toAdd
		Actor[] addArray = toAdd.toArray(new Actor[0]);
		for (int x = 0; x < addArray.length; x++) {
			Actor a = addArray[x];
			actors.add(a);
		}
		toAdd.clear();

		// collects to be removed actors in an array and moves active ones,
		// checking for collisions
		for (Actor a : actors) {
			if (a.remove) {
				toRemove.add(a);
			} else {
				a.move(ms);
				// check for collisions
				threadPool.execute(new CollisionThread(a, actors
						.toArray(new Actor[0])));
			}
		}
		// removes dead actors from the main array
		for (Actor a : toRemove) {
			if (!actors.remove(a))
				GameEngine.log("Error in removing actor " + a);
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
	public static void drawActors(Graphics g) {
		for (Actor a : actors.toArray(new Actor[0])) {
			a.draw(g);
		}
	}

	public static void setEngine(GameEngine e) {
		engine = e;
	}
}
