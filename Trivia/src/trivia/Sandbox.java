package trivia;

import java.awt.Graphics;
import java.awt.geom.Point2D;

/**
 * A mode used for testing of new features.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class Sandbox extends GameMode {

	/**
	 * Creates a new Sandbox controller.
	 * @param eng GameEngine to utilize
	 */
	public Sandbox(GameEngine eng) {
		super(eng);
	}

	@Override
	public void clicked(int x, int y) {
		//engine.log("Clicked at " + x + ", " + y);
		Particle.spawnRandomExplosion(new Point2D.Float(x,y));
	}

	@Override
	public void run(int ms) {
		Actor.handleActors(ms);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Actor.drawActors(g);
	}

	@Override
	public String toString() {
		return "Sandbox";
	}
	
	

}
