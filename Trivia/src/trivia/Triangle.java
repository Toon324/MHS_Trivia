package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

public class Triangle extends Actor {

	Point currentDest;
	static float MAX_ACCEL = 100F;// Pixels/s/s

	private double viewAngle;// angle that the view will deviate on both sides
	private int viewDist;

	public Triangle(boolean debugMode,GameEngine e) {
		super(debugMode,e);
		Polygon poly = new Polygon();

		int width = 30, height = 30;
		poly.addPoint(-width / 2, -height / 2);
		poly.addPoint(width / 2, 0);
		poly.addPoint(-width / 2, height / 2);
		setBasePoly(poly);
		viewAngle = Math.PI / 8;
		viewDist = 200;
		drawClr = Color.cyan;
	}

	public void setDestination(Point pnt) {
		currentDest = pnt;
	}

	public void checkCollision(Actor other) {
		if (other instanceof Triangle) {
			return;
		}
		super.checkCollision(other);
	}

	@Override
	public float getMaxAccel() {
		return MAX_ACCEL;
	}

}
