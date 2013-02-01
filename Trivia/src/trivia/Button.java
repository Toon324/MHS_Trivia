package trivia;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;

/**
 * Graphic representing a clickable area for game control.
 * 
 * @author Cody Swendrowski, Dan Miller
 */
public class Button {
	protected int width, height;
	protected String text;
	protected boolean clicked;
	protected boolean enabled;
	protected int x_pos, y_pos;
	protected Font f = new Font("Serif", Font.BOLD, 25);

	/**
	 * Creates a new Button.
	 * 
	 * @param s
	 *            String to display in Button
	 * @param x
	 *            x_pos to draw at
	 * @param y
	 *            y_pos to draw at
	 */
	public Button(String s, int x, int y) {
		enabled = true;
		clicked = false;
		text = s;
		x_pos = x;
		y_pos = y;
	}

	/**
	 * Draws the Button.
	 * 
	 * @param g
	 *            Graphics to draw with
	 */
	public void draw(Graphics g) {
		Color temp = g.getColor();
		Font tempF = g.getFont();

		g.setFont(f);

		// Autosizes width and height of Button to fit text
		FontMetrics fm = g.getFontMetrics(f);
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(text, g);
		width = (int) rect.getWidth() + 20;
		height = (int) rect.getHeight();
		Polygon leftTri = new Polygon();
		Polygon rightTri = new Polygon();
		leftTri.addPoint(x_pos - 1, y_pos - 1);
		leftTri.addPoint(x_pos - 20, y_pos + ((height) / 2));
		leftTri.addPoint(x_pos - 1, y_pos + height + 1);

		rightTri.addPoint(x_pos + width + 1, y_pos - 1);
		rightTri.addPoint(x_pos + width + 20, y_pos + ((height) / 2));
		rightTri.addPoint(x_pos + width + 1, y_pos + height + 1);
		g.setColor(Color.cyan);
		g.fillPolygon(leftTri);
		g.fillPolygon(rightTri);
		g.fillRect(x_pos - 1, y_pos - 1, width + 2, height + 2);

		g.setColor(Color.BLACK);
		if (!enabled)
			g.setColor(Color.DARK_GRAY);

		leftTri = new Polygon();
		rightTri = new Polygon();
		leftTri.addPoint(x_pos, y_pos);
		leftTri.addPoint(x_pos - 19, y_pos + ((height) / 2));
		leftTri.addPoint(x_pos, y_pos + height);

		rightTri.addPoint(x_pos + width, y_pos);
		rightTri.addPoint(x_pos + width + 19, y_pos + ((height) / 2));
		rightTri.addPoint(x_pos + width, y_pos + height);

		g.fillPolygon(leftTri);
		g.fillPolygon(rightTri);
		g.fillRect(x_pos, y_pos, width, height);
		g.setColor(Color.cyan);
		g.drawString(text, x_pos + 10, y_pos + 23);

		g.setColor(temp);
		g.setFont(tempF);
	}

	/**
	 * Returns true if Button was clicked; else, returns false. If it is true,
	 * it sets it to false immediately. One-use method.
	 * 
	 * @return clicked
	 */
	public boolean isClicked() {
		if (clicked) {
			clicked = false;
			return true;
		}
		return clicked;
	}

	/**
	 * Checks mouseclick against Button location.
	 * 
	 * @param mx
	 *            Mouseclick x_pos
	 * @param my
	 *            Mouseclick y_pos
	 */
	public void checkClick(int mx, int my) {
		if (enabled && (mx >= x_pos) && (mx <= x_pos + width)
				&& (my <= y_pos + height) && (my >= y_pos)) {
			clicked = true;
		}
	}

	public static boolean isOneClicked(List<Button> buts) {
		try {
			for (Button but : buts) {
				// intentionally does not use the .isClicked method to avoid
				// unclicking buttons
				if (but.clicked)
					return true;
			}
			return false;
		} catch (java.lang.NullPointerException e) {
			return false;
		}
	}

	public void setEnabled(boolean enable) {
		if (!enable)
			clicked = false;
		enabled = enable;
	}

	/**
	 * Returns x_pos of Button.
	 * 
	 * @return x_pos
	 */
	public int getX() {
		return x_pos;
	}

	/**
	 * Returns y_pos of Button.
	 * 
	 * @return y_pos
	 */
	public int getY() {
		return y_pos;
	}

	/**
	 * Returns width of Button.
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns height of Button.
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the text displayed in the button
	 * 
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets top-left corner coordinates of Button.
	 * 
	 * @param x
	 *            x_pos
	 * @param y
	 *            y_pos
	 */
	public void set(int x, int y) {
		x_pos = x;
		y_pos = y;
	}

	/**
	 * Sets the string inside the button
	 * 
	 * @param s
	 *            text for the button
	 */
	public void setString(String s) {
		text = s;
	}
}
