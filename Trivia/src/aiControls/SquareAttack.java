package aiControls;

import java.awt.Polygon;
import java.awt.geom.Point2D;

import trivia.Actor;
import trivia.Square;

public class SquareAttack extends AI_Control {

	Actor target;
	Point2D.Float cen;
	
	public SquareAttack(Square a, Actor b) {
		super(a);
		target = b;
		if(target == actor)
			actor.removeAI_Control(this);
	}

	@Override
	public void run(int ms) {
		boolean fired = false;
		cen = actor.getCenter();
		double angle = actor.getAngle();
		for(int i = 0; i < 360; i += 90){
			boolean right = false, left = false; //used to determine if the enemy is in the sights
			double cornerAngle = (angle + Math.toRadians(i)) % (Math.PI * 2);
			Polygon targetPoly = target.getDrawnPoly();
			double pntAngle;
			
			for(int j = 0; j < targetPoly.npoints; j++){
				pntAngle = (-Math.atan2(targetPoly.xpoints[j] - cen.x, targetPoly.ypoints[j] - cen.y) + Math.PI * 5/2) % (Math.PI * 2);
				//System.out.println("Angle: " + Math.toDegrees(pntAngle) + "Corner Angle: " + Math.toDegrees(cornerAngle));
				if(pntAngle > cornerAngle){
					left = true;
				}else{
					right = true;
				}
				if(right && left){
					((Square) actor).fireShot(i / 90);
					fired = true;
					break;
				}
			}
		}
		if(!fired){
			System.out.println("Removed firing AI");
			actor.removeAI_Control(this);
		}
	}

}
