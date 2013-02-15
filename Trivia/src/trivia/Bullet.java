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
public class Bullet extends Actor {

	private int alpha;

	public Bullet(GameEngine e, Point2D.Float vectorSpeed, Color c) {
		super(e);
		alpha = 255;
		vectVel = vectorSpeed;
		drawClr = c;
		basePoly.addPoint(0, 0);
		basePoly.addPoint(4, 0);
		basePoly.addPoint(4, 4);
		basePoly.addPoint(0, 4);
	}

	public void draw(Graphics g) {
		// Draws a fading tail behind the Particle
		for (int a = 0; a <= 10; a += 1) {
			drawClr = new Color(drawClr.getRed(), drawClr.getGreen(),
					drawClr.getBlue(), alpha / (a + 1));
			g.setColor(drawClr);
			g.fillRect((int) (center.x - vectVel.x/6 * a),
					(int) (center.y - vectVel.y/6 * a), 4, 4);
			//engine.log("Drawn at " + (center.x - vectVel.x*a) + ", " + (center.y - vectVel.y*a));
		}
	}

	public void move(int ms) {
		setCenter(center.x + (vectVel.x * (ms / 100f)), center.y
				+ (vectVel.y * (ms / 100f)));
	}

	@Override
	public void setCenter(float x, float y) {
		if (x < 0 || y < 0 || x > engine.windowWidth || y > engine.windowHeight) {
			death = true;
			return;
		}
		super.setCenter(x, y);
	}
	
	public void checkCollision(Actor other) {
		return; //Particle does not collide
	}

	public String toString() {
		return "Particle";
	}

	public void setCenter(Point2D.Float center) {
		this.center = center;
	}
}
