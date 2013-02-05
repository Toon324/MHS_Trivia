package trivia;

import java.awt.Graphics;
import java.awt.geom.Point2D;

/**
 * A mode used for testing of new features.
 * @author Cody Swendrowski
 *
 */
public class Sandbox extends GameMode {

	/**
	 * @param eng
	 */
	public Sandbox(GameEngine eng) {
		super(eng);
	}

	@Override
	public void clicked(int x, int y) {
		//engine.log("Clicked at " + x + ", " + y);
		engine.particleEngine.spawnRandomExplosion(new Point2D.Float(x,y));
	}

	@Override
	public void run(int ms) {
		engine.actors.handleActors(ms);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		engine.actors.drawActors(g);
	}

	@Override
	public String toString() {
		return "Sandbox";
	}
	
	

}
