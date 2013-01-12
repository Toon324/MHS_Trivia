package trivia;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/*import java.awt.Graphics2D;
 import java.awt.geom.AffineTransform;
 import java.awt.image.AffineTransformOp;*/

/**
 * Generic class for all objects in the game.
 * 
 * @author Cody Swendrowski
 */
public class Actor {
	public int width, height, speed, x_pos, y_pos, score, xTile, yTile, tile,
			tileSpeed, tiles, dir, attack, life;
	public GameImage image;
	private int pos;
	protected boolean death;
	private Rectangle bounds = new Rectangle();

	/**
	 * Creates a new Actor.
	 * 
	 * @param i
	 *            GameImage of Actor
	 * @param p
	 *            Position in ArrayList
	 */
	public Actor(GameImage i, int p) {
		image = i;
		dir = 0;
		x_pos = 0;
		y_pos = 0;
		width = 0;
		height = 0;
		score = 0;
		xTile = 0;
		yTile = 0;
		attack = 0;
		life = 100;
		tile = 0;
		tileSpeed = 0;
		tiles = 0;
		speed = 3;
		death = false;
		pos = p;
		bounds.setSize(width, height);
	}

	/**
	 * Draws the Actor.
	 * 
	 * @param p
	 *            Corner of the screen to draw in reference to
	 * @param g
	 *            Graphics to be drawn with
	 */
	public void draw(Point p, Graphics g) {
		// ensure no dead objects are draw
		if (!death) {
			BufferedImage i = image.getImage(xTile, yTile);
			if (i == null) {
				return;
			}
			g.drawImage(i, x_pos - p.x, y_pos - p.y, x_pos + width - p.x, y_pos
					+ height - p.y, 0, 0, i.getWidth(), i.getHeight(), null);
		}
	}

	/**
	 * Moves the Actor.
	 * 
	 * @param w
	 *            Width of window to draw in
	 * @param h
	 *            Height of window to draw in
	 */
	public void move(int w, int h) {
		tile++;
		if (tile > tileSpeed) {
			xTile++;
			tile = 0;
			if (xTile > tiles) {
				xTile = 0;
			}
		}
	}

	/**
	 * Returns known position in Actors.
	 * 
	 * @return position in Actors
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * Sets known position in Actors.
	 * 
	 * @param p
	 *            int position to set
	 */
	public void setPos(int p) {
		pos = p;
	}

	/**
	 * Sets the known bounds of the Actor. Should not be called past
	 * initialization.
	 * 
	 * @param x
	 *            Width of bounds
	 * @param y
	 *            Height of bounds
	 */
	public void setBounds(int x, int y) {
		bounds.setBounds(new Rectangle(x, y));
	}

	/**
	 * Returns true if Actor is dead; else, returns false.
	 * 
	 * @return death
	 */
	public boolean isDead() {
		return death;
	}

	/**
	 * Returns score value of Actor.
	 * 
	 * @return score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Sets the score value of the Actor. Should not be called after
	 * initialization.
	 * 
	 * @param s
	 *            the score to set
	 */
	public void setScore(int s) {
		score = s;
	}

	/**
	 * Checks to see if Actor is colliding with another Actor.
	 * 
	 * @param other
	 *            Actor to check collision against
	 */
	public void checkCollision(Actor other) {
		// only checks enemy collisions
		// to be overridden
	}

	/**
	 * Returns speed value of Actor.
	 * 
	 * @return speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Returns x_pos of Actor.
	 * 
	 * @return x_pos
	 */
	public int getX() {
		return x_pos;
	}

	/**
	 * Returns y_pos of Actor.
	 * 
	 * @return y_pos
	 */
	public int getY() {
		return y_pos;
	}

	/**
	 * Sets x_pos of Actor.
	 * 
	 * @param x
	 *            int x_pos to set
	 */
	public void setX(int x) {
		x_pos = x;
	}

	/**
	 * Sets y_pos of Actor.
	 * 
	 * @param y
	 *            int y_pos to set
	 */
	public void setY(int y) {
		y_pos = y;
	}

	/**
	 * Returns the direction that the Actor is facing.
	 * 
	 * @return dir
	 */
	public int getDir() {
		return dir;
	}

	/**
	 * Sets the direction that the Actor is facing.
	 * 
	 * @param d
	 *            Direction to set as.
	 */
	public void setDir(int d) {
		dir = d;
	}

	/**
	 * Reduces health of Actor. If health is less than 0, the Actor dies.
	 * 
	 * @param a
	 *            Amount of health to reduce by.
	 */
	public void addDamage(int a) {
		if (life == -324) {
			return;
		}
		life -= a;
		if (life <= 0) {
			setDeath(true);
		}
	}

	/**
	 * Sets death to parameter.
	 * 
	 * @param d
	 *            boolean to set death to
	 */
	public void setDeath(boolean d) {
		death = d;
	}

	/**
	 * Allows System to print name of object. Returns the name of the Actor
	 * 
	 * @return "Actor"
	 */
	public String toString() {
		return "Actor";
	}

}
