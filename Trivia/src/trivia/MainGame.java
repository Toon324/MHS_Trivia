/**
 * 
 */
package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * @author dsmiller95
 *
 */
public class MainGame extends GameMode {
	
	private enum states {QUESTIONS, DISPLAY_RESPONSE, END_GAME};
	private states state;
	
	
	private String[] questions = {"What is 1 + 2?", "What is 4 - 9?", "What describes your mom?"};
	private String[][] 	answers = {
			{"3", "-2", "4", "2"},
			{"-5", "3", "-9", "-4"},
			{"fat", "beautiful", "pretty", "glorious"}
	};
	private int currentQuestion = 0;
	private int score = 0;
	private boolean lastAnswer = false;
	private long lastTime = 0;

	private Font f = new Font ("Serif", Font.BOLD, 20);
	
	
	public MainGame(GameEngine eng){
		super(eng);
		state = states.QUESTIONS;
	}
	
	
	@Override
	public void run() {
		switch(state){
		case QUESTIONS:
			if(buttons == null){//if this is the first time, set up the buttons
				currentQuestion -= 1;
				nextQuestion();
			}
			
			for(int i = 0; i < buttons.length; i++){
				if(buttons[i].isClicked()){
					if( buttons[i].getText().equals(answers[currentQuestion][0]) ){//if the text of the current button is the answer
						score += 1;
						lastAnswer = true;
					}else{
						score -= 1;
						lastAnswer = false;
					}
					lastTime = System.currentTimeMillis();
					state = states.DISPLAY_RESPONSE;
				}
			}
			break;
		case DISPLAY_RESPONSE:
			if(System.currentTimeMillis() > lastTime + 1000)
				nextQuestion();
			break;
		case END_GAME:
			break;
		}
	}
	
	private void nextQuestion(){
		engine.log("Asking next question");
		currentQuestion += 1;
		if(currentQuestion >= answers.length){
			state = states.END_GAME;
			return;
		}
		//makes sure that the mode is correct, for when this gets called from a different mode
		state = states.QUESTIONS;
		buttons = new Button[4];
		for(int i = 0; i < answers[currentQuestion].length; i++){
			buttons[i] = new Button(answers[currentQuestion][i], 10, 70 + (i * 35));
		}
	}
	
	
	@Override
	public void paint(Graphics g) {
		Color temp = g.getColor();
		Font tempF = g.getFont();
		

		g.setFont(f);
		g.setColor(Color.BLUE);
		
		switch(state){
		case QUESTIONS:
			g.drawString(questions[currentQuestion], 10, 50);
			
			try{
				for(int i = 0; i < buttons.length; i++){
					buttons[i].draw(g);
				}
			}catch(java.lang.NullPointerException e){
				engine.log("No buttons in MainGame!");
			}
			//intentionally left out break; room is left for the score to be printed out after the previous prints
		case END_GAME:
			g.drawString("Your score is " + score + ".", 10, 30);
			break;
			
		case DISPLAY_RESPONSE:
			if(lastAnswer){
				g.drawString("You got that right!", 10, 30);
			}else{
				g.drawString("You got that wrong!", 10, 30);
			}
		}
		
		g.setColor(temp);
		g.setFont(tempF);
		
	}
	
	public String toString(){
		return "Main Game";
	}
}
