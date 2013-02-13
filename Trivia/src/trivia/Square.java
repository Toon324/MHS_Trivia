package trivia;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

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

	private Square() {
		super();
		Polygon poly = new Polygon();

		int width = 30, height = 30;
		poly.addPoint(width, 0);
		poly.addPoint(0, -height);
		poly.addPoint(-width, 0);
		poly.addPoint(0, height);
		setBasePoly(poly);
		viewAngle = Math.PI / 16;
		viewDist = 150;
		clearAI_Control();
		aiCtrl.add(new RotateSearch(this));
		drawClr = Color.red;
	}

	public void move(int ms) {
		super.move(ms);
	}
	
	/**
	 * Fires a shot from the given spoke, 0-3
	 * @param spoke
	 */
	
	public void fireShot(int spoke){
		double speed = 300;
		double shotAngle = (spoke*Math.PI/2 + angle) % (Math.PI*2);
		Point2D.Float shotVel = new Point2D.Float((float) (speed * Math.cos(shotAngle)), (float) (speed * Math.sin(shotAngle)));
		//Polygon shotShape = new Polygon(new int[] {-4, 4, 4}, new int[] {0, 3, -3}, 3);
		Particle.addParticle((Point2D.Float) (center.clone()), shotVel, Color.magenta);
	}

	public ArrayList<Actor> getActorsInView(){
		ArrayList<Actor> list = new ArrayList<Actor>();
		for(int i = 0; i < 360; i += 90){
			list.addAll(getActorsInView(Math.toRadians((Math.toDegrees(angle) + i) % 360), viewAngle, viewDist));
		}
		return list;
	}
	
	public boolean equals(Object other){
		if (other == this) return true;
		if (other instanceof Square) return true;
		return false;
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
	
	public String toString(){
		return "Square " + super.toString();
	}
}
