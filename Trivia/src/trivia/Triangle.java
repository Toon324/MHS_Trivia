package trivia;

import java.awt.Polygon;

public class Triangle extends Actor {
	
	int targetX;
	
	public Triangle(boolean debugMode, int p, int tX) {
		super(debugMode, p);
		setSize(30,30);
		Polygon poly = new Polygon();
		poly.addPoint(0, 0);
		poly.addPoint(width/2, -height);
		poly.addPoint(width, 0);
		setBasePoly(poly);
		targetX = tX;
		//setCenter(200, 200);
		speed = 1;
	}
	
	public void move(int w, int h)
	{
		//setCenter(center.x + 1, center.y);
		if (center.x < targetX)
		{
			setCenter(center.x + 1, center.y);
		}
	}

}
