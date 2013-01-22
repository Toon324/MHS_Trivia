package trivia;

public class Triangle extends Actor {
	
	int timer = 0;
	
	public Triangle(boolean debugMode, int p) {
		super(debugMode, p);
		setSize(30,30);
		speed = 1;
		basePoly.addPoint(0, 0);
		basePoly.addPoint(width/2, -height);
		basePoly.addPoint(width, 0);
	}
	
	public void move(int w, int h)
	{
		timer++;
		if (timer % 1 == 0)
		{
			rotate(Math.toRadians(1));
		}
		
	}

}
