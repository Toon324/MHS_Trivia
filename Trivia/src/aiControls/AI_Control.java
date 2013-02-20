package aiControls;

import Actors.AI_Actor;
import Actors.Actor;

public abstract class AI_Control {
	AI_Actor actor;
	public AI_Control(AI_Actor a){
		actor = a;
	}
	
	public abstract void run(int ms);
}
