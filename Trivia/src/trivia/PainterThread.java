package trivia;

import java.awt.Graphics;

/**
 * Handles painting of a given Actor.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class PainterThread extends Thread {

	private Actor actor;
	private Graphics graphics;

	/**
	 * Creates a new PainterThread.
	 * 
	 * @param a
	 *            Actor to paint
	 * @param g
	 *            Graphics to paint with
	 */
	public PainterThread(Actor a, Graphics g) {
		// System.out.println("New painterThread created.");
		actor = a;
		graphics = g.create();
	}

	@Override
	public void run() {
		actor.draw(graphics);
	}
}
