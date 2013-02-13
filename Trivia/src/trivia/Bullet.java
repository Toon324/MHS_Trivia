/**
 * 
 */
package trivia;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D.Float;

/**
 * @author Cody
 *
 */
public class Bullet extends Particle {

	/**
	 * @param debugMode
	 * @param e
	 * @param vectorSpeed
	 * @param c
	 */
	public Bullet(GameEngine e, Float vectorSpeed, Color c) {
		super(e, vectorSpeed, c);
	}

}
