package Actors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.Random;

import trivia.GameEngine;
import trivia.Helper;

/**
 * A single point Actor that does not interact with others. Used for particle
 * effects.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public abstract class Particle extends Actor {

	
	public Particle(){
		super();
	}
	
	protected Particle(Point2D.Float center, Color c) {
		super();
		setCenter(center.x, center.y);
		drawClr = c;
	}
	//move in Actor is sufficient
	
	public void setVel(float x, float y){
		vectVel = new Point2D.Float(x, y);
	}
	
	@Override
	public void setCenter(float x, float y) {
		if (x < 0 || y < 0 || x > engine.windowWidth || y > engine.windowHeight) {
			remove = true;
			return;
		}
		super.setCenter(x, y);
	}

	
	public String toString(){
		return "Particle " + super.toString();
	}

	public static Bullet addBullet(Point2D.Float center, Point2D.Float vectorSpeed,
			Color c) {
		Bullet p = new Bullet(center, (Float) vectorSpeed.clone(), c);
		add(p);
		return p;
	}
	
	public static Laser addLaser(Point2D.Float center, Helper.myDub angle, Color c){
		return addLaser(center, angle, c, 1000);
	}
	
	public static Laser addLaser(Point2D.Float center, Helper.myDub angle, Color c, int life){
		Laser l = new Laser(center, angle, c);
		l.setLife(life);
		add(l);
		return l;
	}
	
	/**
	 * Spawns an explosion of between 25-100 particles, all sharing a random color,
	 * vector, and decay rate. Particles are spawned in an equal division around
	 * the center point.
	 * 
	 * @param center
	 *            Center point to spawn around.
	 */
	public static void spawnRandomExplosion(Point2D.Float center) {
		Random gen = new Random();
		int num = gen.nextInt(25) + 50;
		//GameEngine.log("Num: " + num);
		double angleInc = (2 * Math.PI) / num;
		double rndAngleAdd;
		float speed = gen.nextFloat() + 500f;
		Color c = new Color(gen.nextFloat(), gen.nextFloat(), gen.nextFloat(),
				1.0f);
		c.brighter();
		for (int x = 0; x < num; x++) {
			rndAngleAdd = gen.nextDouble() * angleInc * 2 - (angleInc);
			Point2D.Float vector = new Point2D.Float();
			// engine.log("Angle " + Math.toDegrees(angleInc*x));
			vector.x = (float) (speed * Math.cos(angleInc * x + rndAngleAdd));
			vector.y = (float) (speed * Math.sin(angleInc * x + rndAngleAdd));
			// engine.log("Vector " + vector.x + ", " + vector.y);
			addBullet(center, vector, c);
			addLaser(center, new Helper.myDub(gen.nextDouble() * 2 * Math.PI), c);
		}
	}
}
