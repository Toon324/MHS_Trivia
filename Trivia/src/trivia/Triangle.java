package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

public class Triangle extends Actor {

	Point currentDest;
	static float MAX_ACCEL = 500F;// Pixels/s/s

	private double viewAngle;// angle that the view will deviate on both sides
	private int viewDist;

	public Triangle(boolean debugMode) {
		super(debugMode);
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
		 * Point2D.Float accel = new Point2D.Float((ms/1000F) *
		 * getAccelToReach(currentDest.x - center.x, vectVel.x), (ms / 1000F) *
		 * getAccelToReach(currentDest.y - center.y, vectVel.y));
		 * System.out.printf(
		 * "Moving for %dms to (%4d, %4d) from (%6.2f, %6.2f). Acceleration: (%6.3f, %6.3f) (%6.2f) Velocity: (%6.1f, %6.1f)\n"
		 * , ms, currentDest.x, currentDest.y, center.x, center.y, accel.x,
		 * accel.y, MAX_ACCEL, vectVel.x, vectVel.y); vectVel.x += accel.x;
		 * vectVel.y += accel.y;
		 */

		/*
		 * Particle.addParticle( new Point2D.Float(center.x, center.y), new
		 * Point2D.Float((float) Math.random() * 10 - 5, (float) Math.random() *
		 * 10 - 5), new Color((float) Math.random(), (float) Math.random(),
		 * (float) Math.random()), 3F, 2F);
		 */

		viewArea = new ArrayList<Polygon>();
		ArrayList<Actor> list = getActorsInView(angle, viewAngle, viewDist);
		if (!list.isEmpty()) {
			for (Actor a : list) {
				if (!a.equals(this) && a instanceof Square) {
					/*
					 * double distance = center.distance(a.center); Float newVel
					 * = new Float( (float) ((a.center.x - center.x) * 5 /
					 * distance), (float) ((a.center.y - center.y) * 5 /
					 * distance)); Particle.addParticle((Float) center.clone(),
					 * newVel, new Polygon(new int[] {-2, 2, 2, -2}, new int[]
					 * {-2, -2, 2, 2}, 4), Color.green, 2F, 1F);
					 */
					// process actors
				}
			}
		}
		vectVel.x += (ms / 1000F)
				* getAccelToReach(currentDest.x - center.x, vectVel.x,
						MAX_ACCEL);
		vectVel.y += (ms / 1000F)
				* getAccelToReach(currentDest.y - center.y, vectVel.y,
						MAX_ACCEL);
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

}
