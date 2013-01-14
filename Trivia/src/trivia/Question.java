package trivia;

/**
 * Represents a question to be asked. Holds possible answers and which is correct.
 * @author Cody Swendrowski
 */
public class Question
{
	private String question, a, b, c, d;
	private int answer;
	private String[] answers = new String[4];
	
	/**
	 * Creates new multiple choice question.
	 * @param q Question to be displayed
	 * @param a Answer A [INT 1]
	 * @param b Answer B [INT 2]
	 * @param c Answer C [INT 3]
	 * @param d Answer D [INT 4]
	 * @param ans int of which answer is correct.
	 */
	public Question(String q, String A, String B, String C, String D, int ans)
	{
		question = q;
		a= "A)" + A;
		b= "B)" +B;
		c= "C)" +C;
		d= "D)" +D;
		answer = ans;
		pack();
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
	
	/**
	 * Packs together all possible answers into an array.
	 */
	private void pack()
	{
		answers[0]=a;
		answers[1]=b;
		answers[2]=c;
		answers[3]=d;
	}
}
