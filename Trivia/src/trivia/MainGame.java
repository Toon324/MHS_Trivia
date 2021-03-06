package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Pulls questions from text files based on which categories are selected.
 * Displays those questions and answers. Right answers are give points based on
 * how fast they are answered. Wrong answers lose points.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class MainGame extends GameMode {

	private Point[] fleetPositions;
	private boolean[] triangleFleetStatus, squareFleetStatus;

	private enum states {
		QUESTIONS, DISPLAY_RESPONSE, WAIT_FOR_END
	};

	private states state;
	private Questions qstSet;
	private int maxFleetSize, scoreCalc;
	private boolean lastAnswer = false;
	private long lastTime = 0;
	private Font f = new Font("Serif", Font.BOLD, 26);

	/**
	 * Creates a new MainGame controller.
	 * 
	 * @param eng
	 *            Engine to utilize and report to
	 */
	public MainGame(GameEngine eng) {
		super(eng);
		state = states.DISPLAY_RESPONSE;// will automatically time out to next
										// phase
		initializeFleetPositions(1);
	}

	/**
	 * Helper method. Sets up Fleet positions. Offers differing options for ease
	 * of use and future expandability.
	 * 
	 * @param setup
	 *            Represents which setup to load.
	 */
	private void initializeFleetPositions(int setup) {
		switch (setup) {
		case 0:
			maxFleetSize = 4;
			Point a1 = new Point(200, 50),
			a2 = new Point(230, 120),
			a3 = new Point(230, 190),
			a4 = new Point(200, 260);
			Point[] atemp = { a1, a2, a3, a4 };
			boolean[] astatusTemp = { false, false, false, false };
			fleetPositions = atemp;
			triangleFleetStatus = astatusTemp.clone();
			squareFleetStatus = astatusTemp.clone();
			addShipsToTriangleFleet(maxFleetSize);
			addShipsToSquareFleet(maxFleetSize);
			break;
		case 1:
			maxFleetSize = 8;
			Point b1 = new Point(200, 50),
			b2 = new Point(230, 120),
			b3 = new Point(230, 190),
			b4 = new Point(200, 260),
			b5 = new Point(130, 50),
			b6 = new Point(130, 120),
			b7 = new Point(130, 190),
			b8 = new Point(130, 260);
			Point[] btemp = { b1, b2, b3, b4, b5, b6, b7, b8 };
			boolean[] bstatusTemp = { false, false, false, false, false, false,
					false, false };
			fleetPositions = btemp;
			triangleFleetStatus = bstatusTemp.clone();
			squareFleetStatus = bstatusTemp.clone();
			addShipsToTriangleFleet(maxFleetSize);
			addShipsToSquareFleet(maxFleetSize);
			break;
		}
	}

	/**
	 * Loads questions based on which categories are set to be true.
	 * 
	 * @param cats
	 *            Categories to load.
	 */
	public void setCategories(String[] cats) {
		qstSet = new Questions("Resources\\question_Sets\\", cats);
	}

	@Override
	public void run(int ms) {
		// intentionally modifies the object, rather than re-instantianizing
		GameEngine.envSize.x = engine.windowWidth;
		GameEngine.envSize.y = engine.windowHeight - 250;

		engine.actors.handleActors(ms);
		// Particle.runParticles(ms);

		switch (state) {
		case QUESTIONS:
			runQuestion();
			break;
		case DISPLAY_RESPONSE:
			if (System.currentTimeMillis() > lastTime + 1000 && engine.ENTER) {
				state = states.QUESTIONS;

				if (qstSet.nextQuestion()) {
					buttons = new ArrayList<Button>();
					String[] ans = qstSet.getAnsArray();
					for (int i = 0; i < ans.length; i++) {
						buttons.add(new Button(ans[i], 20,
								(engine.windowHeight - 165) + (i * 40)));
					}
				} else {
					state = states.WAIT_FOR_END;
				}
			}
			break;
		case WAIT_FOR_END:
			if (System.currentTimeMillis() > lastTime + 3000)
				engine.setMode(engine.endGame);
			break;
		}
	}

	/**
	 * Determines if answer is clicked, and what score to give.
	 */
	private void runQuestion() {
		if (buttons == null) {// if this is the first time, set up the buttons
			if (!qstSet.nextQuestion())
				engine.setMode(engine.endGame);
		}

		// If a button is clicked, check to see if it is the right answer
		if (Button.isOneClicked(buttons)) {
			engine.actors.fire();
			lastTime = System.currentTimeMillis();
			state = states.DISPLAY_RESPONSE;
			lastAnswer = qstSet.checkCorrect(buttons.toArray(new Button[0]));
			// If the answer is right, add points and ships, as well as set
			// evade
			if (lastAnswer) {
				int toAdd = (int) (100 / Math.pow(2,
						Math.pow(qstSet.getTimePassed() / (double) 5000, 4)));
				engine.score += toAdd;
				if (toAdd < 50)
					toAdd = 50;
				engine.actors.setEvade(toAdd);
				addShipsToTriangleFleet(1);
				addShipsToSquareFleet(1);
				engine.ENTER = true;
			}
			// If the answer is wrong, only add ships to enemy fleet, set evade
			// to minimum (50), and display correct answer until Enter is
			// pressed
			else {
				addShipsToSquareFleet(1);
				engine.actors.setEvade(50);
				engine.ENTER = false;
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		// Draws background image
		g.drawImage(background, 0, 0, engine.windowWidth, engine.windowHeight,
				null);

		Color temp = g.getColor();
		Font tempF = g.getFont();

		engine.actors.drawActors(g);

		g.setFont(f);
		g.setColor(Color.cyan);

		switch (state) {
		case QUESTIONS:
			// Draws question background
			g.setColor(engine.transGray);
			g.fillRect(0, engine.windowHeight - 250, engine.windowWidth,
					engine.windowHeight);

			g.setColor(Color.cyan);

			// Draws question
			FontMetrics fm = g.getFontMetrics(f);
			Rectangle2D rect = fm.getStringBounds(qstSet.getQuestion(), g);
			int questionWidth = (int) rect.getWidth() + 60;

			if (questionWidth < engine.windowWidth) {
				g.drawString(qstSet.getQuestion(), 60,
						engine.windowHeight - 210);
			} else // If question goes outside the screen, split it
			{
				String question = qstSet.getQuestion();
				StringBuilder part1 = new StringBuilder();
				StringBuilder part2 = new StringBuilder();
				Scanner splitter = new Scanner(question);
				while (splitter.hasNext()) {
					rect = fm.getStringBounds(part1.toString(), g);
					if (rect.getWidth() + 60 < engine.windowWidth - 50) {
						part1.append(" " + splitter.next());
					} else {
						part2.append(" " + splitter.next());
					}
				}
				splitter.close();
				g.drawString(part1.toString(), 60, engine.windowHeight - 210);
				g.drawString(part2.toString(), 60, engine.windowHeight - 185);
			}

			try {
				for (Button but : buttons) {
					but.draw(g);
				}
			} catch (java.lang.NullPointerException e) {
				GameEngine.log("No buttons in MainGame!");
			}
			break;

		default: // while waiting for the end, display last question's info
			g.setColor(engine.transGray);
			g.fillRect(0, engine.windowHeight - 250, engine.windowWidth,
					engine.windowHeight);
			g.setColor(Color.cyan);
			// If answer was correct, tell player it was
			if (lastAnswer) {
				g.drawString("You got that right!", 10,
						engine.windowHeight - 230);
			}
			// If answer was wrong, tell player the correct answer and wait for
			// them to hit Enter
			else {
				g.drawString("Nice try, but you got that wrong!", 10,
						engine.windowHeight - 220);
				g.drawString("The correct answer was:", 10,
						engine.windowHeight - 160);
				g.setColor(Color.red);
				try {
					g.drawString(qstSet.getCorrectString(), 10,
							engine.windowHeight - 130);
				} catch (Exception e) {
					engine.ENTER = true;
				}
				g.setColor(Color.cyan);
				g.drawString("Press ENTER to continue.", 10,
						engine.windowHeight - 70);
			}
		}

		if (state == states.QUESTIONS)
			scoreCalc = (int) (100 / Math.pow(2,
				Math.pow(qstSet.getTimePassed() / (double) 5000, 4))) + 5;

		// Draws score
		g.drawString("Score:", engine.windowWidth - 220,
				engine.windowHeight - 140);
		g.fillRect(engine.windowWidth - 220, engine.windowHeight - scoreCalc
				- 10, 65, scoreCalc);

		// Draws evade chance
		g.drawString("Evade %:", engine.windowWidth - 120,
				engine.windowHeight - 140);
		g.setFont(engine.small);
		g.setColor(Color.green);
		g.drawString("100%", engine.windowWidth-30, engine.windowHeight-110);
		g.setColor(Color.orange);
		g.drawString("75%", engine.windowWidth-30, engine.windowHeight-80);
		g.setColor(Color.red);
		g.drawString("50%", engine.windowWidth-30, engine.windowHeight-55);
		
		int evadeCalc = scoreCalc;
		
		if (scoreCalc < 50)
			evadeCalc = 50;

		// Draws bar with color based on chance
		if (evadeCalc > 75)
			g.setColor(Color.green);
		else if (evadeCalc > 50)
			g.setColor(Color.orange);
		else
			g.setColor(Color.red);
		
		g.fillRect(engine.windowWidth - 100, engine.windowHeight - 10
				- evadeCalc, 65, evadeCalc);

		g.setColor(temp);
		g.setFont(tempF);

	}

	/**
	 * Called when a Triangle dies to tell the corresponding fleet position that
	 * the position can be filled in with a new Triangle.
	 * 
	 * @param x
	 *            X position of Triangle
	 * @param y
	 *            Y position of Triangle
	 */
	public void setTrianglePositionToFalse(int x, int y) {
		int num = 0;
		for (Point p : fleetPositions) {
			if (p.x == x && p.y == y)
				triangleFleetStatus[num] = false;
			num++;
		}
	}

	/**
	 * Called when a Square dies to tell the corresponding fleet position that
	 * the position can be filled in with a new Square.
	 * 
	 * @param x
	 *            X position of Square
	 * @param y
	 *            Y position of Square
	 */
	public void setSquarePositionToFalse(int x, int y) {
		int num = 0;
		for (Point p : fleetPositions) {
			if (engine.windowWidth - p.x == x && p.y == y)
				squareFleetStatus[num] = false;
			num++;
		}
	}

	/**
	 * Helper method. Adds ships to the Triangle fleet, and updates
	 * TriangleFleetStatus.
	 * 
	 * @param shipsToAdd
	 *            Number of Triangles to add
	 */
	private void addShipsToTriangleFleet(int shipsToAdd) {
		for (int i = 0; i < fleetPositions.length; i++) {
			if (shipsToAdd <= 0)
				break;
			if (!triangleFleetStatus[i]) {
				engine.actors.addTriangle(fleetPositions[i].x,
						fleetPositions[i].x - 250, fleetPositions[i].y);
				triangleFleetStatus[i] = true;
				shipsToAdd -= 1;
			}
		}
	}

	/**
	 * Helper method. Adds ships to the Square fleet, and updates
	 * SquareFleetStatus
	 * 
	 * @param shipsToAdd
	 *            Number of Squares to add
	 */
	private void addShipsToSquareFleet(int shipsToAdd) {
		if (shipsToAdd > 0) {
			for (int i = 0; i < fleetPositions.length; i++) {
				if (shipsToAdd <= 0)
					break;
				if (!squareFleetStatus[i]) {
					engine.actors.addSquare(engine.windowWidth
							- fleetPositions[i].x, engine.windowWidth
							- fleetPositions[i].x + 250, fleetPositions[i].y);
					squareFleetStatus[i] = true;
					shipsToAdd -= 1;
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Main Game";
	}
}
