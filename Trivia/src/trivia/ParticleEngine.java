package trivia;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Controls the logic behind the creation of differing Particle explosions.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class ParticleEngine {

	private GameEngine engine;

	/**
	 * Creates a new ParticleEngine.
	 * 
	 * @param e
	 *            GameEngine to use
	 */
	public ParticleEngine(GameEngine e) {
		engine = e;
	}

	/**
	 * Spawns an explosion of up to 13 particles, all sharing a random color,
	 * vector, and decay rate. Particles are spawned in an equal division around
	 * the center point.
	 * 
	 * @param center
	 *            Center point to spawn around.
	 */
	public void spawnRandomExplosion(Point2D.Float center) {
		Random gen = new Random();
		int num = gen.nextInt(11) + 3;
		// engine.log("Num: " + num);
		double angleInc = (2 * Math.PI) / num;
		float speed = gen.nextFloat() + .2f;
		Color c = new Color(gen.nextFloat(), gen.nextFloat(), gen.nextFloat(),
				1.0f);
		c.brighter();
		for (int x = 0; x < num; x++) {
			Point2D.Float vector = new Point2D.Float();
			// engine.log("Angle " + Math.toDegrees(angleInc*x));
			vector.x = (float) (speed * Math.cos(angleInc * x));
			vector.y = (float) (speed * Math.sin(angleInc * x));
			// engine.log("Vector " + vector.x + ", " + vector.y);
			engine.actors.addParticle(center, vector, c);
		}
	}
}
