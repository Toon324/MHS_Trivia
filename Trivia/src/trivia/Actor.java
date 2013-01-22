package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

/**
 * Generic class for all objects in the game.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class Actor {
	protected int width, height, speed, score;
	protected double angle;
	private int pos;
	private boolean debug;
	protected boolean death;
	protected Point center;
	protected Polygon basePoly, drawPoly;

	/**
	 * Creates a new Actor.
	 * 
	 * @param p
	 *            Position in ArrayList
	 */
	public Actor(boolean debugMode, int p) {
		debug = debugMode;
		basePoly = new Polygon();
		angle = 0;
		center = new Point(0,0);
		width = 0;
		height = 0;
		score = 0;
		speed = 3;
		death = false;
		pos = p;
	}
	
	public void setBasePoly(Polygon poly){
		basePoly = poly;
		drawPoly = new Polygon(poly.xpoints, poly.ypoints, poly.npoints);
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
		g.setColor(Color.cyan);
		if(drawPoly == null) drawPoly = basePoly;
		
		
		int x = 0, y = 0, firstX = 0, firstY = 0;
		int res;
		float array[] = new float[6];
		PathIterator iter = drawPoly.getPathIterator(new AffineTransform());
		while(!iter.isDone()){
			res = iter.currentSegment(array);
			switch(res){
			case PathIterator.SEG_CLOSE:
				array[0] = firstX; array[1] = firstY;
			case PathIterator.SEG_LINETO:
				g.drawLine(x, y, (int)array[0], (int)array[1]);
				g.drawLine(x, y, center.x, center.y);
				x = (int) array[0]; y = (int) array[1];
				break;
			case PathIterator.SEG_MOVETO:
				x = (int) array[0]; y = (int) array[1];
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
	public void move(int w, int h) {
	}

	/**
	 * Returns known position in Actors.
	 * 
	 * @return position in Actors
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * Sets known position in Actors.
	 * 
	 * @param p
	 *            int position to set
	 */
	public void setPos(int p) {
		pos = p;
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
		// only checks enemy collisions
		// to be overridden
	}

	/**
	 * Returns speed value of Actor.
	 * 
	 * @return speed
	 */
	public int getSpeed() {
		return speed;
	}

	
	public void setSize(int x, int y) {
		width = x;
		height = y;
		center.x += width/2;
		center.y -= height/2;
	}
	
	public Point getCenter()
	{
		return center;
	}
	
	public void setCenter(int x, int y)
	{
		if(drawPoly != null) drawPoly.translate(-center.x, -center.y);
		basePoly.translate(-center.x, -center.y);
		center.x = x;
		center.y = y;
		if(drawPoly != null) drawPoly.translate(center.x, center.y);
		basePoly.translate(center.x, center.y);
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
		log("**********************************");
		d %= (2 * Math.PI);
		log("Angle: " + Math.toDegrees(d));
		drawPoly = rotate(basePoly, d);
		angle=d;
	}
	
	public void rotate(double d){
		d %= (2 * Math.PI);
		angle += d;
		angle %= (2 * Math.PI);
		drawPoly = rotate(basePoly, angle);
	}

	private Polygon rotate(Polygon myPoly, double d) {
		Polygon newPoly = new Polygon();
		log("computing angle " + Math.toDegrees(d));
		return applyAffineTransform(myPoly, AffineTransform.getRotateInstance(d, center.x, center.y));
	}
	
	private Polygon applyAffineTransform(Polygon poly, AffineTransform trans){
		Point[] points = new Point[poly.npoints];
		for(int i = 0; i < poly.npoints; i++){
			points[i] = new Point(poly.xpoints[i], poly.ypoints[i]);
		}
		trans.transform(points, 0, points, 0, points.length);
		Polygon newPoly = new Polygon();
		for(int i = 0; i < points.length; i++){
			newPoly.addPoint(points[i].x, points[i].y);
		}
		return newPoly;
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
	
	private void log(String s)
	{
		if(debug)
		{
			System.out.println(s);
		}
	}

}
