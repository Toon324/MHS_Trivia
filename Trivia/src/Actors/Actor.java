package Actors;

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

import trivia.GameEngine;
import trivia.Helper;


import aiControls.*;

/**
 * Generic class for all objects in the game.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public abstract class Actor {

	protected Point2D.Float center;
	protected Color drawClr;
	protected float radius; //maximum distance from center to any point

	protected double angle;
	protected double rotateVel;// radians/s
	protected Point2D.Float vectVel, vectAccel;// pixels/s(/s)

	protected Polygon basePoly, drawPoly;

	protected boolean remove;
	
	protected static int ID_Count = 0;
	protected int ID;
	
	/**
	 * Creates a new Actor.
	 * 
	 * @param p
	 *            Position in ArrayList
	 */

	public Actor() {
		ID = ID_Count;
		ID_Count++;
		basePoly = new Polygon();
		vectVel = new Point2D.Float(0, 0);
		vectAccel = new Point2D.Float(0, 0);
		rotateVel = 0;
		angle = 0;
		center = new Point2D.Float(0, 0);
		drawClr = Color.cyan;
		remove = false;
	}
	
	public void setBasePoly(Polygon poly) {
		poly.translate((int) center.x, (int) center.y);
		basePoly = poly;
		drawPoly = new Polygon(poly.xpoints, poly.ypoints, poly.npoints);
		
		double tmpRadius = 0, newRad = 0;
		int[] xPoints = poly.xpoints, yPoints = poly.ypoints;
		for(int i = 0; i < poly.npoints;i++){
			tmpRadius = center.distance(xPoints[i], yPoints[i]);
			if(tmpRadius > newRad){
				newRad = tmpRadius;
			}
		}
		radius = (float) newRad;
	}
	
	
	public double getRad(){
		return radius;
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
	
	public void setAccel(Point2D.Float newAccel){
		vectAccel.setLocation(newAccel);
	}
	
	public Point2D.Float getAccel(){
		return vectAccel;
	}
	
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
		float sec = ms/1000F;
		setCenter(center.x + sec * vectVel.x, center.y + sec
				* vectVel.y);
		vectVel.y += vectAccel.y * sec;
		vectVel.x += vectAccel.x * sec;
		if (rotateVel != 0)// used to prevent unnecessary use of polygonal rotation
			rotate((ms / 1000F) * rotateVel);
	}

	/**
	 * Checks to see if Actor is colliding with another Actor.
	 * 
	 * @param other
	 *            Actor to check collision against
	 * 
	 * @return boolean
	 *            If the actor died when colliding
	 */
	public abstract boolean checkCollision(Actor other);

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
	 * @return direction
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
		return Helper.applyAffineTransform(myPoly,
				AffineTransform.getRotateInstance(d, center.x, center.y));
	}

	/**
	 * Allows System to print name of object. Returns the name of the Actor
	 * 
	 * @return "Actor"
	 */
	public String toString() {
		return "" + ID;
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
		
		// adds toAdd Actors to actors
		for (Actor a : toAdd) {
			actors.add(a);
		}
		toAdd.clear();

		// collects to be removed actors in an array and moves active ones,
		// checking for collisions
		for (Actor a : actors) {
			if (a.remove) {
				if(!(a instanceof Particle)) GameEngine.log("Removing " + a);
				toRemove.add(a);
			} else {
				a.move(ms);
				for (Actor act : actors) {//use of radius will massively increase speed of collisions, thus no need for threading
					if (!a.equals(act) && a.center.distance(act.center) <= a.radius + act.radius)
						if (a.checkCollision(act)) break;
				}
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
	
	public static void onEnvSizeChange(){
		Laser.resetBounds();
	}
	
	public static void setEngine(GameEngine e) {
		engine = e;
	}
}
