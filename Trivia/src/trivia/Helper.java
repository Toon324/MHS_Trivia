package trivia;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import Actors.Square;

public final class Helper {

	public static class myDub {
		public double val;

		public myDub(double val) {
			this.val = val;
		}
	}

	public static Point2D.Float findIntersect(Line2D l1, Line2D l2) {
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

	public static Polygon applyAffineTransform(Polygon poly,
			AffineTransform trans) {
		Point[] points = new Point[poly.npoints];
		for (int i = 0; i < poly.npoints; i++) {
			points[i] = new Point(poly.xpoints[i], poly.ypoints[i]);
		}
		trans.transform(points, 0, points, 0, points.length);
		Polygon newPoly = new Polygon();
		for (int i = 0; i < points.length; i++) {
			newPoly.addPoint(points[i].x, points[i].y);
		}
		return newPoly;
	}

	public static float getAccelToReach(float xDist, float currentVel, float MAX) {
		/*
		 * Time for velocity to reach 0 if it started to slow down:
		 * (currentVel/MAX_ACCEL)
		 * 
		 * Distance to the next solution for y = 0 that will be traveled in t
		 * time if acceleration is reversed: currentVel * t - (MAX_ACCEL/2) *
		 * t^2
		 */
		byte velSign = (byte) ((((Float.floatToIntBits(currentVel) >> 31) & (1)) * 2 - 1) * -1);
		double t = currentVel / MAX;
		float possibleDist = (float) (currentVel * t - (MAX / 2)
				* Math.pow(t, 2));

		if (possibleDist >= xDist * velSign)
			velSign *= -1;
		if (Math.abs(xDist) <= 2 && currentVel <= MAX)
			return -currentVel;
		return MAX * velSign;
	}

	/**
	 * Takes a given ray denoted in polar coordinates, and determines if it
	 * intersects the given polygon
	 * 
	 * @param point
	 *            The origin of the ray
	 * @param angle
	 *            The angle of the ray
	 * @param target
	 *            The target polygon
	 * @return If the ray intersects the polygon
	 */
	public static boolean doesPolarIntersectPoly(Point2D.Float point,
			double angle, Polygon target) {

		boolean right = false, left = false; // used to determine if the
												// enemy is in the shot area

		double pntAngle;

		for (int j = 0; j < target.npoints; j++) {
			pntAngle = (Math.atan2(target.ypoints[j] - point.y,
					target.xpoints[j] - point.x) + 2 * Math.PI) % (Math.PI * 2);
			double difference = (pntAngle - angle) % (Math.PI * 2);
			if (Math.abs(difference) < (Math.PI / 2))
				if ((pntAngle - angle) % (Math.PI * 2) > 0) {
					left = true;
				} else {
					right = true;
				}
			if (right && left) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * CURRENTLY NON-FUNCTIONAL
	 * 
	 * @param p1
	 *            origin of the shot
	 * @param p2
	 *            shot target
	 * @param vel2
	 *            shot target velocity
	 * @param ac2
	 *            shot target acceleration
	 * @param speed
	 *            speed of the shot
	 * @return Angle which would produce a shot from p1 which would cross
	 *         through p2
	 */
	public static final double getLeadShotAngle(Point2D.Float p1,
			Point2D.Float p2, Point2D.Float vel, Point2D.Float ac, double speed) {

		GameEngine.log("P1:  " + p1);
		GameEngine.log("P2:  " + p2);
		GameEngine.log("vel: " + vel);
		GameEngine.log("acc: " + ac);

		double dX = p1.x - p2.x, dY = p1.y - p2.y;
		double a = (ac.y * ac.y + ac.x * ac.x) / 4;
		double b = (vel.y * ac.y + vel.x * ac.x);
		double c = (vel.x * vel.x + vel.y * vel.y - (dX * ac.x + dY * ac.y + speed
				* speed));
		double d = -2 * (dX * vel.x + dY * vel.y);
		double e = dX * dX + dY * dY;

		double x = 2 * c * c * c - 9 * b * c * d + 27 * a * d * d + 27 * b * b
				* e - 72 * a * c * e;
		double y = c * c - 3 * b * d + 12 * a * e;
		double z = Math.pow(x + Math.sqrt(x * x - 4 * y * y * y), 1 / 3);
		double Ba = b / a;
		double Ca = c / a;
		double C1 = /* 2^1/3 */1.2599210498948731647672106072782 * y
				/ (3 * a * z);
		double C2 = z / (3 * 1.2599210498948731647672106072782 * a);

		double M = Math.sqrt(Ba * Ba / 4 + C1 + C2 - 2 * Ca / 3);

		double N = Ba * Ba / 2 - C1 - C2 - 4 * Ca / 3;
		double O = (-Ba * Ba * Ba + 4 * Ba * Ca - 8 * d / a) / (4 * M);
		GameEngine.log("N:   " + N);
		GameEngine.log("O:   " + O);

		double P, Q, time;
		if (N - O >= 0) {
			P = Math.sqrt(N - O);
			GameEngine.log("Psq: " + (N - O));
			Q = -Ba / 4 - M / 2;
		} else {
			P = Math.sqrt(N + O);
			GameEngine.log("Psq: " + (N + O));
			Q = -Ba / 4 + M / 2;
		}

		if (Q - P >= 0) {
			time = Q - P;
		} else {
			time = Q + P;
		}

		double theta = (Math.atan2(dY - vel.y * time - ac.y * time * time / 2,
				dX - vel.x * time - ac.y * time * time / 2) + Math.PI * 2)
				% (Math.PI * 2);

		return theta;
	}

	public static double getAngleAtTime(Point2D.Float p1, Point2D.Float p2,
			Point2D.Float vel, Point2D.Float ac, double time) {

		double theta = (Math.atan2(p1.y
				- (p2.y + vel.y * time + ac.y * time * time / 2), p1.y
				- (p2.y + vel.x * time + ac.y * time * time / 2)) + Math.PI * 2)
				% (Math.PI * 2);

		return theta;
	}
}
