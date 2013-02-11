package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

public class Triangle extends Actor {

	Point currentDest;
	static float MAX_ACCEL = 100F;// Pixels/s/s
	private int destination;

	public Triangle(boolean debugMode,GameEngine e, int dest) {
		super(debugMode,e);
		Polygon poly = new Polygon();
		destination = dest;
		vectVel = new Point2D.Float(2,0);
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
	
	public void move(int ms)
	{
		if (center.x + vectVel.x*(ms/1000) <= destination)
			center.x += vectVel.x*(ms/1000);
	}

	@Override
	public float getMaxAccel() {
		return MAX_ACCEL;
	}

}
