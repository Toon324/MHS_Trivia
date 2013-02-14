package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.Random;

/**
 * A single point Actor that does not interact with others. Used for particle
 * effects.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class Particle extends Actor {

	private int alpha;
	private Color color;

	/**
	 * @param debugMode
	 * @param p
	 */
	public Particle() {
		super();
		alpha = 255;
	}

	public Particle(Point2D.Float vectorSpeed, Color c) {
		super();
		alpha = 255;
		vectVel = vectorSpeed;
		color = c;
		basePoly = new Polygon(new int[] {-1, -1, 1, 1},
								new int[] {-1, 1, -1, 1},
								4);
	}

	public void draw(Graphics g) {
		// Draws a fading tail behind the Particle
		for (int a = 0; a <= 2000/*represents how long, in ms, the tail should be*/; a += 100) {
			color = new Color(color.getRed(), color.getGreen(),
					color.getBlue(), alpha / (a + 1));
			g.setColor(color);
			g.fillRect((int) (center.x - vectVel.x * a),
					(int) (center.y - vectVel.y * a), 4, 4);
		}
	}
	
	//move in Actor is sufficient

	@Override
	public void setCenter(float x, float y) {
		if (x < 0 || y < 0 || x > engine.windowWidth || y > engine.windowHeight) {
			remove = true;
			return;
		}
		super.setCenter(x, y);
	}

	
	public String toString(){
		return "Particle " + super.toString();
	}

	public static void addParticle(Point2D.Float center, Point2D.Float vectorSpeed,
			Color c) {
		Particle p = new Particle(vectorSpeed, c);
		p.setCenter(center.x, center.y);
		add(p);
	}
	
	/**
	 * Spawns an explosion of between 25-100 particles, all sharing a random color,
	 * vector, and decay rate. Particles are spawned in an equal division around
	 * the center point.
	 * 
	 * @param center
	 *            Center point to spawn around.
	 */
	public static void spawnRandomExplosion(Point2D.Float center) {
		Random gen = new Random();
		int num = gen.nextInt(25) + 50;
		//GameEngine.log("Num: " + num);
		double angleInc = (2 * Math.PI) / num;
		double rndAngleAdd;
		float speed = gen.nextFloat() + 500f;
		Color c = new Color(gen.nextFloat(), gen.nextFloat(), gen.nextFloat(),
				1.0f);
		c.brighter();
		for (int x = 0; x < num; x++) {
			rndAngleAdd = gen.nextDouble() * angleInc * 2 - (angleInc);
			Point2D.Float vector = new Point2D.Float();
			// engine.log("Angle " + Math.toDegrees(angleInc*x));
			vector.x = (float) (speed * Math.cos(angleInc * x + rndAngleAdd));
			vector.y = (float) (speed * Math.sin(angleInc * x + rndAngleAdd));
			// engine.log("Vector " + vector.x + ", " + vector.y);
			Particle.addParticle(center, vector, c);
		}
	}
}
