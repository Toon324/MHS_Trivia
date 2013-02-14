package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

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
		aiCtrl.add(new RandomWander(this, GameEngine.envSize));
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

		if (this.getClass().equals(other.getClass()))
			return false;

		Polygon otherPoly = other.basePoly;
		for (int i = 0; i < otherPoly.npoints; i++) {
			if (!other.remove && basePoly.contains(otherPoly.xpoints[i], otherPoly.ypoints[i])) {
				if(onCollide(other)) return true;;
			}
		}
		return false;
	}

	/**
	 * Called when this actor has collided with another.
	 * PRECONDITION: The actor passed must not be flagged for removal, or equal to this object
	 * 
	 * @param other The actor this actor has collided with
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
}
