/**
 * 
 */
package trivia;

import java.util.ArrayList;

/**
 * @author Cody
 *
 */
public class CollisionThread implements Runnable {

	private Actor actor;
	private Object[] actors;
	
	public CollisionThread(Actor a, Object[] objects) {
		actor = a;
		actors = objects;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		for (Object a : actors)
		{
			Actor b = (Actor) (a);
			if (!actor.equals(b))
				actor.checkCollision(b);
		}
	}

}
