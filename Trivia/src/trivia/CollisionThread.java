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
	private Object[] actors;

	/**
	 * Creates a new CollisionThread.
	 * 
	 * @param a
	 *            Actor to check collisions with
	 * @param objects
	 *            Collection of Actors to check collisions against
	 */
	public CollisionThread(Actor a, Object[] objects) {
		actor = a;
		actors = objects;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		for (Object a : actors) {
			Actor b = (Actor) (a);
			if (!actor.equals(b))
				actor.checkCollision(b);
		}
	}

}
