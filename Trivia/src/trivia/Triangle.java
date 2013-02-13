package trivia;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;

public class Triangle extends FightingActor {

	Point currentDest;

	public Triangle(GameEngine e, int dest) {
		super(e);
		Polygon poly = new Polygon();
		destination = dest;
		vectVel = new Point2D.Float(50,0);
		int width = 30, height = 30;
		poly.addPoint(-width / 2, -height / 2);
		poly.addPoint(width / 2, 0);
		poly.addPoint(-width / 2, height / 2);
		setBasePoly(poly);
		shotVel.x = 40;
		drawClr = Color.cyan;
	}

	public void setDestination(Point pnt) {
		currentDest = pnt;
	}
	
	public void move(int ms) {
		if (center.x + vectVel.x*(ms/1000f) <= destination)
			super.move(ms);
	}

	public void checkCollision(Actor other) {
		if (other instanceof Triangle) {
			return;
		}
		super.checkCollision(other);
	}
	
	public String toString() {
		return "Triangle";
	}

}
