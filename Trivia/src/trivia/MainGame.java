/**
 * 
 */
package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author dsmiller95
 * 
 */
public class MainGame extends GameMode {

	private enum states {
		QUESTIONS, DISPLAY_RESPONSE, END_GAME
	};

	private states state;

	private String[] categories = {"Cat1"};
	
	private String[] questions;
	private String[][] answers;
	private int[] ansKey;

	private int currentQuestion = 0;
	private int score = 0;
	private boolean lastAnswer = false;
	private long lastTime = 0;

	private Font f = new Font("Serif", Font.BOLD, 20);

	public MainGame(GameEngine eng, String[] cats) {
		super(eng);
		state = states.QUESTIONS;
		categories = cats;
		proccessQuestions(readFile(cats));
	}

	@Override
	public void run() {
		switch (state) {
		case QUESTIONS:
			runQuestions();
			break;
		case DISPLAY_RESPONSE:
			if (System.currentTimeMillis() > lastTime + 1000)
				nextQuestion();
			break;
		case END_GAME:
			break;
		}
	}

	private void runQuestions() {
		if (buttons == null) {// if this is the first time, set up the buttons
			currentQuestion -= 1;
			nextQuestion();
		}

		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isClicked()) {
				// if the text of the current button is the answer
				if (i == ansKey[currentQuestion]) {
					score += 1;
					lastAnswer = true;
				} else {
					score -= 1;
					lastAnswer = false;
				}
				lastTime = System.currentTimeMillis();
				state = states.DISPLAY_RESPONSE;
			}
		}

	}
	
	public void setCategories(String[] cats){
		categories = cats;
	}
	
	private void nextQuestion() {
		engine.log("Asking next question");
		currentQuestion += 1;
		if (currentQuestion >= answers.length) {
			state = states.END_GAME;
			return;
		}
		// makes sure that the mode is correct, for when this gets called from a
		// different mode
		state = states.QUESTIONS;
		buttons = new Button[answers[currentQuestion].length];
		for (int i = 0; i < answers[currentQuestion].length; i++) {
			buttons[i] = new Button(answers[currentQuestion][i], 10,
					70 + (i * 35));
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
			g.drawString(questions[currentQuestion], 10, 50);

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
			g.drawString("Your score is " + score + ".", 10, 30);
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

	/**
	 * Reads in a file to an array
	 * 
	 * @return ArrayList<String> arrayList containing the contents of the file,
	 *         separated by line
	 */
	private ArrayList<String> readFile(String[] fileNames) {
		engine.log("loading the questions");

		ArrayList<String> input = new ArrayList<String>();
		
		for(int i = 0; i < fileNames.length; i++){
			engine.log("Loading " + fileNames[i]);
			Scanner scanner = new Scanner(
					Trivia.class.getResourceAsStream("\\Resources\\question_Sets\\" + fileNames[i] + ".txt"));
			scanner.useDelimiter("\n");
			try {
				while (scanner.hasNext()) {
					String s = scanner.next();
					input.add(s);
				}
			} catch (Exception e) {
				engine.log(e.toString());
			} finally {
				scanner.close();
			}
		}
		return input;
	}
	
	
	/**
	 * Takes the read in file, randomizes the questions and answers, and puts the results into global variables questions, answers, and the answer key into ansKey
	 * Preconditions:
	 * 		The text file input has questions seperated by lines, and on each line the items are seperated by "|", with the first item being the question, the second the correct answer, and all the rest incorrect answers
	 * @param input
	 */
	private void proccessQuestions(ArrayList<String> input) {
		
		ArrayList<String> questList = new ArrayList<String>();
		ArrayList<ArrayList<String>> ansList = new ArrayList<ArrayList<String>>();
		ArrayList<Integer> ansKeyList = new ArrayList<Integer>();
		
		//Variables for use inside the loops
		String line, item;
		int lineCount = 0;
		int itemCnt = 0;
		Iterator<String> iter = input.iterator();
		
		while (iter.hasNext()) {
			line = iter.next();
			
			//Scanner used to separate out each line based on the tab character
			Scanner scan = new Scanner(line);
			scan.useDelimiter("\\t");
			itemCnt = 0;
			
			while (scan.hasNext()) {
				item = scan.next();
				
				if (itemCnt > 1) {
					ansList.get(lineCount).add(item);
				} else if (itemCnt == 1){
					ansKeyList.add(Integer.parseInt(item) - 1);
					itemCnt++;
				} else if (itemCnt == 0){
					questList.add(item);
					ansList.add(new ArrayList<String>());
					itemCnt++;
				}
			}
			scan.close();
			lineCount++;
		}
		
		
		questions = new String[questList.size()];
		ansKey = new int[questList.size()];
		
		answers = new String[questList.size()][];
		
		//sort arrays are used to determine where each value should be pulled from either questList or ansList and put into the result array
		int[] sort1 = genUniqueRandArray(questList.size());
		int[] sort2;
		for(int i = 0; i < sort1.length; i++){
			questions[i] = questList.get(sort1[i]);
			
			answers[i] = new String[ansList.get(sort1[i]).size()];
			sort2 = genUniqueRandArray(answers[i].length);
			
			for(int j = 0; j < answers[i].length; j++){
				answers[i][j] = ansList.get(sort1[i]).get(sort2[j]);
				if(sort2[j] == ansKeyList.get(sort1[i])){
					//if this is pulling the same answer as determined in the list, set the answer key to this index as the correct answer
					ansKey[i] = j;
				}
			}
		}
		
	}

	/**
	 * Utility used to generate an array of a certain length which contains numbers from 0 to length - 1, with each number occuring only once
	 * @param len: length of the result array
	 * @return Array of random numbers ranging from 0 to len - 1, with no number occuring twice
	 */
	private int[] genUniqueRandArray(int len) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		int addNumber;

		for (int i = 0; i < len; i++) {
			addNumber = (int) (Math.random() * len);
			if (list.contains(addNumber)) {
				i--;
			} else {
				list.add(addNumber);
			}
		}
		
		Iterator<Integer> iter = list.iterator();
		int[] result = new int[len];
		for(int i = 0; i< result.length; i++){
			result[i] = iter.next().intValue();
		}
		return result;
	}
	

	public String toString() {
		return "Main Game";
	}
}
