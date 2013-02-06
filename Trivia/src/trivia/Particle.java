package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

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
	public Particle(boolean debugMode, GameEngine e) {
		super(debugMode, e);
		alpha = 255;
	}

	public Particle(boolean debugMode, GameEngine e, Point2D.Float vectorSpeed, Color c) {
		super(debugMode, e);
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
			death = true;
			return;
		}
		super.setCenter(x, y);
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
}
