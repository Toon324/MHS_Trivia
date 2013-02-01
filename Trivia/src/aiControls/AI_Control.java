package aiControls;

import trivia.Actor;

public abstract class AI_Control {
	Actor actor;
	public AI_Control(Actor a){
		actor = a;
	}
	
	public abstract void run(int ms);
}
