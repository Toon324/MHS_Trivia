package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

public class Particle {

	private Point2D.Float center, velocity;
	private float decay, stopSpeed;
	private Polygon shape;
	Color myColor;

	private static Point2D.Float ORIGIN = new Point2D.Float(0, 0);
	static private ArrayList<Particle> particles = new ArrayList<Particle>();
	
	private Particle(Float cen, Float vel, Polygon poly, Color clr, float dec, float stop) {
		center = new Float();
		velocity = vel;
		myColor = clr;
		decay = dec;
		stopSpeed = stop;
		shape = poly;
		setCenter(cen.x, cen.y);
	}
	
	private void draw(Graphics g){
		g.setColor(myColor);
		g.fillPolygon(shape);
		//g.fillRect((int) (center.x - 5), (int) (center.y - 5), 10, 10);
	}
	
	/**
	 * Moves this particle. Returns true when it should be removed
	 * @param ms milliseconds since last call
	 * @return true if it should be removed, false if it should remain in the list
	 */
	private boolean run(int ms){
		setCenter(center.x + (velocity.x * (ms/1000F)), center.y + (velocity.y * (ms/1000F)));
		double newDec = Math.pow(decay, (ms/1000F));
		velocity.x /= (newDec);
		velocity.y /= (newDec);
		if(velocity.distance(ORIGIN) < stopSpeed)
			return true;
		return false;
	}
	
	private void setCenter(float x, float y) {
		shape.translate((int) -center.x, (int) -center.y);
		center.x = x;
		center.y = y;
		shape.translate((int) center.x, (int) center.y);
	}
	
	static public void addParticle(Float cenVect, Float velVect, Polygon poly, Color clr, float decay, float stopSpeed){
		Particle p = new Particle(cenVect, velVect, poly, clr, decay, stopSpeed);
		particles.add(p);
	}
	
	static public void runParticles(int ms){
		ArrayList<Particle> toRemove = new ArrayList<Particle>();
		for(Particle p : particles){
			if(p.run(ms))
				toRemove.add(p);
		}
		for(Particle p : toRemove){
			particles.remove(p);
		}
	}
	
	static public void drawParticles(Graphics g){
		for(Particle p : particles)
			p.draw(g);
	}
}
