package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Cody Swendrowski, Dan Miller
 * 
 */
public class MainGame extends GameMode {

	private enum states {
		QUESTIONS, DISPLAY_RESPONSE, END_GAME
	};

	private states state;

	private Question[] questions;
	//private int[] toAsk;

	private Question question;
	private int currentQuestion = 0;
	private int score = 0;
	private boolean lastAnswer = false;
	private long lastTime = 0;

	private Font f = new Font("Serif", Font.BOLD, 20);

	public MainGame(GameEngine eng) {
		super(eng);
		state = states.QUESTIONS;
		sortQuestions(readFile());
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
				if (i == question.getAnswer()-1) {
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

	private void nextQuestion() {
		engine.log("Asking next question");
		currentQuestion += 1;
		if (currentQuestion >= questions.length) {
			state = states.END_GAME;
			return;
		}
		question = questions[currentQuestion];
		// makes sure that the mode is correct, for when this gets called from a
		// different mode
		state = states.QUESTIONS;
		buttons = new Button[4];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Button(question.getPossibleAnswer(i), 10,70 + (i * 35));
		}
	}

	@Override
	public void paint(Graphics g) {
		Color temp = g.getColor();
		Font tempF = g.getFont();

		g.setFont(f);
		g.setColor(Color.BLUE);

		switch (state) {
		case QUESTIONS:
			g.drawString(questions[currentQuestion].getQuestion(), 10, 50);

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
	private ArrayList<Question> readFile() {
		engine.log("loading the questions");

		ArrayList<Question> input = new ArrayList<Question>();

		Scanner scanner = new Scanner(MainGame.class.getResourceAsStream("questions.txt"));
		
		Question temp;
		
		int cnt = 0;
		try {
			while (scanner.hasNextLine()) {
				temp = readQuestion(cnt);
				input.add(temp);
				engine.log("Added: " + temp.getQuestion());
				for (int x = 0; x < 4; x++)
				{
					engine.log("Answers: " + temp.getPossibleAnswer(x));
				}
				cnt++;
				scanner.nextLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return input;
	}
	
	/**
	 * Takes the read in file, randomizes the questions and answers, and puts the results into global variables questions, answers, and the answer key into ansKey
	 * Preconditions:
	 * 		The text file input has questions seperated by lines, and on each line the items are seperated by "|", with the first item being the question, the second the correct answer, and all the rest incorrect answers
	 * @param input
	 */
	private void sortQuestions(ArrayList<Question> input) {		
		questions = new Question[input.size()];
		
		//sort arrays are used to determine where each value should be pulled from either questList or ansList and put into the result array
		int[] sort1 = genUniqueRandArray(input.size());
		engine.log("Sort1 length: " + sort1.length);
		for(int i = 0; i < sort1.length; i++){
			questions[i] = input.get(sort1[i]);
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
	
	/**
	 * Reads a question from file and returns it.
	 * @param q int of Question to be read
	 * @return Question q
	 */
	private Question readQuestion(int q) 
	{
		engine.log("reading question " + q);
		Scanner scanner = new Scanner(MainGame.class.getResourceAsStream("questions.txt"));
		
		ArrayList<String> content;
		
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			Scanner scan = new Scanner(line);
			scan.useDelimiter("\\x7C");
			Integer text = new Integer(scan.next());
			if (text.equals(q))
			{
				content = new ArrayList<String>();
				while(scan.hasNext()){
					content.add(scan.next());
				}/*
				scan.next();
				String first = "";
				while (true)
				{
					String s = scan.next();
					if (s.endsWith("|"))
					{
						break;
					}
					first += " " + s;					
				}
				String a = "";
				while (true)
				{
					String s = scan.next();
					if (s.endsWith("|"))
					{
						break;
					}
					a += " " + s;
				}
				String b = "";
				while (true)
				{
					String s = scan.next();
					if (s.endsWith("|"))
					{
						break;
					}
					b += " " + s;
				}
				String c = "";
				while (true)
				{
					String s = scan.next();
					if (s.endsWith("|"))
					{
						break;
					}
					c += " " + s;
				}
				String d = "";
				while (true)
				{
					String s = scan.next();
					if (s.endsWith("|"))
					{
						break;
					}
					d += " " + s;
				}
				Integer ans = new Integer(scan.next());*/
				scanner.close();
				scan.close();
				return new Question(content.get(0), content.subList(1, content.size()).toArray(new String[] {}));
			}
			scan.close();
		}
			scanner.close();
			return new Question("Error", new String[] {"e", "e", "e", "e"});
	}
	
	public String toString() {
		return "Main Game";
	}
}
