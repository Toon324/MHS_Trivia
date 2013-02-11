package trivia;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;

public class Triangle extends Actor {

	Point currentDest;
	static float MAX_ACCEL = 100F;// Pixels/s/s
	private int destination;

	public Triangle(boolean debugMode,GameEngine e, int dest) {
		super(debugMode,e);
		Polygon poly = new Polygon();
		destination = dest;
		vectVel = new Point2D.Float(50,0);
		int width = 30, height = 30;
		poly.addPoint(-width / 2, -height / 2);
		poly.addPoint(width / 2, 0);
		poly.addPoint(-width / 2, height / 2);
		setBasePoly(poly);
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
		//GameEngine.log(this.toString() + " at " + center.x + ", " + center.y + " S: " + (ms/1000f));
		if (center.x + vectVel.x*(ms/1000f) <= destination) 
			super.move(ms);
	}

	@Override
	public float getMaxAccel() {
		return MAX_ACCEL;
	}
	
	public String toString() {
		return "Triangle";
	}

}
