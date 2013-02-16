package trivia;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;

/**
 * Used to represent the enemy. Fires at player ships.
 * 
 * @author Cody Swendrowski, Dan Miller
 */

public class Square extends FightingActor {

	public Square(GameEngine e, int dest) {
		super(e);
		destination = dest;
		int width = 30, height = 30;
		vectVel = new Point2D.Float(-50, 0);
		Polygon base = new Polygon();
		base.addPoint(width, 0);
		base.addPoint(0, -height);
		base.addPoint(-width, 0);
		base.addPoint(0, height);
		setBasePoly(base);
		drawClr = Color.orange;
		shotVel.x = -40; // Make shots move left
	}

	@Override
	public void move(int ms) {
		if (center.x + vectVel.x * (ms / 1000f) >= destination) // If square is
																// to the right,
																// keep moving
			super.move(ms);
	}

	@Override
	public String toString() {
		return "Square";
	}
}
