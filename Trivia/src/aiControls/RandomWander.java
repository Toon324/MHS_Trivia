package aiControls;

import java.awt.Point;
import java.awt.geom.Point2D;

import Actors.AI_Actor;
import Actors.Actor;


public class RandomWander extends AI_Control {

	Point currentDest;
	Point2D.Float velocityVect;
	Point envSize;

	public RandomWander(AI_Actor a, Point env) {
		super(a);
		envSize = env;
		currentDest = new Point((int) (Math.random() * envSize.x),
				(int) (Math.random() * envSize.y));
	}

	@Override
	public void run(int ms) {
		velocityVect = actor.getVelocity();// must pass object
		Point2D.Float center = actor.getCenter();

		actor.setAccel(new Point2D.Float(
				Actor.getAccelToReach(currentDest.x - center.x, velocityVect.x, actor.getMaxAccel()),
				Actor.getAccelToReach(currentDest.y - center.y, velocityVect.y,	actor.getMaxAccel())));

		if (center.distance(currentDest) <= 10) {
			currentDest = new Point((int) (Math.random() * envSize.x),
					(int) (Math.random() * envSize.y));
		}
	}
}
