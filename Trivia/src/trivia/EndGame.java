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

	@Override
	public void run(int ms) {
		if (buttons.get(0).isClicked()) {
			engine.setMode(engine.mainMenu);
		}
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, engine.windowWidth, engine.windowHeight,
				null);
		g.setColor(engine.transGray);
		g.fillRect(0, engine.windowHeight - 250, engine.windowWidth,
				engine.windowHeight);
		g.setColor(Color.cyan);
		buttons.get(0).set((engine.windowWidth/2)-(buttons.get(0).width/2), engine.windowHeight - 100);
		buttons.get(0).draw(g);
		g.setFont(engine.large);
		g.drawString("Your score is " + engine.score + ".",
				engine.windowWidth / 2 - 100, engine.windowHeight - 150);
	}

	@Override
	public String toString() {
		return "EndGame";
	}
}
