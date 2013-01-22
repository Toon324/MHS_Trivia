package trivia;

public class Triangle extends Actor {
	
	int timer = 0;
	
	public Triangle(boolean debugMode, int p) {
		super(debugMode, p);
		setSize(30,30);
		speed = 1;
		poly.addPoint(0, 0);
		poly.addPoint(width/2, -height);
		poly.addPoint(width, 0);
	}
	
	public void move(int w, int h)
	{
		timer++;
		if (timer % 100 == 0)
		{
			setAngle(90);
		}
		
	}

}
