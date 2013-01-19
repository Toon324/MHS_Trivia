/**
 * 
 */
package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

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

	private int currentQuestion = 0;
	private int score = 0;
	private boolean lastAnswer = false;
	private long lastAnswered, questionStarted = 0;

	private Font f = new Font("Serif", Font.BOLD, 20);

	public MainGame(GameEngine eng) {
		super(eng);
		state = states.QUESTIONS;
	}
	
	public void setCategories(String[] cats){
		qstSet = new Questions("\\Resources\\question_Sets\\", cats);
	}
	
	@Override
	public void run() {
		switch (state) {
		case QUESTIONS:
			runQuestion();
			break;
		case DISPLAY_RESPONSE:
			if (System.currentTimeMillis() > lastAnswered + 1000)
				nextQuestion();
			break;
		case END_GAME:
			break;
		}
	}

	private void runQuestion() {
		if (buttons == null) {// if this is the first time, set up the buttons
			currentQuestion -= 1;
			nextQuestion();
		}

		for (int i = 0; i < buttons.length; i++) {
			updateButtonPositions();
			if (buttons[i].isClicked()) {
				// if the current button is the answer
				if (i == qstSet.answerKey[currentQuestion]) {
					score += MAX_POINTS_POSSIBLE - ((System.currentTimeMillis() - questionStarted) / 1000);
					lastAnswer = true;
				} else {
					score -= WRONG_ANSWER;
					if (score < 0)
					{
						score = 0;
					}
					lastAnswer = false;
				}
				lastAnswered = System.currentTimeMillis();
				state = states.DISPLAY_RESPONSE;
			}
		}

	}
	
	private void updateButtonPositions() {
		int totalWidth = 0;
		for (int i=0; i<buttons.length; i++)
		{
			totalWidth += buttons[i].getWidth();
		}
		Button lastButton = new Button("",-(engine.windowWidth-totalWidth)/4,0);
		for (int i=0; i<buttons.length; i++)
		{
			buttons[i].set(((engine.windowWidth-totalWidth)/4) + 
					lastButton.getX() + lastButton.getWidth(), buttons[i].getY());
			lastButton = buttons[i];
		}
	}

	private void nextQuestion() {
		//engine.log("Asking next question");
		currentQuestion += 1;
		if (currentQuestion >= qstSet.answers.length) {
			state = states.END_GAME;
			return;
		}
		// makes sure that the mode is correct, for when this gets called from a
		// different mode
		state = states.QUESTIONS;
		buttons = new Button[qstSet.answers[currentQuestion].length];
		for (int i = 0; i < qstSet.answers[currentQuestion].length; i++) {
			buttons[i] = new Button(qstSet.answers[currentQuestion][i], 0,
					(engine.windowHeight - 150));
		}
		questionStarted = System.currentTimeMillis();
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
			//Draws question background
			g.setColor(Color.gray);
			g.fillRect(0, engine.windowHeight-250, engine.windowWidth, engine.windowHeight);
			g.setColor(Color.cyan);
			
			//Draws amount of points remaining to be gained
			g.drawString("Points left: " + (MAX_POINTS_POSSIBLE - ((System.currentTimeMillis() - questionStarted) / 1000)),
					60, engine.windowHeight - 200);
			
			//Draws question
			g.drawString(qstSet.questions[currentQuestion], 60, engine.windowHeight-180);

			try {
				for (int i = 0; i < buttons.length; i++) {
					buttons[i].draw(g);
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
