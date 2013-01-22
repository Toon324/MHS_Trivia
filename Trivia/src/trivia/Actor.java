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
	protected Point corner, center;
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
		corner = new Point(0,0);
		center = new Point(0,0);
		width = 0;
		height = 0;
		score = 0;
		speed = 3;
		death = false;
		pos = p;
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
				g.drawLine(x + corner.x, y + corner.y, (int)array[0] + corner.x, (int)array[1] + corner.y);
				g.drawLine(x + corner.x, y + corner.y, center.x + corner.x, center.y + corner.y);
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
		center.x = width/2;
		center.y = -height/2;
	}
	
	public Point getCorner()
	{
		return corner;
	}
	
	public void setCorner(int x, int y)
	{
		corner.x = x;
		corner.y = y;
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
		Point[] pnts = new Point[myPoly.npoints];
		for(int i=0; i < myPoly.npoints; i++)
		{
			log("-------");
			pnts[i] = new Point(myPoly.xpoints[i], myPoly.ypoints[i]);
			log("oldx: " + pnts[i].x);
			log("oldy: " + pnts[i].y);
		}
		AffineTransform.getRotateInstance(d, center.x, center.y).transform(pnts, 0, pnts, 0, pnts.length);
		for(int i = 0; i < pnts.length; i++){
			newPoly.addPoint(pnts[i].x, pnts[i].y);
			log("centerx: " + center.x);
			log("centery: " + center.y);
			log("newx: " + pnts[i].y);
			log("newy: " + pnts[i].x);
		}
		return newPoly;
	}
	
	/*public Point rotatePoint(Point pt, Point center, double angleRad)
	{
	    double cosAngle = Math.cos(angleRad );
	    double sinAngle = Math.sin(angleRad );
	    double dx = (pt.x-center.x);
	    double dy = (pt.y-center.y);

	    pt.x = center.x + (int) (dx*cosAngle-dy*sinAngle);
	    pt.y = center.y + (int) (dx*sinAngle+dy*cosAngle);
	    return pt;
	}*/

	/**
	 * Sets death to parameter.
	 * 
	 * @param d
	 *            boolean to set death to
	 */
	public void setDeath(boolean d) {
		death = d;
	}
	
	/*public Polygon rotate(Polygon p, double angle, double x, double y) {  
	    if (angle == 0) return p;
	    AffineTransform rotation = AffineTransform.getRotateInstance(angle, x, y);  
	    PathIterator pit = p.getPathIterator(rotation);  
	    float[] a = new float[6]; // as per PathIterator.currentSegment() spec  
	    Polygon rp = new Polygon();  
	    int ty;  
	    do {  
	      ty = pit.currentSegment(a);  
	      if (ty != PathIterator.SEG_CLOSE)
	      {
	    	  log("BeforeX: " + a[0] + " BeforeY: " + a[1]);
	    	  rp.addPoint(Math.round(a[0]), Math.round(a[1]));  
	    	  log("AfterX: " + Math.round(a[0]) + " AfterY: " + Math.round(a[1]));
	      }
	      pit.next();
	    } while (ty!=PathIterator.SEG_CLOSE && !pit.isDone());  
	    return rp;  
	  }*/

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
