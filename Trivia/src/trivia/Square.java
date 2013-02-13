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

public class Square extends FightingActor {

	Point currentDest;
	static float MAX_ACCEL = 1000F;

	public Square(GameEngine e, int dest) {
		super(e);
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
		shotVel.x = -40;
	}
	
	public void checkCollision(Actor other) {
		if (other instanceof Square)
			return;
		super.checkCollision(other);
	}
	
	public void move(int ms) {
		//engine.log("Square at " + center.x + ", " + center.y);
		if (center.x + vectVel.x*(ms/1000f) >= destination)
			super.move(ms);
	}
	
	public String toString()
	{
		return "Square";
	}
}
