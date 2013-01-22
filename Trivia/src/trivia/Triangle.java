package trivia;

import java.awt.Polygon;

public class Triangle extends Actor {
	
	int timer = 0;
	
	public Triangle(boolean debugMode, int p) {
		super(debugMode, p);
		setSize(30,30);
		Polygon poly = new Polygon();
		poly.addPoint(0, 0);
		poly.addPoint(width/2, -height);
		poly.addPoint(width, 0);
		setBasePoly(poly);
		
		//setCenter(200, 200);
		speed = 1;
	}
	
	public void move(int w, int h)
	{
		timer++;
		if (timer % 1 == 0)
		{
			rotate(Math.toRadians(1));
		}
		setCenter(center.x + 1, center.y + 1);
	}

}
