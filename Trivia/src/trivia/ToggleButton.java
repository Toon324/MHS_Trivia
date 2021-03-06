package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * A button that has two states, on and off. Clicking the button flips the
 * state.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class ToggleButton extends Button {

	/**
	 * @param s
	 *            String to display
	 * @param x
	 *            X position to draw at
	 * @param y
	 *            Y position to draw at
	 */
	public ToggleButton(String s, int x, int y) {
		super(s, x, y);
	}

	@Override
	public void draw(Graphics g) {
		Color temp = g.getColor();
		Font tempF = g.getFont();

		// Autosizes width of Button to fit text
		FontMetrics fm = g.getFontMetrics(f);

		java.awt.geom.Rectangle2D rect = fm.getStringBounds(text, g);
		width = (int) rect.getWidth() + 20;
		height = (int) rect.getHeight();
		bounds = new Polygon();
		
		bounds.addPoint(x_pos, y_pos);
		bounds.addPoint(x_pos, y_pos+height);
		bounds.addPoint(x_pos+width, y_pos+height);
		bounds.addPoint(x_pos+width, y_pos);

		g.setFont(f);
		g.setColor(Color.WHITE);
		if (clicked)
			g.setColor(Color.LIGHT_GRAY);

		g.fillPolygon(bounds);
		g.setColor(Color.gray);
		// Draws inverse sections in gray depending on state
		if (!clicked) {
			g.fillRect(x_pos - 2, y_pos - 2, width + 2, 2);
			g.fillRect(x_pos - 2, y_pos - 2, 2, height + 2);
		} else {
			g.fillRect(x_pos + width, y_pos, 2, height + 2);
			g.fillRect(x_pos - 2, y_pos + height, width + 2, 2);
		}

		g.setColor(Color.BLACK);
		if (!clicked) {
			g.fillRect(x_pos + width, y_pos, 2, height + 2);
			g.fillRect(x_pos - 2, y_pos + height, width + 2, 2);
		} else {
			g.fillRect(x_pos - 2, y_pos, width + 2, 2);
			g.fillRect(x_pos - 2, y_pos, 2, height + 2);
		}

		g.drawString(text, x_pos + 10, y_pos + 23);

		g.setColor(temp);
		g.setFont(tempF);
	}

	@Override
	public boolean isClicked() {
		return clicked;
	}

	@Override
	public void checkClick(int mx, int my) {
		if (enabled && (mx >= x_pos) && (mx <= x_pos + width)
				&& (my <= y_pos + height) && (my >= y_pos)) {
			clicked = !clicked; //Toggles back and forth
		}
	}
}
