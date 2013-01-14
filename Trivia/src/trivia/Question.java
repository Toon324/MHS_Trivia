package trivia;

/**
 * Represents a question to be asked. Holds possible answers and which is correct.
 * @author Cody Swendrowski
 */
public class Question
{
	private String question;
	private int answer;
	private String[] answers;
	
	/**
	 * Creates new multiple choice question.
	 * @param q Question to be displayed
	 * @param a Answer A [INT 1]
	 * @param b Answer B [INT 2]
	 * @param c Answer C [INT 3]
	 * @param d Answer D [INT 4]
	 * @param ans int of which answer is correct.
	 */
	public Question(String q, String[] ansStr)
	{
		question = q;
		answers = ansStr;
		answer = 0;
	}
	
	/**
	 * Returns question to be asked.
	 * @return question
	 */
	public String getQuestion()
	{
		return question;
	}
	
	/**
	 * Returns int of which answer is correct.
	 * @return answer
	 */
	public int getAnswer()
	{
		return answer;
	}
	
	/**
	 * Returns array of all possible answers.
	 * @return answers
	 */
	public String[] getPossibleAnswers()
	{
		return answers;
	}
	
	public String getPossibleAnswer(int a)
	{
		return answers[a];
	}
}
