/**
 * 
 */
package trivia;


/**
 * Calculates the collisions of one actor in relation to the group of actors.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class CollisionThread implements Runnable {

	private Actor actor;
	private Actor[] actors;

	/**
	 * Creates a new CollisionThread.
	 * 
	 * @param a
	 *            Actor to check collisions with
	 * @param objects
	 *            Collection of Actors to check collisions against
	 */
	public CollisionThread(Actor a, Actor[] acts) {
		actor = a;
		actors = acts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		for (Actor a : actors) {
			if (!actor.equals(a) && actor.center.distance(a.center) <= actor.radius + a.radius)
				actor.checkCollision(a);
		}
	}

}
