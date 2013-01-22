/**
 * 
 */
package trivia;

/**
 * @author Swen Server
 *
 */
public class SinglePoint extends Actor {

	/**
	 * @param debugMode
	 * @param p
	 */
	public SinglePoint(boolean debugMode, int p) {
		super(debugMode, p);
		poly.addPoint(1, 1);
	}
	
	public void move(int w, int h)
	{
		setAngle(angle+90);
	}

}
