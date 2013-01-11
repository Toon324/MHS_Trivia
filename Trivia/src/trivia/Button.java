package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Graphic representing a clickable area for game control.
 * 
 * @author Cody Swendrowski
 */
public class Button 
{
	private int width = 100;
	private final int height = 30;
	private String text;
	private Boolean clicked;
	private int x_pos, y_pos;
	private Font f = new Font ("Serif", Font.BOLD, 20);
	
	/**
	 * Creates a new Button.
	 * @param s String to display in button
	 * @param x x_pos to draw at
	 * @param y y_pos to draw at
	 */
	public Button(String s, int x, int y)
	{
		clicked = false;
		text = s;
		x_pos = x;
		y_pos = y;
	}
	
	/**
	 * Draws the Button.
	 * @param g Graphics to draw with
	 */
	public void draw(Graphics g)
	{
		Color temp = g.getColor();
		Font tempF = g.getFont();
		FontMetrics fm   = g.getFontMetrics(f);
		
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(text, g);
		width = (int) rect.getWidth() + 20;
		
		g.setFont(f);
		g.setColor(Color.BLACK);
		g.fillRect(x_pos, y_pos, width, height);
		g.setColor(Color.red);
		g.drawString(text, x_pos+10, y_pos+20);
		
		g.setColor(temp);
		g.setFont(tempF);
	}
	
	/**
	 * Returns true if Button was clicked; else, returns false.
	 * @return clicked
	 */
	public boolean isClicked()
	{
		return clicked;
	}
	
	/**
	 * Checks mouseclick against Button location.
	 * @param mx Mouseclick x_pos
	 * @param my Mouseclick y_pos
	 */
	public void checkClick(int mx, int my)
	{
		if ((mx >= x_pos) && (mx <= x_pos+width))
		{
			if ((my <= y_pos+height) && (my >= y_pos))
			{
				clicked = true;
			}
		}
	}
	
	/**
	 * Returns x_pos of Button.
	 * @return x_pos
	 */
	public int getX()
	{
		return x_pos;
	}

	/**
	 * Returns y_pos of Button.
	 * @return y_pos
	 */ 
	public int getY()
	{
		return y_pos;
	}
	
	/**
	 * Returns width of Button.
	 * @return width
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Returns height of Button.
	 * @return height
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Sets top-left corner coordinates of Button.
	 * @param x x_pos
	 * @param y y_pos
	 */
	public void set(int x, int y)
	{
		x_pos = x;
		y_pos = y;
	}
}
