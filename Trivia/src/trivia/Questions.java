package trivia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Questions {
	
	private String[] filePaths;
	public String[] questions;
	public String[][] answers;
	public int[] answerKey;
	
	public Questions(String path, String[] names){
		filePaths = new String[names.length];
		for(int i = 0; i < names.length; i++){
			filePaths[i] = path + names[i] + ".txt";
		}
		
		proccessQuestions(readFile(filePaths));
	}
	


	/**
	 * Reads in a file to an array
	 * 
	 * @return ArrayList<String> arrayList containing the contents of the file,
	 *         separated by line
	 */
	private ArrayList<String> readFile(String[] filePaths) {

		ArrayList<String> input = new ArrayList<String>();
		
		for(int i = 0; i < filePaths.length; i++){
			Scanner scanner = new Scanner(
					Trivia.class.getResourceAsStream(filePaths[i]));
			scanner.useDelimiter("\n");
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
		answerKey = new int[questList.size()];
		
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
					answerKey[i] = j;
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
	
}
