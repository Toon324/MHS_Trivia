package aiControls;

import trivia.Actor;

public class RotateSearch extends AI_Control {

	double startAngle, currentTrueAngle;

	public RotateSearch(Actor a) {
		super(a);
		startAngle = a.getAngle();
		currentTrueAngle = startAngle;
	}

	@Override
	public void run(int ms) {
		double rotateVel = actor.getRotateVel();
		actor.accelerateRotation(Actor.getAccelToReach((float) (startAngle + 2
				* Math.PI - currentTrueAngle), (float) rotateVel, 1)
				* (ms / 1000F));

		currentTrueAngle += actor.getRotateVel() * (ms / 1000F);
		if (startAngle + 1.8 * Math.PI <= currentTrueAngle) {
			startAngle = actor.getAngle();//do it again
			currentTrueAngle = startAngle;
		}
	}

}
