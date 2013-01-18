package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * @author Cody Swendrowski, Dan Miller
 *
 */
public class ToggleButton extends Button {

	/**
	 * @param s String to display
	 * @param x X position to draw at
	 * @param y Y position to draw at
	 */
	public ToggleButton(String s, int x, int y) {
		super(s, x, y);
	}

	/**
	 * @param s String to display
	 * @param x X position to draw at
	 * @param y Y position to draw at
	 * @param height height of Button
	 */
	public ToggleButton(String s, int x, int y, int height) {
		super(s, x, y, height);
	}
	
	public void draw(Graphics g)
	{
		Color temp = g.getColor();
		Font tempF = g.getFont();
		
		//Autosizes width of Button to fit text
		FontMetrics fm   = g.getFontMetrics(f);
		
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(text, g);
		width = (int) rect.getWidth() + 20;
		
		g.setFont(f);
		g.setColor(Color.BLACK);
		if (clicked)
		{
			g.setColor(Color.cyan);
		}
		g.fillRect(x_pos, y_pos, width, height);
		g.setColor(Color.cyan);
		if (clicked)
		{
			g.setColor(Color.black);
		}
		g.drawString(text, x_pos+10, y_pos+20);
		
		g.setColor(temp);
		g.setFont(tempF);
	}

	public boolean isClicked()
	{
		return clicked;
	}
	
	public void checkClick(int mx, int my)
	{
		if ((mx >= x_pos) && (mx <= x_pos+width))
		{
			if ((my <= y_pos+height) && (my >= y_pos))
			{
				clicked = !clicked;
			}
		}
	}
}
