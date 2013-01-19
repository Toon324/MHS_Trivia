/**
 * 
 */
package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Cody Swendrowski, Dan Miller
 * 
 */
public class MainGame extends GameMode {

	private final int WRONG_ANSWER = 5, MAX_POINTS_POSSIBLE = 10;

	private enum states {
		QUESTIONS, DISPLAY_RESPONSE, END_GAME
	};

	private states state;

	private Questions qstSet;

	private int score;
	private boolean lastAnswer = false;
	private long lastTime = 0;

	private Font f = new Font("Serif", Font.BOLD, 20);

	public MainGame(GameEngine eng) {
		super(eng);
		state = states.DISPLAY_RESPONSE;// will automatically time out to next
	}

	public void setCategories(String[] cats) {
		qstSet = new Questions("\\Resources\\question_Sets\\", cats);
	}

	@Override
	public void run() {
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
						buttons.add(new Button(ans[i], 10, 70 + (i * 35)));
					}
				} else {
					state = states.END_GAME;
				}
			}
			break;

		case END_GAME:
			break;
		}
	}

	private void runQuestion() {
		if (buttons == null) {// if this is the first time, set up the buttons
			if (!qstSet.nextQuestion())
				state = states.END_GAME;
		}
		if (Button.isOneClicked(buttons)) {
			lastTime = System.currentTimeMillis();
			state = states.DISPLAY_RESPONSE;
			lastAnswer = qstSet.checkCorrect(buttons.toArray(new Button[0]));
			if (lastAnswer) {
				score += 100 / Math.pow(2,
						Math.pow(qstSet.getTimePassed() / (double) 5000, 4));
			} else {
				score -= 90;
			}
		}
	}

	private void updateButtonPositions() {
		int totalWidth = 0;
		for (Button but : buttons) {
			totalWidth += but.getWidth();
		}
		Button lastButton = new Button("", -(engine.windowWidth - totalWidth) / 4, 0);
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).set(
					((engine.windowWidth - totalWidth) / 4) + lastButton.getX()
							+ lastButton.getWidth(), buttons.get(i).getY());
			lastButton = buttons.get(i);
		}
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, engine.windowWidth, engine.windowHeight, null);
		
		Color temp = g.getColor();
		Font tempF = g.getFont();

		g.setFont(f);
		g.setColor(Color.cyan);

		switch (state) {
		case QUESTIONS:
			
			updateButtonPositions();
			
			//Draws question background
			g.setColor(Color.gray);
			g.fillRect(0, engine.windowHeight-250, engine.windowWidth, engine.windowHeight);
			g.setColor(Color.cyan);
			
			//Draws question
			g.drawString(qstSet.getQuestion(), 60, engine.windowHeight-180);

			try {
				for (Button but:buttons) {
					but.draw(g);
				}
			} catch (java.lang.NullPointerException e) {
				engine.log("No buttons in MainGame!");
			}
			// intentionally left out break; room is left for the score to be
			// printed out after the previous prints
		case END_GAME:
			g.drawString("Your score is " + score + ".", 60, engine.windowHeight-230);
			break;

		case DISPLAY_RESPONSE:
			if (lastAnswer) {
				g.drawString("You got that right!", 10, 30);
			} else {
				g.drawString("You got that wrong!", 10, 30);
			}
		}

		g.setColor(temp);
		g.setFont(tempF);

	}

	public String toString() {
		return "Main Game";
	}
}
