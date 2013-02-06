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

		// engine.playSound("Eternity.wav", true);
	}

	@Override
	public void run(int ms) {
		List<Button> Categories = buttons.subList(1, buttons.size() - 1);

		buttons.get(0).setEnabled(Button.isOneClicked(Categories));

		if (buttons.get(0).isClicked()) {
			ArrayList<String> cats = new ArrayList<String>();
			// assumes there is only one button that is not a toggleButton

			for (Button but : Categories) {
				if (but.isClicked())
					cats.add(but.getText());
			}

			engine.mainGame = new MainGame(engine);
			engine.mainGame.setCategories(cats.toArray(new String[0]));
			engine.setMode(engine.mainGame);
		}
		if (buttons.get(buttons.size() - 1).isClicked()) {
			engine.setMode(engine.instructions);
		}
	}

	public void paint(Graphics g) {
		// int w = engine.windowWidth, h = engine.windowHeight;
		// Button[] buts = buttons.toArray(new Button[0]);

		/*
		 * if(w >= 800 && h >= 600){ buts[0].set(w/2 - buts[0].width/2 , h/2 -
		 * h/6 ); buts[1].set(w/2 - buts[1].width/2 - w/16, h/2 - h/12);
		 * buts[2].set(w/2 - buts[2].width/2 + w/16, h/2 - h/12);
		 * buts[3].set(w/2 - buts[3].width/2 - w/16, h/2 + h/12);
		 * buts[4].set(w/2 - buts[4].width/2 + w/16, h/2 + h/12);
		 * buts[5].set(w/2 - buts[5].width/2 , h/2 ); }else{ buts[0].set(w/2 -
		 * buts[0].width/2 , h/2 - 100); buts[1].set(w/2 - buts[1].width/2 - 50,
		 * h/2 - 50 ); buts[2].set(w/2 - buts[2].width/2 + 50, h/2 - 50 );
		 * buts[3].set(w/2 - buts[3].width/2 - 50, h/2 + 50 ); buts[4].set(w/2 -
		 * buts[4].width/2 + 50, h/2 + 50 ); buts[5].set(w/2 - buts[5].width/2 ,
		 * h/2 ); }
		 */
		g.drawImage(background, 0, 0, engine.windowWidth, engine.windowWidth,
				null);
		g.setColor(Color.cyan);
		g.setFont(engine.large);
		g.drawString("CATEGORIES", 140, 150);
		super.paint(g);
	}

	public String toString() {
		return "Main Menu";
	}

}
