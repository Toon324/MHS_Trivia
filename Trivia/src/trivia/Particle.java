/**
 * 
 */
package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

/**
 * @author Cody Swendrowski, Dan Miller
 *
 */
public class Particle extends Actor {
	
	private int alpha;
	private Color color;
	private Point2D.Float vector;
	private double alphaDecay, speedDecay;
	
	/**
	 * @param debugMode
	 * @param p
	 */
	public Particle(boolean debugMode) {
		super(debugMode);
		alpha = 255;
	}
	
	public Particle(boolean debugMode, Point2D.Float vectorSpeed, double alphaDecayRate, double speedDecayRate, Color c)
	{
		super(debugMode);
		alpha = 255;
		vector = vectorSpeed;
		alphaDecay = alphaDecayRate;
		speedDecay = speedDecayRate;
		color = c;
	}
	
	public void draw(Graphics g)
	{
		//log("Drawn at " + center.x + " " + center.y);
		
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		g.setColor(color);
		g.fillOval((int)(center.x),(int) (center.y), 10, 10);
	}
	
	public void move(int w, int h)
	{
		speed -= speedDecay;
		//alpha -= alphaDecay;
		setCenter((int)(center.x+vector.x), (int)(center.y+vector.y));
	}

	public void setVectorSpeed(int x, int y)
	{
		vector.x = x;
		vector.y = y;
	}
	
	public void setAlphaDecayRate(int r)
	{
		alphaDecay = r;
	}
	
	public void setSpeedDecayRate(int r)
	{
		speedDecay = r;
	}
	
	public void setColor(Color c)
	{
		color = c;
	}
	
	public void setVectorSpeed(Point2D.Float vectorSpeed)
	{
		vector = vectorSpeed;
	}

	@Override
	public float getMaxAccel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setCenter(Float center) {
		this.center = center;
	}

	public static void runParticles(int ms) {
		// TODO Auto-generated method stub
		
	}
}
