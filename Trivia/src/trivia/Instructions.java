package trivia;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.Scanner;

/**
 * Displays the instructions for play and offers a button to go to MainMenu.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class Instructions extends GameMode {

	private String[] instructions = {
			"Click the category buttons to choose which questions to be tested on.",
			"Click on the button containing your answer of choice.",
			"The longer you take to answer, the less points you get for being right,",
			"And your ships will have a decreased chance of evading enemy shots!" };

	/**
	 * Creates a new Instructions controller.
	 * 
	 * @param eng
	 *            GameEngine to utilize
	 */
	public Instructions(GameEngine eng) {
		super(eng);
		buttons.add(new Button("Main Menu", 250, 500));
		engine.playSound("Eternity.wav", true);
	}

	@Override
	public void run(int ms) {
		if (buttons.get(0).isClicked()) {
			engine.setMode(engine.mainMenu);
		}
	}

	@Override
	public void paint(Graphics g) {
		// Draws background image
		g.drawImage(background, 0, 0, engine.windowWidth, engine.windowWidth,
				null);

		// Draws transparent gray background for text
		g.setColor(engine.transGray);
		g.fillRect(0, 50, engine.windowWidth, engine.windowHeight - 130);

		// Paints buttons
		super.paint(g);

		g.setColor(Color.cyan);
		g.setFont(engine.large);

		// Paints all of the instructions
		for (int i = 0; i < instructions.length; i++) {
			String[] instruction = sizeString(instructions[i], g);
			for (int x = 0; x < instruction.length; x++) {
				g.drawString(instruction[x], 10, (i + 1) * 100 + (x * 30));
			}
		}
	}

	/**
	 * Helper method. Calculates the width of the string, then breaks it up into
	 * two parts if string exceeds window width.
	 * 
	 * @param s
	 *            String to size
	 * @param g
	 *            Graphics object to use for size calculations (font affects
	 *            size)
	 * @return An array of Strings with up to two String elements
	 */
	private String[] sizeString(String s, Graphics g) {
		FontMetrics fm = g.getFontMetrics(engine.large);
		Rectangle2D rect = fm.getStringBounds(s, g);
		int stringWidth = (int) rect.getWidth() + 60;

		if (stringWidth < engine.windowWidth) {
			String[] toReturn = { s };
			return toReturn;
		} else // If question goes outside the screen, split it
		{
			StringBuilder part1 = new StringBuilder();
			StringBuilder part2 = new StringBuilder();
			Scanner splitter = new Scanner(s);
			while (splitter.hasNext()) {
				rect = fm.getStringBounds(part1.toString(), g);
				if (rect.getWidth() + 60 < engine.windowWidth - 50) {
					part1.append(" " + splitter.next());
				} else {
					part2.append(" " + splitter.next());
				}
			}
			splitter.close();
			String[] toReturn = { part1.toString(), part2.toString() };
			return toReturn;
		}
	}

}
