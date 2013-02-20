package Actors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

import trivia.GameEngine;

public class Bullet extends CollidingParticle {

	protected Bullet(Float center, Float velocity, Color c) {
		super(center, c);

		Polygon poly = new Polygon(new int[] {-2, 2, -2}, new int[] {2, 0, -2}, 3);
		poly = applyAffineTransform(poly, AffineTransform.getScaleInstance(2, 2));
		poly = applyAffineTransform(poly, AffineTransform.getRotateInstance(Math.atan2(velocity.y, velocity.x)));
		setBasePoly(poly);
		setVel(velocity.x, velocity.y);
	}

	public void draw(Graphics g) {
		// Draws a fading tail behind the Particle
		Color tmpClr;
		Point2D.Float originalCenter = (Float) center.clone();
		int maxTrail = 40;// represents how long the tail should be
		for (int a = 0; a <= maxTrail; a += 10) {
			double alphMod = 1 - (double) a / maxTrail;
			alphMod *= alphMod;
			tmpClr = new Color(drawClr.getRed(), drawClr.getGreen(),
					drawClr.getBlue(), (int) (drawClr.getAlpha() * alphMod));
			g.setColor(tmpClr);
			setCenter(originalCenter.x - vectVel.x * (a / 1000F), originalCenter.y - vectVel.y * (a / 1000F));
			g.fillPolygon(drawPoly);
		}
		setCenter(originalCenter.x, originalCenter.y);
	}

	@Override
	public boolean checkCollision(Actor other) {
		return false;
	}
}
