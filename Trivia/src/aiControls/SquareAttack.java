package aiControls;

import java.awt.Polygon;
import java.awt.geom.Point2D;

import Actors.AI_Actor;
import Actors.Actor;
import Actors.Square;


import trivia.GameEngine;

public class SquareAttack extends AI_Control {

	Actor target;
	Point2D.Float cen;
	Point2D.Float vel;
	int shots = 0;

	public SquareAttack(Square a, Actor b) {
		super(a);
		target = b;
		if (target == actor)
			actor.removeAI_Control(this);
	}

	@Override
	public void run(int ms) {
		boolean fired = false;
		cen = actor.getCenter();
		vel = actor.getVelocity();
		double angle = actor.getAngle();
		//double targetAngle = AI_Actor.getLeadShotAngle(cen, target.getCenter(), target.getVelocity(), target.getAccel(), ((Square) actor).getShotSpeed());
		//GameEngine.log("Target angle: " + targetAngle);
		for (int i = 0; i < 360; i += 90) {
			boolean right = false, left = false; // used to determine if the
												// enemy is in the shot area
			double cornerAngle = (angle + Math.toRadians(i)) % (Math.PI * 2);
			Polygon targetPoly = target.getDrawnPoly();
			double pntAngle;

			for (int j = 0; j < targetPoly.npoints; j++) {
				pntAngle = (Math.atan2(targetPoly.ypoints[j] - cen.y,
						targetPoly.xpoints[j] - cen.x) + 2 * Math.PI)
						% (Math.PI * 2);
				double difference = (pntAngle - cornerAngle) % (Math.PI * 2);
				if (Math.abs(difference) < (Math.PI / 2))
					if ((pntAngle - cornerAngle) % (Math.PI * 2) > 0) {
						left = true;
					} else {
						right = true;
					}
				if (right && left) {
					((Square) actor).fireShot(i / 90);
					fired = true;
					shots++;
					break;
				}
			}
		}
		if (!fired) {
			actor.removeAI_Control(this);
		}
	}
}
