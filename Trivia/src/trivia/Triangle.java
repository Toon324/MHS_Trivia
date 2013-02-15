package trivia;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;

/**
 * Fights for the Player, and is affected by Player performance via a change in
 * evade chance.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class Triangle extends FightingActor {

	/**
	 * Creates a new Triangle.
	 * 
	 * @param e
	 *            GameEngine to utilize
	 * @param dest
	 *            Destination to move to
	 */
	public Triangle(GameEngine e, int dest) {
		super(e);
		destination = dest;
		vectVel = new Point2D.Float(50, 0); // Move right
		int width = 30, height = 30;
		basePoly.addPoint(-width / 2, -height / 2);
		basePoly.addPoint(width / 2, 0);
		basePoly.addPoint(-width / 2, height / 2);
		shotVel.x = 40; // Shoot right
		drawClr = Color.cyan;
	}

	@Override
	public void move(int ms) {
		if (center.x + vectVel.x * (ms / 1000f) <= destination)
			super.move(ms);
	}

	@Override
	public String toString() {
		return "Triangle";
	}

}
