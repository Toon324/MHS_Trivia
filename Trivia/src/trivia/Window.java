package trivia;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Displays the Applet.
 * 
 * @author Cody
 */
public class Window 
{
	public static void main(String[] args)
	{
		Boolean debugMode = true;
		//Sets up game
		Trivia game = new Trivia(debugMode);
		JFrame frame = new JFrame("Trivia");
		
		//Initializes game
		game.init();
		
		//Add applet to frame
		frame.add(game, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800,600);
		frame.setResizable(true);
		frame.setVisible(true);
		//Runs game
		game.run();
	}
}
