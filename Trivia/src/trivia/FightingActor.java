package trivia;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * The base class for an Actor that will participate in combat. Includes
 * variables for location to move to, evade chance, and shot data.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class FightingActor extends Actor {

	protected int destination, evade;
	protected long lastShot;
	protected Point2D.Float shotVel = new Point2D.Float();

	/**
	 * Creates a new FightingActor.
	 * 
	 * @param e
	 *            GameEngine to utilize
	 */
	public FightingActor(GameEngine e) {
		super(e);
		evade = 75; // % chance to evade a shot
	}

	/**
	 * Sets evade chance of Actor
	 * 
	 * @param e
	 *            evade chance, on the scale of 0 to 100, with 100 being always
	 *            evade and 0 being never evade.
	 */
	public void setEvade(int e) {
		evade = e;
	}

	/**
	 * Fires a bullet. Is called from this class instead of Actors to add in
	 * drawClr and ShotVel, as well as match centers.
	 */
	public void fire() {
		engine.actors.fireBullet(center, drawClr, new Point2D.Float(shotVel.x,
				0));
	}

	/**
	 * Overrides checkCollision.
	 * 
	 * If actor being checked is the same color, do not check for collisions.
	 * This is used for team based firing.
	 */
	public void checkCollision(Actor other) {
		if (other.equals(this))
			return;
		int distance = 1;
		if ((other.basePoly.getBounds().y - basePoly.getBounds().y) != 0)
			distance = Math.abs((other.basePoly.getBounds().x - basePoly.getBounds().x)
				/ (other.basePoly.getBounds().y - basePoly.getBounds().y));
		
		GameEngine.log("Distance between " + toString() + " and " + other.toString() + " is: " + distance);

		if (distance > 5
				|| drawClr.toString()
						.equalsIgnoreCase(other.drawClr.toString())
				|| other instanceof Particle)
			return;

		// Checks to see if FightingActor evades the collision
		Random gen = new Random();
		int temp = gen.nextInt(100) + 1;
		if (temp < evade)
			return;

		//If it doesn't evade, check for collision
		Polygon otherPoly = other.basePoly;
		for (int i = 0; i < otherPoly.npoints; i++) {
			if (basePoly.contains(new Point(otherPoly.xpoints[i],
					otherPoly.ypoints[i]))) {
				setDeath(true);
				other.setDeath(true);
			}
		}
	}

	@Override
	public String toString() {
		return "FightingActor";
	}

}
