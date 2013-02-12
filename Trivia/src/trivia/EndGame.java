package trivia;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Displays all post-game information and allows player to return to Main Menu.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class EndGame extends GameMode {

	/**
	 * Creates a new EndGame controller.
	 * 
	 * @param eng
	 *            GameEngine to utilize
	 */
	public EndGame(GameEngine eng) {
		super(eng);
		buttons.add(new Button("Return to Main Menu", 200, 500));
	}

	public void run(int ms) {
		if (buttons.get(0).isClicked()) {
			engine.setMode(engine.mainMenu);
		}
	}

	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, engine.windowWidth, engine.windowHeight,
				null);
		g.setColor(engine.transGray);
		g.fillRect(0, engine.windowHeight - 250, engine.windowWidth,
				engine.windowHeight);
		g.setColor(Color.cyan);
		buttons.get(0).draw(g);
		g.setFont(engine.large);
		buttons.get(0).set(200, engine.windowHeight-100);
		g.drawString("Your score is " + engine.score + ".", engine.windowWidth/2-100,
				engine.windowHeight - 150);
	}
	
	public String toString()
	{
		return "EndGame";
	}
}
