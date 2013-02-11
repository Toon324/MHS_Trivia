package trivia;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

/**
 * Fights with a heavy reliance on random movement and shots, will fire a shot
 * whenever one of its spokes lines up with an enemy
 * 
 * @author Cody Swendrowski, Dan Miller
 * 
 */

public class Square extends Actor {

	Point currentDest;
	static float MAX_ACCEL = 1000F;
	private int destination;

	public Square(boolean debugMode,GameEngine e, int dest) {
		super(debugMode,e);
		Polygon poly = new Polygon();
		destination = dest;
		int width = 30, height = 30;
		vectVel = new Point2D.Float(-50,0);
		poly.addPoint(width, 0);
		poly.addPoint(0, -height);
		poly.addPoint(-width, 0);
		poly.addPoint(0, height);
		setBasePoly(poly);
		drawClr = Color.orange;
		death = false;
	}

	public void move(int ms) {
		if (center.x + vectVel.x*(ms/1000f) >= destination)
			super.move(ms);
	}
	
	/**
	 * Fires a shot from the given spoke, 0-3
	 * @param spoke
	 */
	
	public void fireShot(int spoke){
		double speed = 300;
		double shotAngle = 90;
		Point2D.Float shotVel = new Point2D.Float((float) (speed * Math.cos(shotAngle)), (float) (speed * Math.sin(shotAngle)));
		Polygon shotShape = new Polygon(new int[] {-4, 4, 4}, new int[] {0, 3, -3}, 3);
		//System.out.printf("Shot vel: (%.4f, %.4f)\n", shotVel.x, shotVel.y);
		actors.addParticle((Point2D.Float) (center.clone()), shotVel, Color.magenta);
	}

	@Override
	public float getMaxAccel() {
		return MAX_ACCEL;
	}
	
	public void checkCollision(Actor other) {
		if (other.equals(this) || other instanceof Square)
			return;
		else
			super.checkCollision(other);
	}
	
	public String toString()
	{
		return "Square";
	}
}
