package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Generic class for all objects in the game.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public abstract class Actor {

	protected boolean debug;
	protected Actors actors;

	protected boolean death;
	protected Point2D.Float center;
	protected Color drawClr;
	protected GameEngine engine;

	// derivative motion values
	protected Point2D.Float vectVel;// pixels/s

	protected Polygon basePoly, drawPoly;

	/**
	 * Creates a new Actor.
	 * 
	 * @param p
	 *            Position in ArrayList
	 */

	public Actor(boolean debugMode, GameEngine e) {
		debug = debugMode;
		basePoly = new Polygon();
		vectVel = new Point2D.Float(0, 0);
		center = new Point2D.Float(0, 0);
		death = false;
		engine = e;
		drawClr = Color.cyan;
	}

	public void setActors(Actors act) {
		actors = act;
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

		drawPoly(g, drawPoly, new Point((int) center.x, (int) center.y), true);
	}

	private void drawPoly(Graphics g, Polygon p, Point thisCenter,
			boolean centerLines) {
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
				g.drawLine(x, y, (int) array[0], (int) array[1]);

				if (centerLines)
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
	}

	/**
	 * Returns true if Actor is dead; else, returns false.
	 * 
	 * @return death
	 */
	public boolean isDead() {
		return death;
	}

	/**
	 * Checks to see if Actor is colliding with another Actor.
	 * 
	 * @param other
	 *            Actor to check collision against
	 */
	public void checkCollision(Actor other) {
		if (other.equals(this) || other instanceof Particle)
			return;

		Polygon otherPoly = other.basePoly;
		for (int i = 0; i < otherPoly.npoints; i++) {
			if (basePoly.contains(new Point(otherPoly.xpoints[i],
					otherPoly.ypoints[i]))) {
				setDeath(true);
				other.setDeath(true);
			}
		}
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

	public void setDeath(boolean d) {
		death = d;
	}

	/**
	 * Allows System to print name of object. Returns the name of the Actor
	 * 
	 * @return "Actor"
	 */
	public String toString() {
		return "Actor";
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
}
