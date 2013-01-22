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
		basePoly.addPoint(1, 1);
	}
	
	public void move(int w, int h)
	{
		setAngle(angle + 90);
	}
	
	public void checkCollision(Actor other)
	{
		return;
	}

}
