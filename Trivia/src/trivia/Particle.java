package trivia;

import java.awt.Color;
import java.awt.Graphics;
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
	private Point2D.Float vector;

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
		vector = vectorSpeed;
		color = c;
	}

	public void draw(Graphics g) {
		// Draws a fading tail behind the Particle
		for (int a = 0; a <= 130; a += 15) {
			color = new Color(color.getRed(), color.getGreen(),
					color.getBlue(), alpha / (a + 1));
			g.setColor(color);
			g.fillRect((int) (center.x - vector.x * a),
					(int) (center.y - vector.y * a), 4, 4);
		}
	}

	public void move(int ms) {
		setCenter(center.x + (vector.x * (ms / 100f)), center.y
				+ (vector.y * (ms / 100f)));
	}

	@Override
	public void setCenter(float x, float y) {
		if (x < 0 || y < 0 || x > engine.windowWidth || y > engine.windowHeight) {
			remove = true;
			return;
		}
		super.setCenter(x, y);
	}

	public static void addParticle(Point2D.Float center, Point2D.Float vectorSpeed,
			Color c) {
		Particle p = new Particle(vectorSpeed, c);
		p.setCenter(center);
		add(p);
	}
	
	@Override
	public float getMaxAccel() {
		return 0;
	}

	public String toString() {
		return "Particle";
	}

	public void setCenter(Point2D.Float center) {
		this.center = center;
	}
	
	/**
	 * Spawns an explosion of up to 13 particles, all sharing a random color,
	 * vector, and decay rate. Particles are spawned in an equal division around
	 * the center point.
	 * 
	 * @param center
	 *            Center point to spawn around.
	 */
	public static void spawnRandomExplosion(Point2D.Float center) {
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
			Particle.addParticle(center, vector, c);
		}
	}
}
