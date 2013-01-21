package trivia;

public class Triangle extends Actor {
	
	int timer = 0;
	
	public Triangle(int p) {
		super(p);
		setSize(30,30);
		speed = 1;
		poly.addPoint(0, 0);
		poly.addPoint(0, height);
		poly.addPoint(width, height/2);
	}
	
	public void move(int w, int h)
	{
		timer++;
		if (timer % 100 == 0)
		{
			setAngle(angle+1);
		}
		
	}

}
