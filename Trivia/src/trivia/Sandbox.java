package trivia;

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;


/**
 * A mode used for testing of new features.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class Sandbox extends GameMode {

	/**
	 * Creates a new Sandbox controller.
	 * @param eng GameEngine to utilize
	 */
	public Sandbox(GameEngine eng) {
		super(eng);
	}

	@Override
	public void clicked(MouseEvent e) {
		//engine.log("Clicked at " + x + ", " + y);
		if(e.getButton() == MouseEvent.BUTTON1)
			Particle.spawnRandomExplosion(new Point2D.Float(e.getX() ,e.getY()));
		else if(e.getButton() == MouseEvent.BUTTON2)
			Triangle.addTriangle(e.getX(), e.getY());
		else
			Square.addSquare(e.getX(), e.getY());
	}
	
	
	
	@Override
	public void run(int ms) {
		super.run(ms);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Actor.drawActors(g);
	}

	@Override
	public String toString() {
		return "Sandbox";
	}
	
	

}
