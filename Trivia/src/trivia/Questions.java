package trivia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Reads in all questions based on categories selected, then offers a way to
 * retrieve those questions and their right answer.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class Questions {

	private String[] filePaths;
	private String[] questions;
	private String[][] answers;
	private int[] answerKey;

	private int currentQuestion;
	private long lastQuestionTime = 0;

	/**
	 * Creates a new Questions controller.
	 * 
	 * @param path
	 *            Location of questions to load
	 * @param names
	 *            Names of categories to load
	 */
	public Questions(String path, String[] names) {
		filePaths = new String[names.length];
		for (int i = 0; i < names.length; i++) {
			filePaths[i] = path + names[i] + ".txt";
		}

		proccessQuestions(readFile(filePaths));

		currentQuestion = -1;
	}

	/**
	 * Returns the current question.
	 * 
	 * @return
	 */
	public String getQuestion() {
		try {
			return questions[currentQuestion];
		} catch (Exception e) {
			GameEngine.log("No current question!");
			if (GameEngine.debugMode)
				return e.getMessage();
			else
				return "";
		}
	}

	/**
	 * Returns the possible answers in an array.
	 * 
	 * @return String array of possible answers
	 */
	public String[] getAnsArray() {
		try {
			return answers[currentQuestion];
		} catch (Exception e) {
			e.printStackTrace();
			return new String[] { e.getMessage() };
		}
	}

	/**
	 * Calculates time since last question was answered. Used for score
	 * calculations.
	 * 
	 * @return Time since last question was answered
	 */
	public long getTimePassed() {
		return System.currentTimeMillis() - lastQuestionTime;
	}

	/**
	 * Checks to find the first clicked button and returns if it is the correct
	 * answer or not.
	 * 
	 * @param buts
	 *            The buttons to check
	 * @return The correctness of the answer
	 */
	public boolean checkCorrect(Button[] buts) {
		for (int i = 0; i < buts.length; i++) {
			if (buts[i].isClicked()) {
				// if the current button is the answer
				if (i == answerKey[currentQuestion]) {
					System.out.println("Question is correct!");
					return true;
				} else {
					System.out.println("Question is incorrect!");
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * Returns the correct answer.
	 * 
	 * @return String of correct answer
	 */
	public String getCorrectString() {
		String[] currentAnswers = answers[currentQuestion];
		return currentAnswers[answerKey[currentQuestion]];
	}

	/**
	 * Moves to the next question.
	 * 
	 * @return true if there is another question, false otherwise
	 */
	public boolean nextQuestion() {
		currentQuestion += 1;
		lastQuestionTime = System.currentTimeMillis();
		if (currentQuestion >= questions.length) {
			return false;
		}
		return true;
	}

	/**
	 * Helper method. Reads in questions from given filepaths.
	 * 
	 * @return ArrayList<String> containing the contents of the file, separated
	 *         by line
	 */
	private ArrayList<String> readFile(String[] filePaths) {

		ArrayList<String> input = new ArrayList<String>();

		for (int i = 0; i < filePaths.length; i++) {
			Scanner scanner = new Scanner(
					Trivia.class.getResourceAsStream(filePaths[i]));
			scanner.useDelimiter("\n"); // Uses newline as a signal that it is a
										// new question
			try {
				while (scanner.hasNext()) {
					String s = scanner.next();
					input.add(s);
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			} finally {
				scanner.close();
			}
		}
		return input;
	}

	/**
	 * Helper method. Takes the read in file, randomizes the questions and
	 * answers, and puts the results into global variables questions, answers,
	 * and the answer key into ansKey Preconditions: The text file input has
	 * questions separated by lines, and on each line the items are separated by
	 * tabs.
	 * 
	 * @param input
	 *            ArrayList of strings to process.
	 */
	private void proccessQuestions(ArrayList<String> input) {

		ArrayList<String> questList = new ArrayList<String>();
		ArrayList<ArrayList<String>> ansList = new ArrayList<ArrayList<String>>();
		ArrayList<Integer> ansKeyList = new ArrayList<Integer>();

		// Variables for use inside the loops
		String line, item;
		int lineCount = 0;
		int itemCnt = 0;
		Iterator<String> iter = input.iterator();

		while (iter.hasNext()) {
			line = iter.next();

			// Scanner used to separate out each line based on the tab character
			Scanner scan = new Scanner(line);
			scan.useDelimiter("\\t");
			itemCnt = 0;

			while (scan.hasNext()) {
				item = scan.next();

				if (itemCnt > 1) {
					ansList.get(lineCount).add(item);
				} else if (itemCnt == 1) {
					ansKeyList.add(Integer.parseInt(item) - 1);
					itemCnt++;
				} else if (itemCnt == 0) {
					questList.add(item);
					ansList.add(new ArrayList<String>());
					itemCnt++;
				}
			}
			scan.close();
			lineCount++;
		}

		questions = new String[questList.size()];
		answerKey = new int[questList.size()];

		answers = new String[questList.size()][];

		// Sort arrays are used to determine which value should be pulled
		// from either questList or ansList and put into the result array
		int[] sort1 = genUniqueRandArray(questList.size());
		int[] sort2;
		for (int i = 0; i < sort1.length; i++) {
			questions[i] = questList.get(sort1[i]);

			answers[i] = new String[ansList.get(sort1[i]).size()];
			sort2 = genUniqueRandArray(answers[i].length);

			for (int j = 0; j < answers[i].length; j++) {
				answers[i][j] = ansList.get(sort1[i]).get(sort2[j]);
				if (sort2[j] == ansKeyList.get(sort1[i])) {
					// if this is pulling the same answer as determined in the
					// list, set the answer key to this index as the correct
					// answer
					answerKey[i] = j;
				}
			}
		}

	}

	/**
	 * Helper method. Used to generate an array of a certain length which
	 * contains numbers from 0 to length - 1, with each number occurring only
	 * once.
	 * 
	 * @param len
	 *            length of the result array
	 * @return Array of random numbers ranging from 0 to len - 1, with no number
	 *         occuring twice
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
		for (int i = 0; i < result.length; i++) {
			result[i] = iter.next().intValue();
		}
		return result;
	}

}
