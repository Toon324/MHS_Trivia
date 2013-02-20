package Actors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

import trivia.GameEngine;

public class Laser extends Particle {

	int chargeTime, fireTime;
	boolean charging;
	double damage;
	Point endPoint;
	
	
	
	long stateStartTime;

	static ArrayList<Line2D.Float> windowBounds;

	public Laser(Point2D.Float origin, double direction, Color c) {
		super(origin, c);
		center = origin;
		angle = direction;
		radius = -50;// to prevent any collisions
		stateStartTime = System.currentTimeMillis();
		charging = true;
	}

	public void setLife(int life) {
		chargeTime = life * 4 / 5;
		fireTime = life / 5;
	}

	public void move(int ms) {
		if (charging) {
			if (stateStartTime + chargeTime < System.currentTimeMillis()) {
				stateStartTime = System.currentTimeMillis();
				charging = false;
				findCollidePoint();
				center = new Point2D.Float(center.x, center.y);
			}
		} else {
			if (stateStartTime + fireTime < System.currentTimeMillis()) {
				remove = true;
				return;
			}
			findCollidePoint();
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(drawClr);
		if (charging) {
			g.drawOval((int) center.x, (int) center.y, 5, 5);
		} else {
			g.drawLine((int) center.x, (int) center.y, endPoint.x, endPoint.y);
		}
	}

	public static void resetBounds() {
		windowBounds = new ArrayList<Line2D.Float>();
		windowBounds.add(new Line2D.Float(0, 0, GameEngine.getEnv().x, 0));
		windowBounds.add(new Line2D.Float(GameEngine.getEnv().x, 0, GameEngine
				.getEnv().x, GameEngine.getEnv().y));
		windowBounds.add(new Line2D.Float(GameEngine.getEnv().x, GameEngine
				.getEnv().y, 0, GameEngine.getEnv().y));
		windowBounds.add(new Line2D.Float(0, GameEngine.getEnv().y, 0, 0));
	}

	private void findCollidePoint() {
		ArrayList<AI_Actor> toCheck = new ArrayList<AI_Actor>();
		Line2D.Float laserRay = new Line2D.Float(center.x, center.y,
				(float) (center.x + 10000 * Math.cos(angle)),
				(float) (center.y + 10000 * Math.sin(angle)));
		for (Actor a : actors) {
			if (!(a instanceof AI_Actor))
				continue;
			Polygon poly = a.getDrawnPoly();
			if (poly != null && laserRay.intersects(poly.getBounds2D())) {
				toCheck.add((AI_Actor) a);
			}
		}

		ArrayList<Point2D.Float> possIntersect = new ArrayList<Point2D.Float>();
		// used to keep track of which actor is being actually collided with
		ArrayList<AI_Actor> actMapping = new ArrayList<AI_Actor>();

		// add the window edges to intersects
		for (Line2D ln : windowBounds) {
			if (laserRay.intersectsLine(ln)) {
				possIntersect.add(findIntersect(laserRay, ln));
				actMapping.add(null);
			}
		}

		Polygon poly;
		Line2D ln;
		// add any actors intersections
		for (AI_Actor act : toCheck) {
			poly = act.getDrawnPoly();
			GameEngine.log("Searching poly with " + poly.npoints + "points.");
			for (int i = 0; i < poly.npoints - 1; i++) {
				ln = new Line2D.Float(poly.xpoints[i], poly.ypoints[i],
						poly.xpoints[i + 1], poly.ypoints[i + 1]);
				if (laserRay.intersectsLine(ln)) {
					possIntersect.add(findIntersect(laserRay, ln));
					actMapping.add(act);
				}
			}
			ln = new Line2D.Float(poly.xpoints[poly.npoints - 1],
					poly.ypoints[poly.npoints - 1], poly.xpoints[0],
					poly.ypoints[0]);
			if (laserRay.intersectsLine(ln)) {
				possIntersect.add(findIntersect(laserRay, ln));
				actMapping.add(act);
			}
		}

		int closest = 0;
		double minDist = Integer.MAX_VALUE;
		for (int i = 0; i < possIntersect.size(); i++) {
			Point2D.Float pnt = possIntersect.get(i);
			double dst = pnt.distance(center);
			if (dst < minDist) {
				closest = i;
				minDist = dst;
			}
		}

		AI_Actor close = actMapping.get(closest);
		if (close != null)
			close.damage(damage);

		endPoint = new Point((int) possIntersect.get(closest).x,
				(int) possIntersect.get(closest).y);
		// endPoint = new Point((int) laserRay.x2, (int) laserRay.y2);
	}

	private Point2D.Float findIntersect(Line2D l1, Line2D l2) {
		double denom1 = l1.getX1() - l1.getX2();
		double denom2 = l2.getX1() - l2.getX2();
		double m1 = (l1.getY1() - l1.getY2()) / (denom1);
		double m2 = (l2.getY1() - l2.getY2()) / (denom2);
		double c1 = -l1.getX1() * m1 + l1.getY1();
		double c2 = -l2.getX1() * m2 + l2.getY1();
		double x, y;
		if (denom1 == 0 && denom2 == 0) {
			return new Point2D.Float(200, 200);
		} else if (denom1 == 0) {
			x = l1.getX1();
			y = m2 * x + c2;
		} else if (denom2 == 0) {
			x = l2.getX1();
			y = m1 * x + c1;
		} else {
			x = (c2 - c1) / (m1 - m2);
			y = m1 * x + c1;
		}
		return new Point2D.Float((float) x, (float) y);
	}

	@Override
	public boolean checkCollision(Actor other) {
		return false;
	}

}
