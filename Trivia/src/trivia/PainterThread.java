/**
 * 
 */
package trivia;

import java.awt.Graphics;

/**
 * @author Cody
 *
 */
public class PainterThread extends Thread {

	private Actor actor;
	private Graphics graphics;
	
	public PainterThread(Actor a, Graphics g) {
		//System.out.println("New painterThread created.");
		actor = a;
		graphics = g.create();
	}

	public void run()
	{
		actor.draw(graphics);
	}
}
