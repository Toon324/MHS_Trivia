/**
 * 
 */
package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

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
	public Particle(boolean debugMode, GameEngine e) {
		super(debugMode,e);
		alpha = 255;
	}
	
	public Particle(boolean debugMode, GameEngine e, Point2D.Float vectorSpeed, double alphaDecayRate, double speedDecayRate, Color c)
	{
		super(debugMode,e);
		alpha = 255;
		vector = vectorSpeed;
		alphaDecay = alphaDecayRate;
		speedDecay = speedDecayRate;
		color = c;
	}
	
	public void draw(Graphics g)
	{
		//log("Drawn at " + center.x + " " + center.y);
		
		for (int a=0; a<= 200; a += 25)
		{
			color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha/(a+1));
			g.setColor(color);
			g.fillRect((int)(center.x - vector.x * a),(int) (center.y - vector.y*a), 4, 4);
		}
	}
	
	public void move(int ms)
	{
		//vector.x -= speedDecay;
		//vector.y -= speedDecay;
		//alpha -= alphaDecay;
		setCenter(center.x+vector.x, center.y+vector.y);
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
	public void setCenter(float x, float y) {
		if (x < 0 || y < 0 || x > engine.windowWidth || y > engine.windowHeight) {
			death = true;
			return;
		}
		super.setCenter(x, y);
	}

	public void setCenter(Point2D.Float center) {
		this.center = center;
	}

	@Override
	public float getMaxAccel() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String toString()
	{
		return "Particle";
	}
}
