package Actors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import trivia.GameEngine;

import aiControls.AI_Control;
import aiControls.RandomWander;

public abstract class AI_Actor extends Actor {

	protected Point currentDest;
	protected double viewAngle;
	protected int viewDist;
	ArrayList<Polygon> viewArea;
	protected ArrayList<AI_Control> aiCtrl;
	protected double health;

	protected boolean death;

	public AI_Actor() {
		super();
		death = false;
		viewArea = new ArrayList<Polygon>();
		aiCtrl = new ArrayList<AI_Control>();
		aiCtrl.add(new RandomWander(this, GameEngine.getEnv()));
		health = 100;
	}

	public void draw(Graphics g) {
		super.draw(g);

		drawPolyCenterLines(g, drawPoly, new Point((int) center.x,
				(int) center.y));
		g.setColor(Color.red);
		if (viewArea != null && !viewArea.isEmpty())
			for (Polygon p : viewArea)
				g.drawPolygon(p);
		viewArea = new ArrayList<Polygon>();
	}

	public void move(int ms) {
		AI_Control[] tmp = aiCtrl.toArray(new AI_Control[0]);
		if (health <= 0) {
			this.kill();
			return;
		}
		for (AI_Control ai : tmp)
			ai.run(ms);

		super.move(ms);
	}

	public boolean checkCollision(Actor other) {
		// this will still check actors against particles, but not particles
		// against actors

		if (this.getClass().equals(other.getClass())
				|| !(other instanceof AI_Actor || other instanceof CollidingParticle))
			return false;

		Polygon otherPoly = other.basePoly;
		for (int i = 0; i < otherPoly.npoints; i++) {
			if (!other.remove
					&& basePoly.contains(otherPoly.xpoints[i],
							otherPoly.ypoints[i])) {
				if (onCollide(other))
					return true;
			}
		}
		return false;
	}

	/**
	 * Called when this actor has collided with another. PRECONDITION: The actor
	 * passed must not be flagged for removal, or equal to this object
	 * 
	 * @param other
	 *            The actor this actor has collided with
	 * 
	 * @return boolean if the actor has died
	 */
	public boolean onCollide(Actor other) {
		if (other instanceof Particle) {
			other.remove = true;
			this.damage(1);
			if (health <= 0) {
				this.kill();
				return true;
			}
		} else if (other instanceof AI_Actor) {
			kill();
			((AI_Actor) other).kill();
			return true;
		}
		return false;
	}

	public boolean hasAIClass(Class<?> cls) {
		for (AI_Control a : aiCtrl)
			if (a.getClass() == cls)
				return true;
		return false;
	}

	public void addAI_Control(AI_Control ctrl) {
		aiCtrl.add(ctrl);
	}

	public void removeAI_Control(AI_Control ctrl) {
		aiCtrl.remove(ctrl);
	}

	public void clearAI_Control() {
		aiCtrl = new ArrayList<AI_Control>();
	}

	public ArrayList<Actor> getActorsInView() {
		return getActorsInView(angle, viewAngle, viewDist);
	}

	protected ArrayList<Actor> getActorsInView(double viewAngle,
			double viewRads, double viewDist) {
		ArrayList<Actor> valids = new ArrayList<Actor>();
		ArrayList<Actor> all = actors;

		Polygon tmpArea = new Polygon(
				new int[] {
						(int) center.x,
						(int) (Math.cos(viewAngle + viewRads) * viewDist + center.x),
						(int) (Math.cos(viewAngle - viewRads) * viewDist + center.x) },
				new int[] {
						(int) center.y,
						(int) (Math.sin(viewAngle + viewRads) * viewDist + center.y),
						(int) (Math.sin(viewAngle - viewRads) * viewDist + center.y) },
				3);

		// viewArea.add(tmpArea);

		int[] xPnts, yPnts;
		for (Actor a : all) {
			if (a instanceof Particle)
				break;
			if (a == this)
				continue;
			xPnts = a.drawPoly.xpoints;
			yPnts = a.drawPoly.ypoints;

			for (int i = 0; i < a.drawPoly.npoints; i++) {
				if (tmpArea.contains(xPnts[i], yPnts[i])) {
					valids.add(a);
					break;
				}
			}
		}
		return valids;
	}

	public void damage(double amount) {
		health -= amount;
	}

	public void kill() {
		death = true;
		remove = true;
	}

	public boolean isDead() {
		return death;
	}

	public abstract float getMaxAccel();
	
	/**
	 * 
	 * CURRENTLY NON-FUNCTIONAL
	 * 
	 * @param p1
	 *            origin of the shot
	 * @param p2
	 *            shot target
	 * @param vel2
	 *            shot target velocity
	 * @param ac2
	 *            shot target acceleration
	 * @param speed
	 *            speed of the shot
	 * @return Angle which would produce a shot from p1 which would cross
	 *         through p2
	 */
	public static final double getLeadShotAngle(Point2D.Float p1,
			Point2D.Float p2, Point2D.Float vel, Point2D.Float ac, double speed) {

		GameEngine.log("P1:  " + p1);
		GameEngine.log("P2:  " + p2);
		GameEngine.log("vel: " + vel);
		GameEngine.log("acc: " + ac);
		
		
		double dX = p1.x - p2.x, dY = p1.y - p2.y;
		double a = (ac.y * ac.y + ac.x * ac.x) / 4;
		double b = (vel.y * ac.y + vel.x * ac.x);
		double c = (vel.x * vel.x + vel.y * vel.y - (dX * ac.x + dY * ac.y + speed
				* speed));
		double d = -2 * (dX * vel.x + dY * vel.y);
		double e = dX * dX + dY * dY;

		double x = 2 * c * c * c - 9 * b * c * d + 27 * a * d * d + 27 * b * b
				* e - 72 * a * c * e;
		double y = c * c - 3 * b * d + 12 * a * e;
		double z = Math.pow(x + Math.sqrt(x * x - 4 * y * y * y), 1 / 3);
		double Ba = b / a;
		double Ca = c / a;
		double C1 = /* 2^1/3 */1.2599210498948731647672106072782 * y
				/ (3 * a * z);
		double C2 = z / (3 * 1.2599210498948731647672106072782 * a);

		double M = Math.sqrt(Ba * Ba / 4 + C1 + C2 - 2 * Ca / 3);
		
		double N = Ba * Ba / 2 - C1 - C2 - 4 * Ca / 3;
		double O = (-Ba * Ba * Ba + 4 * Ba * Ca - 8 * d / a) / (4 * M);
		GameEngine.log("N:   " + N);
		GameEngine.log("O:   " + O);
		
		double P, Q, time;
		if (N - O >= 0) {
			P = Math.sqrt(N - O);
			GameEngine.log("Psq: " + (N - O));
			Q = -Ba / 4 - M / 2;
		} else {
			P = Math.sqrt(N + O);
			GameEngine.log("Psq: " + (N + O));
			Q = -Ba / 4 + M / 2;
		}

		if (Q - P >= 0) {
			time = Q - P;
		} else {
			time = Q + P;
		}

		double theta = (Math.atan2(dY - vel.y * time - ac.y * time * time / 2,
				dX - vel.x * time - ac.y * time * time / 2) + Math.PI * 2) % (Math.PI * 2);

		return theta;
	}
}
