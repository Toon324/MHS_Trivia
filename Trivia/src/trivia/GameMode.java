package trivia;

import java.awt.Graphics;

public abstract class GameMode {
	
	public Button[] buttons;
	public GameEngine engine;
	
	public GameMode(GameEngine eng){
		engine = eng;
	}
	
	public void run(){
	}
	
	public void clicked(int x, int y){
		try{
			for(int i = 0; i < buttons.length; i++){
				buttons[i].checkClick(x, y);
			}
		}catch(java.lang.NullPointerException e){
			engine.log("No defined buttons in " + this.toString());
		}
	}
	
	
	public void paint(Graphics g){
		try{
			for(int i = 0; i < buttons.length; i++){
				buttons[i].draw(g);
			}
		}catch(java.lang.NullPointerException e){
			engine.log("No defined buttons in " + this.toString());
		}
	}

}
