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
import trivia.Helper;

public class Laser extends Particle {

	int chargeTime, fireTime;
	double DPS;
	Helper.myDub directionLink;
	Point endPoint;

	long stateStartTime;

	public static double chargeToFiring = 4 / 5;

	int chargeBallRad = 5;
	boolean charging;

	static ArrayList<Line2D.Float> windowBounds;

	public Laser() {
		super();
		remove = true;
	}

	protected Laser(Point2D.Float origin, Helper.myDub angle, Color c, Actor create, double damage) {
		super(origin, c, create);
		DPS = damage;
		center = origin;
		directionLink = angle;
		radius = -50;// to prevent any collisions
		stateStartTime = System.currentTimeMillis();
		charging = true;
	}

	protected Laser(Point2D.Float origin, Helper.myDub angle, Color c, Actor create) {
		this(origin, angle, c, create, 0);
	}

	public void setLife(int life) {
		chargeTime = (int) (life * chargeToFiring);
		fireTime = (int) (life * (1 - chargeToFiring));
	}

	public void move(int ms) {
		if (charging) {
			if (stateStartTime + chargeTime <= System.currentTimeMillis()) {
				// when charging is done
				stateStartTime = System.currentTimeMillis();
				charging = false;
				
				AI_Actor target = findCollidePoint();
				if (target != null) {
					target.damage(DPS * (ms/(1000F)));
				}
			}
		} else {
			if (stateStartTime + fireTime < System.currentTimeMillis()) {
				remove = true;
			}
			AI_Actor target = findCollidePoint();
			if (target != null) {
				target.damage(DPS * (ms/(1000F)));
			}
		}
	}

	public void draw(Graphics g) {
		g.setColor(drawClr);
		if (charging) {
			g.drawOval((int) center.x - chargeBallRad / 2, (int) center.y
					- chargeBallRad / 2, 5, 5);
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

	private AI_Actor findCollidePoint() {
		ArrayList<AI_Actor> toCheck = new ArrayList<AI_Actor>();
		Line2D.Float laserRay = new Line2D.Float(center.x, center.y,
				(float) (center.x + 10000 * Math.cos(directionLink.val)),
				(float) (center.y + 10000 * Math.sin(directionLink.val)));
		for (Actor a : actors) {
			if (!(a instanceof AI_Actor) || a == creator)
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
				possIntersect.add(Helper.findIntersect(laserRay, ln));
				actMapping.add(null);
			}
		}

		Polygon poly;
		Line2D ln;
		// add any actors intersections
		for (AI_Actor act : toCheck) {
			poly = act.getDrawnPoly();
			for (int i = 0; i < poly.npoints - 1; i++) {
				ln = new Line2D.Float(poly.xpoints[i], poly.ypoints[i],
						poly.xpoints[i + 1], poly.ypoints[i + 1]);
				if (laserRay.intersectsLine(ln)) {
					possIntersect.add(Helper.findIntersect(laserRay, ln));
					actMapping.add(act);
				}
			}
			ln = new Line2D.Float(poly.xpoints[poly.npoints - 1],
					poly.ypoints[poly.npoints - 1], poly.xpoints[0],
					poly.ypoints[0]);
			if (laserRay.intersectsLine(ln)) {
				possIntersect.add(Helper.findIntersect(laserRay, ln));
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

		endPoint = new Point((int) possIntersect.get(closest).x,
				(int) possIntersect.get(closest).y);
		// endPoint = new Point((int) laserRay.x2, (int) laserRay.y2);

		AI_Actor close = actMapping.get(closest);
		return close;
	}

	@Override
	public boolean checkCollision(Actor other) {
		return false;
	}

}
