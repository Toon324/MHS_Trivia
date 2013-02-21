package aiControls;

import java.awt.Polygon;
import java.awt.geom.Point2D;

import Actors.AI_Actor;
import Actors.Actor;
import Actors.Square;


import trivia.GameEngine;
import trivia.Helper;

public class SquareAttack extends AI_Control {

	Actor target;
	Point2D.Float cen;
	Point2D.Float vel;
	int shots = 0;
	double laserTime;

	public SquareAttack(Square a, Actor b) {
		super(a);
		laserTime = Square.laserChargeTime;
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
		Polygon targetPoly = target.getDrawnPoly();
		if(targetPoly == null) {actor.removeAI_Control(this); return;}
		
		//double leadAngle = Helper.getAngleAtTime(actor.getCenter(), target.getCenter(), target.getVelocity(), target.getAccel(), laserTime * 4 / 5);
		
		//double targetAngle = AI_Actor.getLeadShotAngle(cen, target.getCenter(), target.getVelocity(), target.getAccel(), ((Square) actor).getShotSpeed());
		//GameEngine.log("Target angle: " + targetAngle);
		
		//the possible change in angle after fired now
		double angleChange = ((laserTime * 4 / 5)/1000F) * actor.getRotateVel();
		for (int i = 0; i < 360; i += 90) {
			double cornerAngle = (angle + Math.toRadians(i) + angleChange) % (Math.PI * 2);
			if(Helper.doesPolarIntersectPoly(cen, cornerAngle, target.getDrawnPoly())){
				//((Square) actor).fireShot(i / 90);
				((Square) actor).chargeLaser(i / 90);
				fired = true;
				shots++;
			}
		}
		if (!fired) {
			actor.removeAI_Control(this);
		}
	}
}
