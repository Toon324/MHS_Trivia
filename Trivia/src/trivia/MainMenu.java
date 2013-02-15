package trivia;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows player to select which categories to pull questions from. Also allows
 * player to play game, or view the instructions.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class MainMenu extends GameMode {

	/**
	 * Creates a new MainMenu controller.
	 * 
	 * @param eng
	 *            GameEngine to utilize
	 */
	public MainMenu(GameEngine eng) {
		super(eng);
		buttons.add(new Button("Start Questioning", 50, 500));
		buttons.add(new ToggleButton("National Officers", 150, 200));
		buttons.add(new ToggleButton("National Partners", 150, 250));
		buttons.add(new ToggleButton("NLC Dates and Locations", 150, 300));
		buttons.add(new ToggleButton("Event Guidelines", 150, 350));
		buttons.add(new ToggleButton("Parlimentary Procedure", 150, 400));
		buttons.add(new Button("INSTRUCTIONS", 500, 500));
	}

	@Override
	public void run(int ms) {
		List<Button> Categories = buttons.subList(1, buttons.size() - 1);

		// If one of the category buttons is clicked, enable the start game
		// button
		buttons.get(0).setEnabled(Button.isOneClicked(Categories));

		// If start game button is clicked, pull which categories to load from
		// buttons
		if (buttons.get(0).isClicked()) {
			ArrayList<String> cats = new ArrayList<String>();

			for (Button but : Categories) {
				if (but.isClicked())
					cats.add(but.getText());
			}

			// Sets mode to MainGame
			engine.mainGame = new MainGame(engine);
			engine.mainGame.setCategories(cats.toArray(new String[0]));
			engine.setMode(engine.mainGame);
		}

		// If instructions button is clicked, go to Instructions
		if (buttons.get(buttons.size() - 1).isClicked()) {
			engine.setMode(engine.instructions);
		}
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, engine.windowWidth, engine.windowWidth,
				null);
		g.setColor(Color.cyan);
		g.setFont(engine.large);
		g.drawString("CATEGORIES", 140, 150);
		super.paint(g);
	}

	@Override
	public String toString() {
		return "Main Menu";
	}

}
