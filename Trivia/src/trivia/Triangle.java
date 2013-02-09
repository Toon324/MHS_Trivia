package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import aiControls.*;

public class Triangle extends Actor {

	Point currentDest;
	static float MAX_ACCEL = 100F;// Pixels/s/s

	public Triangle() {
		super();
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

	public void move(int ms) {
		/*
		viewArea = new ArrayList<Polygon>();
		ArrayList<Actor> list = getActorsInView(angle, viewAngle, viewDist);
		if (!list.isEmpty()) {
			for (Actor a : list) {
				if (!a.equals(this) && a instanceof Square) {
					// process actors
				}
			}
		}
		vectVel.x += (ms / 1000F)
				* getAccelToReach(currentDest.x - center.x, vectVel.x,
						MAX_ACCEL);
		vectVel.y += (ms / 1000F)
				* getAccelToReach(currentDest.y - center.y, vectVel.y,
						MAX_ACCEL);*/
		super.move(ms);
	}

	public void draw(Graphics g) {
		super.draw(g);
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
