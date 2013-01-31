package trivia;

import java.awt.Color;
import java.awt.Graphics;

public class EndGame extends GameMode {

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
		g.setColor(Color.gray);
		g.fillRect(0, engine.windowHeight - 250, engine.windowWidth,
				engine.windowHeight);
		g.setColor(Color.cyan);
		buttons.get(0).draw(g);
		g.drawString("Your score is " + engine.score + ".", 40,
				engine.windowHeight - 225);
	}
}
