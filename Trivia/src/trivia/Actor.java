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
	protected int width, height, speed, score, angle;
	private int pos;
	private AffineTransform transformer;
	private boolean debug;
	protected boolean death;
	protected Point corner, center;
	protected Polygon poly;

	/**
	 * Creates a new Actor.
	 * 
	 * @param p
	 *            Position in ArrayList
	 */
	public Actor(boolean debugMode, int p) {
		debug = debugMode;
		poly = new Polygon();
		angle = 0;
		corner = new Point(0,0);
		center = new Point(0,0);
		width = 0;
		height = 0;
		score = 0;
		speed = 3;
		transformer = new AffineTransform();
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
		for(int i=0; i<poly.npoints; i++)
		{
			int u = i+1;
			if (u >= poly.npoints)
			{
				u=0;
			}
			g.drawLine(corner.x+poly.xpoints[i], corner.y+poly.ypoints[i],
					corner.x+poly.xpoints[u], corner.y+poly.ypoints[u]);
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
	 * Returns the direction that the Actor is facing.
	 * 
	 * @return dir
	 */
	public int getAngle() {
		return angle;
	}

	/**
	 * Sets the direction that the Actor is facing.
	 * 
	 * @param d
	 *            Direction to set as.
	 */
	public void setAngle(int d) {
		log("**********************************");
		d %= 360;
		angle=d;
		poly = rotate(angle);
		
	}

	private Polygon rotate(double d) {
		d = Math.toRadians(d);
		Polygon newPoly = new Polygon();
		log("computing angle " + Math.toDegrees(d));
		for(int i=0; i < poly.npoints; i++)
		{
			log("-------");
			/*
			float localX = center.x-poly.xpoints[i];
			float localY = center.y-poly.ypoints[i];
			*/
			/*
			float localX = poly.xpoints[i];
			float localY = poly.ypoints[i];
			
			float a = (float) Math.atan2(localX,localY);
			log("Angle to point: " + Math.toDegrees(a));
			float r = (float) Math.sqrt(Math.pow(localX,2)+Math.pow(localY,2));
			log("Cos: " + Math.cos(a+d));
			log("Sin: " + Math.sin(a+d));
			log("r: " + r);
			*/
			/*
			int x = center.x + Math.round((float)(r * Math.cos(a+d)));
			int y = center.y + Math.round((float)(r * Math.sin(a+d)));
			*/
			/*
			int x = (int) (center.x + Math.cos(d) * (localX - center.x) - Math.sin(d) * (localY - center.y));
			int y = (int) (center.y + Math.sin(d) * (localX - center.x) + Math.cos(d) * (localY - center.y));
			
			
			
			*/
			/*
			double[] pt = {poly.xpoints[i], poly.ypoints[i]};
			AffineTransform.getRotateInstance(Math.toRadians(d), center.x, center.y)
			  .transform(pt, 0, pt, 0, 1); // specifying to use this double[] to hold coords
			int newX = (int) pt[0];
			int newY = (int) pt[1];
			
			*/
			Point currentPoint = new Point(poly.xpoints[i], poly.ypoints[i]);
			log("oldx: " + currentPoint.x);
			log("oldy: " + currentPoint.y);
			Point newPoint = rotatePoint(currentPoint,center, d);
			log("centerx: " + center.x);
			log("centery: " + center.y);
			log("newx: " + newPoint.y);
			log("newy: " + newPoint.x);
			
			newPoly.addPoint(newPoint.x,newPoint.y);
		}
		return newPoly;
	}
	
	public Point rotatePoint(Point pt, Point center, double angleRad)
	{
	    double cosAngle = Math.cos(angleRad );
	    double sinAngle = Math.sin(angleRad );
	    double dx = (pt.x-center.x);
	    double dy = (pt.y-center.y);

	    pt.x = center.x + (int) (dx*cosAngle-dy*sinAngle);
	    pt.y = center.y + (int) (dx*sinAngle+dy*cosAngle);
	    return pt;
	}

	/**
	 * Sets death to parameter.
	 * 
	 * @param d
	 *            boolean to set death to
	 */
	public void setDeath(boolean d) {
		death = d;
	}
	
	public Polygon rotate(Polygon p, double angle, double x, double y) {  
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
