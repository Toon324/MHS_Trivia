package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.Scanner;

/**
 * @author Cody Swendrowski, Dan Miller
 *
 */
public class Instructions extends GameMode {

	private Font large = new Font("Serif", Font.BOLD, 30);
	private String[] instructions = {
			"Click the category buttons to choose which quesitons to be tested on.",
			"Click on the button containing your answer of choice.",
			"The longer you take to answer, the less points you get for being right." };
	
	/**
	 * @param eng
	 */
	public Instructions(GameEngine eng) {
		super(eng);
		buttons.add(new Button("Main Menu", 200, 500));
	}
	
	public void run()
	{
		if (buttons.get(0).isClicked())
		{
			engine.setMode(engine.mainMenu);
		}
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(Color.cyan);
		g.setFont(large);
		
		
		for(int i=0; i<instructions.length; i++)
		{
			String[] instruction = sizeString(instructions[i], g);
			for(int x=0; x<instruction.length; x++)
			{
				g.drawString(instruction[x], 10, (i+1)*100+(x*30));
			}
		}
	}
	
	private String[] sizeString(String s, Graphics g)
	{
		FontMetrics fm   = g.getFontMetrics(large);
		Rectangle2D rect = fm.getStringBounds(s, g);
		int stringWidth = (int) rect.getWidth()+60;
		
		if (stringWidth < engine.windowWidth)
		{
			String[] toReturn = {s};
			return toReturn;
		}
		else //If question goes outside the screen, split it
		{
			StringBuilder part1 = new StringBuilder();
			StringBuilder part2 = new StringBuilder();
			Scanner splitter = new Scanner(s);
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
			String[] toReturn = {part1.toString(),part2.toString()};
			return toReturn;
		}
	}

}
