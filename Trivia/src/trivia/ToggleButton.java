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
	
	public void draw(Graphics g)
	{
		Color temp = g.getColor();
		Font tempF = g.getFont();
		
		//Autosizes width of Button to fit text
		FontMetrics fm   = g.getFontMetrics(f);
		
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(text, g);
		width = (int) rect.getWidth() + 20;
		height = (int) rect.getHeight();
		
		g.setFont(f);
		g.setColor(Color.RED);
		if (clicked) g.setColor(Color.GREEN);
		if (!enabled) g.setColor(Color.DARK_GRAY);
		
		g.fillRect(x_pos, y_pos, width, height);
		g.setColor(Color.BLACK);
		
		g.drawString(text, x_pos+10, y_pos+23);
		
		g.setColor(temp);
		g.setFont(tempF);
	}

	public boolean isClicked()
	{
		return clicked;
	}
	
	public void checkClick(int mx, int my)
	{
		if (enabled &&
				(mx >= x_pos) && (mx <= x_pos+width) &&
				(my <= y_pos+height) && (my >= y_pos)){
			clicked = !clicked;
		}
	}
}
