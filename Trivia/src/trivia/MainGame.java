package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Scanner;

import aiControls.TriangleFleet;

/**
 * Pulls questions from text files based on which categories are selected.
 * Displays those questions and answers. Right answers are give points based on
 * how fast they are answered. Wrong answers lose points.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class MainGame extends GameMode {

	private enum states {
		QUESTIONS, DISPLAY_RESPONSE
	};

	private states state;
	private ArrayList<TriangleFleet> fleets;
	
	
	private Questions qstSet;

	private int maxFleetSize;
	private int fleetSize;
	
	private boolean lastAnswer = false;
	private long lastTime = 0;

	private Font f = new Font("Serif", Font.BOLD, 23);

	/**
	 * Creates a new MainGame controller.
	 * 
	 * @param eng
	 *            Engine to utilize and report to.
	 */
	public MainGame(GameEngine eng) {
		super(eng);
		fleets = new ArrayList<TriangleFleet>();
		TriangleFleet fleet = new TriangleFleet();
		fleets.add(fleet);
		state = states.DISPLAY_RESPONSE;// will automatically time out to next
		maxFleetSize = 10;
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
		//intentionally modifies the object, rather than re-instantianizing
		GameEngine.envSize.x = engine.windowWidth;
		GameEngine.envSize.y = engine.windowHeight - 250;

		if(fleetSize < maxFleetSize){
			//addShips(1);
			fleetSize++;
		}
		
		for(TriangleFleet tf : fleets){
			tf.nextStep(ms);
		}
		
		engine.actors.handleActors(ms);
		//Particle.runParticles(ms);
		switch (state) {
		case QUESTIONS:
			runQuestion();
			break;
		case DISPLAY_RESPONSE:
			if (System.currentTimeMillis() > lastTime + 1000) {
				state = states.QUESTIONS;

				if (qstSet.nextQuestion()) {
					buttons = new ArrayList<Button>();
					String[] ans = qstSet.getAnsArray();
					for (int i = 0; i < ans.length; i++) {
						buttons.add(new Button(ans[i], 20,
								(engine.windowHeight - 150) + (i * 35)));
					}
				} else {
					engine.setMode(engine.endGame);
				}
			}
			break;
		}
	}

	@Override
	public void mouseMoved(int x, int y) {
	}

	/**
	 * Determines if answer is clicked, and what score to give.
	 */
	private void runQuestion() {
		if (buttons == null) {// if this is the first time, set up the buttons
			if (!qstSet.nextQuestion())
				engine.setMode(engine.endGame);
		}

		if (Button.isOneClicked(buttons)) {
			lastTime = System.currentTimeMillis();
			state = states.DISPLAY_RESPONSE;
			lastAnswer = qstSet.checkCorrect(buttons.toArray(new Button[0]));
			if (lastAnswer) {
				engine.score += 100 / Math.pow(2,
						Math.pow(qstSet.getTimePassed() / (double) 5000, 4));
				addShips(1);
				Triangle.MAX_ACCEL *= 2;
			} else {
				engine.score -= 90;
				Triangle.MAX_ACCEL /= 2;
				if (engine.score < 0) {
					engine.score = 0;
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, engine.windowWidth, engine.windowHeight,
				null);

		Color temp = g.getColor();
		Font tempF = g.getFont();

		engine.actors.drawActors(g);
		//Particle.drawParticles(g);

		g.setFont(f);
		g.setColor(Color.cyan);

		switch (state) {
		case QUESTIONS:

			// Draws question background
			g.setColor(Color.gray);
			g.fillRect(0, engine.windowHeight - 250, engine.windowWidth,
					engine.windowHeight);
			g.setColor(Color.cyan);

			// Draws question
			FontMetrics fm = g.getFontMetrics(f);
			Rectangle2D rect = fm.getStringBounds(qstSet.getQuestion(), g);
			int questionWidth = (int) rect.getWidth() + 60;

			if (questionWidth < engine.windowWidth) {
				g.drawString(qstSet.getQuestion(), 60,
						engine.windowHeight - 190);
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
				g.drawString(part1.toString(), 60, engine.windowHeight - 190);
				g.drawString(part2.toString(), 60, engine.windowHeight - 165);
			}

			try {
				for (Button but : buttons) {
					but.draw(g);
				}
			} catch (java.lang.NullPointerException e) {
				GameEngine.log("No buttons in MainGame!");
			}
			break;

		case DISPLAY_RESPONSE:
			g.setColor(Color.gray);
			g.fillRect(0, engine.windowHeight - 250, engine.windowWidth,
					engine.windowHeight);
			g.setColor(Color.cyan);
			if (lastAnswer) {
				g.drawString("You got that right!", 10,
						engine.windowHeight - 230);
			} else {
				g.drawString("You got that wrong!", 10,
						engine.windowHeight - 230);
			}
		}

		g.setColor(temp);
		g.setFont(tempF);

	}

	private void addShips(int shipsToAdd) {
		Actor t, s;
		for (; shipsToAdd > 0; shipsToAdd--) {
			engine.actors.addTriangle((int) (Math.random() * GameEngine.envSize.x), (int) (Math.random() * GameEngine.envSize.y));
			engine.actors.addSquare((int)(Math.random() * GameEngine.envSize.x),(int) (Math.random() * GameEngine.envSize.y));
		}
	}

	public String toString() {
		return "Main Game";
	}
}
