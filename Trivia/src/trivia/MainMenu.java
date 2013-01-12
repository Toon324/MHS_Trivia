/**
 * 
 */
package trivia;

import java.awt.Graphics;

/**
 * @author Cody Swendrowski
 *
 */
public class MainMenu implements GameMode {
	
	private Button[] buttons = new Button[1];
	
	public MainMenu(){
		buttons[0] = new Button("Test", 10 ,10);
	}
	
	/* (non-Javadoc)
	 * @see trivia.GameMode#run()
	 */
	@Override
	public void run() {
		if(buttons[0].isClicked()){
			buttons[0].set(buttons[0].getX() + 5, buttons[0].getY() + 5);
		}
		// TODO Add in logic

	}

	/* (non-Javadoc)
	 * @see trivia.GameMode#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		buttons[0].draw(g);
		// TODO Auto-generated method stub

	}

	@Override
	public void clicked(int x, int y) {
		for(int i = 0; i < buttons.length; i++){
			buttons[i].checkClick(x, y);
		}
	}

}
