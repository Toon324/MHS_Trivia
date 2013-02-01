package aiControls;

import java.awt.Point;
import java.awt.geom.Point2D;

import trivia.Actor;

public class RandomWander extends AI_Control {

	Point currentDest;
	Point2D.Float velocityVect;

	public RandomWander(Actor a) {
		super(a);
		currentDest = new Point((int) (Math.random() * 500),
				(int) (Math.random() * 500));
	}

	@Override
	public void run(int ms) {
		velocityVect = actor.getVelocity();//must pass object
		Point2D.Float center = actor.getCenter();
		
		velocityVect.x += (ms / 1000F)
				* Actor.getAccelToReach(currentDest.x - center.x,
						velocityVect.x, actor.getMaxAccel());
		velocityVect.y += (ms / 1000F)
				* Actor.getAccelToReach(currentDest.y - center.y,
						velocityVect.y, actor.getMaxAccel());
		if (center.distance(currentDest) <= 10) {
			currentDest = new Point((int) (Math.random() * 500),
					(int) (Math.random() * 500));
		}
	}

}
