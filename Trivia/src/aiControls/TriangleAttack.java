package aiControls;

import java.awt.geom.Point2D;

import trivia.Helper;

import Actors.Actor;
import Actors.Triangle;




public class TriangleAttack extends AI_Control {
	
	private TriangleFleet myFleet;
	Point2D.Float velocityVect;
	
	public TriangleAttack(Triangle a, TriangleFleet fleet) {
		super(a);
		myFleet = fleet;
	}

	@Override
	public void run(int ms) {
		velocityVect = actor.getVelocity();
		Point2D.Float center = actor.getCenter(), target = myFleet.getCurrentTarget().getCenter();
		
		
		velocityVect.x += (ms / 1000F)
				* Helper.getAccelToReach(target.x - center.x,
						velocityVect.x, actor.getMaxAccel());
		velocityVect.y += (ms / 1000F)
				* Helper.getAccelToReach(target.y - center.y,
						velocityVect.y, actor.getMaxAccel());
	}

}
