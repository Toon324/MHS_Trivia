/**
 * 
 */
package trivia;

import java.awt.Graphics;

/**
 * @author Cody Swendrowski
 *
 */
public class MainMenu implements GameEngine.GameMode {
	
	private Button[] buttons;
	private GameEngine engine;
	
	
	public MainMenu(GameEngine eng){
		engine = eng;
		buttons = new Button[1];
		buttons[0] = new Button("Start Questioning", 10, 10, 100, 30);
	}
	
	/* (non-Javadoc)
	 * @see trivia.GameMode#run()
	 */
	@Override
	public void run() {
		if(buttons[0].isClicked()){
			engine.setMode(engine.MAIN_GAME);
		}
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
