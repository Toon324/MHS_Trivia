package trivia;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

import aiControls.RotateSearch;

/**
 * Fights with a heavy reliance on random movement and shots, will fire a shot
 * whenever one of its spokes lines up with an enemy
 * 
 * @author dsmiller95
 * 
 */

public class Square extends Actor {

	Point currentDest;
	static float MAX_ACCEL = 1000F;
	private double viewAngle;
	private double viewDist;

	public Square(boolean debugMode,GameEngine e) {
		super(debugMode,e);
		Polygon poly = new Polygon();

		int width = 30, height = 30;
		poly.addPoint(width, 0);
		poly.addPoint(0, -height);
		poly.addPoint(-width, 0);
		poly.addPoint(0, height);
		setBasePoly(poly);
		viewAngle = Math.PI / 16;
		viewDist = 150;
		aiCtrl.add(new RotateSearch(this));
		drawClr = Color.red;
	}

	public void move(int ms) {
		
		if(currentDest == null)
			currentDest = new Point((int) ((Math.random() * 200 - 100) + center.x), (int) ((Math.random() * 200 - 100) + center.y));

		viewArea = new ArrayList<Polygon>();
		ArrayList<Actor> list = new ArrayList<Actor>();
		for(int i = 0; i < 360; i += 90){
			list = getActorsInView(Math.toRadians((Math.toDegrees(angle) + i) % 360), viewAngle, viewDist);
			
			if (!list.isEmpty()) {
				for (Actor a : list) {
					if (!a.equals(this) && a instanceof Triangle) {
						boolean right = false, left = false; //used to determine if the triangle is in the sights
						double cornerAngle = (angle + Math.toRadians(i)) % (Math.PI * 2);
						Polygon target = a.drawPoly;
						double pntAngle;
						for(int j = 0; j < target.npoints; j++){
							pntAngle = (-Math.atan2(target.xpoints[j] - center.x, target.ypoints[j] - center.y) + Math.PI * 5/2) % (Math.PI * 2);
							System.out.println("Angle: " + Math.toDegrees(pntAngle) + "Corner Angle: " + Math.toDegrees(cornerAngle));
							if(pntAngle > cornerAngle){
								left = true;
							}else{
								right = true;
							}
							if(right && left){
								fireShot(i / 90);
								break;
							}
						}
					}
				}
			}
		}
		
		vectVel.x += (ms / 1000F)
				* getAccelToReach(currentDest.x - center.x, vectVel.x,
						MAX_ACCEL);
		vectVel.y += (ms / 1000F)
				* getAccelToReach(currentDest.y - center.y, vectVel.y,
						MAX_ACCEL);
		super.move(ms);
	}
	
	/**
	 * Fires a shot from the given spoke, 0-3
	 * @param spoke
	 */
	
	private void fireShot(int spoke){
		double speed = 5;
		double shotAngle = (spoke*Math.PI/2 + angle) % (Math.PI*2);
		Point2D.Float shotVel = new Point2D.Float((float) (speed * Math.cos(shotAngle)), (float) (speed * Math.sin(shotAngle)));
		Polygon shotShape = new Polygon(new int[] {-4, 4, 4}, new int[] {0, 3, -3}, 3);
		//System.out.printf("Shot vel: (%.4f, %.4f)\n", shotVel.x, shotVel.y);
		actors.addParticle((Point2D.Float) (center.clone()), shotVel, 0.0, 0.0, Color.magenta);
	}

	@Override
	public float getMaxAccel() {
		return MAX_ACCEL;
	}
}
