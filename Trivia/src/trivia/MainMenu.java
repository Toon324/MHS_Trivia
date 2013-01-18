/**
 * 
 */
package trivia;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * @author Cody Swendrowski
 *
 */
public class MainMenu extends GameMode {
	
	private Image background;
	
	public MainMenu(GameEngine eng){
		super(eng);
		buttons = new Button[6];
		buttons[0] = new Button("Start Questioning", 295, 150);
		buttons[1] = new ToggleButton("Cat1", 300, 200);
		buttons[2] = new ToggleButton("Cat2", 400, 200);
		buttons[3] = new ToggleButton("Cat3", 300, 300);
		buttons[4] = new ToggleButton("Cat4", 400, 300);
		buttons[5] = new ToggleButton("Cat5", 350, 250);
		
		try {
			background = ImageIO.read(MainMenu.class.getResourceAsStream("Resources\\space_background.jpg"));
		} catch (IOException e) {
			eng.log("Can not load background image.");
		}
	}

	@Override
	public void run() {
		if(buttons[0].isClicked()){
			ArrayList<String> cats = new ArrayList<String>();
			//assumes there is only one button that is not a toggleButton
			for(int i = 1; i < buttons.length; i++){
				if(buttons[i].isClicked())
					cats.add(buttons[i].getText());
			}
			engine.mainGame = new MainGame(engine, cats.toArray(new String[0]));
			engine.setMode(engine.mainGame);
		}
	}
	
	public void paint(Graphics g)
	{
		g.drawImage(background, 0, 0, engine.windowWidth, engine.windowHeight, null);
		super.paint(g);
	}
		
	public String toString(){
		return "Main Menu";
	}
	
}
