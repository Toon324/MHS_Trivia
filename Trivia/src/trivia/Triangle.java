package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import aiControls.*;

public class Triangle extends AI_Actor {

	Point currentDest;
	static float MAX_ACCEL = 100F;// Pixels/s/s

	private Triangle() {
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
		super.move(ms);
	}

	public void draw(Graphics g) {
		super.draw(g);
	}

	public static void addTriangle(int x, int y) {
		Triangle c = new Triangle();
		c.setCenter(x, y);
		add(c);
	}
	
	@Override
	public float getMaxAccel() {
		return MAX_ACCEL;
	}
	
	//used for debug
	public static Triangle getEmptyInstance(){
		Triangle t = new Triangle();
		t.setCenter(engine.windowWidth / 2, engine.windowHeight / 2);
		return t;
	}

}
