package Actors;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import trivia.GameEngine;
import trivia.Helper;

import aiControls.RotateSearch;

/**
 * Fights with a heavy reliance on random movement and shots, will fire a shot
 * whenever one of its spokes lines up with an enemy
 * 
 * @author Cody Swendrowski, Dan Miller
 * 
 */

public class Square extends AI_Actor {

	static float MAX_ACCEL = 100F;
	static float fireRate = 1;// ms/shot
	public static int laserChargeTime = 10;// if small, better to be divisible
											// by 5
	long lastShotTime;
	double shotSpeed = 500;

	private ArrayList<Point2D.Float> corners;
	private ArrayList<Helper.myDub> cornerAngles;
	private ArrayList<Laser> cornerLasers;
	
	private Square() {
		super();
		Polygon poly = new Polygon();

		int width = 30, height = 30;
		poly.addPoint(width, 0);
		poly.addPoint(0, -height);
		poly.addPoint(-width, 0);
		poly.addPoint(0, height);
		setBasePoly(poly);
		corners = new ArrayList<Point2D.Float>();
		cornerAngles = new ArrayList<Helper.myDub>();
		cornerLasers = new ArrayList<Laser>();
		for (int i = 0; i < 4; i++) {
			corners.add(new Point2D.Float());
			cornerAngles.add(new Helper.myDub(0));
			cornerLasers.add(new Laser());
		}
		viewAngle = Math.PI / 16;
		viewDist = 150;
		clearAI_Control();
		aiCtrl.add(new RotateSearch(this));
		drawClr = Color.red;
	}

	public void move(int ms) {
		setCorners();
		super.move(ms);
	}

	private void setCorners() {
		for (int i = 0; i < drawPoly.npoints; i++) {
			Point2D.Float crn = corners.get(i);
			Helper.myDub val = cornerAngles.get(i);
			crn.x = drawPoly.xpoints[i];
			crn.y = drawPoly.ypoints[i];
			val.val = (Math.atan2(crn.y - center.y, crn.x - center.x) + 2 * Math.PI)
					% (2 * Math.PI);
		}
	}

	/**
	 * Fires a shot from the given spoke, 0-3
	 * 
	 * @param spoke
	 */

	public boolean fireShot(int spoke) {
		long ms = System.currentTimeMillis();
		if (lastShotTime + 50 <= ms) {
			lastShotTime = ms;
			double shotAngle = (spoke * Math.PI / 2 + angle) % (Math.PI * 2);
			Point2D.Float shotPos = new Point2D.Float(center.x
					+ (float) (radius * Math.cos(shotAngle)), center.y
					+ (float) (radius * Math.sin(shotAngle)));
			Point2D.Float shotVel = new Point2D.Float(
					(float) (shotSpeed * Math.cos(shotAngle)),
					(float) (shotSpeed * Math.sin(shotAngle)));
			// Polygon shotShape = new Polygon(new int[] {-4, 4, 4}, new int[]
			// {0, 3, -3}, 3);
			Particle.addBullet(shotPos, shotVel, Color.magenta);
			return true;
		}
		return false;
	}

	public boolean chargeLaser(int spoke) {
		int trueIndex = (drawPoly.npoints - spoke) % drawPoly.npoints;
		if (cornerLasers.get(trueIndex).remove) {
			cornerLasers.set(
					trueIndex,
					Particle.addLaser(corners.get(trueIndex),
							cornerAngles.get(trueIndex), Color.BLUE, laserChargeTime));
			return true;
		} else {
			return false;
		}
	}

	public double getShotSpeed() {
		return shotSpeed;
	}

	public ArrayList<Actor> getActorsInView() {
		ArrayList<Actor> list = new ArrayList<Actor>();

		for (Actor a : actors) {
			if (center.distance(a.getCenter()) < viewDist) {
				list.add(a);
			}
		}

		/*
		 * for (int i = 0; i < 360; i += 90) { list.addAll(getActorsInView(
		 * Math.toRadians((Math.toDegrees(angle) + i) % 360), viewAngle,
		 * viewDist)); }
		 */
		return list;
	}

	public static void addSquare(int x, int y) {
		Square c = new Square();
		c.setCenter(x, y);
		add(c);
	}

	@Override
	public float getMaxAccel() {
		return MAX_ACCEL;
	}

	public String toString() {
		return "Square " + super.toString();
	}
}
