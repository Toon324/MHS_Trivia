package trivia;

import java.awt.Graphics;

/**
 * @author Cody Swendrowski
 *
 */
public interface GameMode {
	
	public void run();
	public void clicked(int x, int y);
	public void paint(Graphics g);

}
