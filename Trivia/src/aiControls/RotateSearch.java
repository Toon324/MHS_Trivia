package aiControls;

import java.util.ArrayList;

import trivia.Actor;
import trivia.Square;



public class RotateSearch extends AI_Control {

	private double startAngle, currentTrueAngle;

	public RotateSearch(Square a) {
		super(a);
		startAngle = a.getAngle();
		currentTrueAngle = startAngle;
	}

	@Override
	public void run(int ms) {
		double rotateVel = actor.getRotateVel();
		if(rotateVel < 4 * Math.PI)//<---- MAXVEL
			actor.accelerateRotation(0.5 * (ms/1000F));
		ArrayList<Actor> seenActors = actor.getActorsInView();
		if( !(seenActors.isEmpty() || seenActors.contains(this) || actor.hasAIClass(SquareAttack.class)) ){
			int start = (int)(Math.random() * seenActors.size());
			int count = 0;
			Actor selectActor = seenActors.get(start);
			for(count = 0; count < seenActors.size(); count++){
				selectActor = seenActors.get((start + count) % seenActors.size());
				if(!(selectActor instanceof Square)){
					SquareAttack tmp = new SquareAttack(((Square) actor), selectActor);
					actor.addAI_Control(tmp);
					tmp.run(ms);
					break;
				}
			}
		}
	}

}
