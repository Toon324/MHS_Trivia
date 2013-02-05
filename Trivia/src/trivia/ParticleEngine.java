package trivia;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * @author Cody Swendrowski, Dan Miller
 *
 */
public class ParticleEngine {
	
	private GameEngine engine;
	
	public ParticleEngine(GameEngine e)
	{
		engine = e;
	}
	
	public void spawnRandomExplosion(Point2D.Float center)
	{
		Random gen = new Random();
		int num = gen.nextInt(11)+3;
		//engine.log("Num: " + num);
		double angleInc = (2*Math.PI)/num;
		float speed = gen.nextFloat();
		double speedDecay = gen.nextDouble();
		Color c = new Color(gen.nextFloat(),gen.nextFloat(),gen.nextFloat(),1.0f);
		c.brighter();
		for (int x=0; x< num; x++)
		{
			Point2D.Float vector = new Point2D.Float();
			//engine.log("Angle " + Math.toDegrees(angleInc*x));
			vector.x = (float) (speed*Math.cos(angleInc*x));
			vector.y = (float) (speed*Math.sin(angleInc*x));
			//engine.log("Vector " + vector.x + ", " + vector.y);
			engine.actors.addParticle(center, vector, 1/speed, speedDecay, c);
		}
	}

}
