/**
 * 
 */
package trivia;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * @author Cody
 *
 */
public class FightingActor extends Actor {

	private float MAX_ACCEL = 100F;// Pixels/s/s
	protected int destination, evade;
	protected long lastShot;
	protected Point2D.Float shotVel = new Point2D.Float();
	/**
	 * @param debugMode
	 * @param e
	 */
	public FightingActor(GameEngine e) {
		super(e);
		evade = 75; // % chance to evade a shot
	}

	/* (non-Javadoc)
	 * @see trivia.Actor#getMaxAccel()
	 */
	@Override
	public float getMaxAccel() {
		return MAX_ACCEL;
	}
	
	public void setEvade(int e) {
		evade = e;
	}
	
	public void fire() {
		engine.actors.fireBullet(center, drawClr, new Point2D.Float(shotVel.x,0));
	}
	
	public void checkCollision(Actor other) {
		if (other.equals(this) || other.center.x - center.x > 5 || other.center.y - center.y > 5 || drawClr.toString().equalsIgnoreCase(other.drawClr.toString()))
			return;
		
		//Checks to see if FightingActor evades the collision
		Random gen = new Random();
		int temp = gen.nextInt(100)+1;
		if (temp < evade)
			return;
		
		Polygon otherPoly = other.basePoly;
		for (int i = 0; i < otherPoly.npoints; i++) {
			if (basePoly.contains(new Point(otherPoly.xpoints[i], otherPoly.ypoints[i]))) {
				setDeath(true);
				other.setDeath(true);
			}
		}
	}
	
	public String toString() {
		return "FightingActor";
	}

}
