/**
 * 
 */
package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * @author Cody Swendrowski, Dan Miller
 * 
 */
public class MainGame extends GameMode {

	private enum states {
		QUESTIONS, DISPLAY_RESPONSE, END_GAME
	};

	private states state;

	private Questions qstSet;

	private int score;
	private boolean lastAnswer = false;
	private long lastTime = 0;

	private Font f = new Font("Serif", Font.BOLD, 23);

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
						buttons.add(new Button(ans[i], 10, (engine.windowHeight-150) + (i * 35)));
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
				if (score < 0)
				{
					score = 0;
				}
			}
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
			
			//Draws question background
			g.setColor(Color.gray);
			g.fillRect(0, engine.windowHeight-250, engine.windowWidth, engine.windowHeight);
			g.setColor(Color.cyan);
			
			//Draws question
			FontMetrics fm   = g.getFontMetrics(f);
			Rectangle2D rect = fm.getStringBounds(qstSet.getQuestion(), g);
			int questionWidth = (int) rect.getWidth()+60;
			
			if (questionWidth < engine.windowWidth)
			{
				g.drawString(qstSet.getQuestion(), 60, engine.windowHeight-190);
			}
			else //If question goes outside the screen, split it
			{
				String question = qstSet.getQuestion();
				StringBuilder part1 = new StringBuilder();
				StringBuilder part2 = new StringBuilder();
				Scanner splitter = new Scanner(question);
				while (splitter.hasNext())
				{
					rect = fm.getStringBounds(part1.toString(), g);
					if (rect.getWidth()+60 < engine.windowWidth -50)
					{
						part1.append(" " + splitter.next());
					}
					else
					{
						part2.append(" " + splitter.next());
					}
				}
				splitter.close();
				g.drawString(part1.toString(), 60, engine.windowHeight-190);
				g.drawString(part2.toString(), 60, engine.windowHeight-165);
			}

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
			g.drawString("Your score is " + score + ".", 40, engine.windowHeight-225);
			break;

		case DISPLAY_RESPONSE:
			g.setColor(Color.gray);
			g.fillRect(0, engine.windowHeight-250, engine.windowWidth, engine.windowHeight);
			g.setColor(Color.cyan);
			if (lastAnswer) {
				g.drawString("You got that right!", 10, engine.windowHeight-230);
			} else {
				g.drawString("You got that wrong!", 10, engine.windowHeight-230);
			}
		}

		g.setColor(temp);
		g.setFont(tempF);

	}

	public String toString() {
		return "Main Game";
	}
}
