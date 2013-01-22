/**
 * 
 */
package trivia;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * @author Cody Swendrowski
 *
 */
public class MainMenu extends GameMode {
	
	public MainMenu(GameEngine eng){
		super(eng);
		buttons.add(new Button("Start Questioning", 295, 150));
		buttons.add(new ToggleButton("Cat1", 300, 200));
		buttons.add(new ToggleButton("Cat2", 400, 200));
		buttons.add(new ToggleButton("Cat3", 300, 300));
		buttons.add(new ToggleButton("Cat4", 400, 300));
		buttons.add(new ToggleButton("Cat5", 350, 250));
		
		engine.playSound("Eternity.wav", true);
	}

	@Override
	public void run() {
		List<Button> Categories = buttons.subList(1, buttons.size());
		
		buttons.get(0).setEnabled( Button.isOneClicked(Categories) );
		
		if(buttons.get(0).isClicked()){
			ArrayList<String> cats = new ArrayList<String>();
			//assumes there is only one button that is not a toggleButton
			
			for(Button but : Categories){
				if(but.isClicked())
					cats.add(but.getText());
			}
			engine.mainGame.setCategories(cats.toArray(new String[0]));
			engine.setMode(engine.mainGame);
		}
	}
	
	public void paint(Graphics g){
		int w = engine.windowWidth, h = engine.windowHeight;
		Button[] buts = buttons.toArray(new Button[0]);
		
		if(w >= 800 && h >= 600){
			buts[0].set(w/2 - buts[0].width/2       , h/2 - h/6 );
			buts[1].set(w/2 - buts[1].width/2 - w/16, h/2 - h/12);
			buts[2].set(w/2 - buts[2].width/2 + w/16, h/2 - h/12);
			buts[3].set(w/2 - buts[3].width/2 - w/16, h/2 + h/12);
			buts[4].set(w/2 - buts[4].width/2 + w/16, h/2 + h/12);
			buts[5].set(w/2 - buts[5].width/2       , h/2       );
		}else{
			buts[0].set(w/2 - buts[0].width/2     , h/2 - 100);
			buts[1].set(w/2 - buts[1].width/2 - 50, h/2 - 50 );
			buts[2].set(w/2 - buts[2].width/2 + 50, h/2 - 50 );
			buts[3].set(w/2 - buts[3].width/2 - 50, h/2 + 50 );
			buts[4].set(w/2 - buts[4].width/2 + 50, h/2 + 50 );
			buts[5].set(w/2 - buts[5].width/2     , h/2      );
		}
		super.paint(g);
	}
	
	public String toString(){
		return "Main Menu";
	}
	
}
