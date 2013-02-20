package aiControls;

import java.awt.Point;
import java.util.ArrayList;

import Actors.Actor;
import Actors.Triangle;


import trivia.GameEngine;

public class TriangleFleet {
	
	private ArrayList<Triangle> members;
	private ArrayList<Actor> knownEnemies;
	private Actor target;
	public Point envSize = GameEngine.getEnv();
	
	public TriangleFleet() {
		members = new ArrayList<Triangle>();
		knownEnemies = new ArrayList<Actor>();
		target = Triangle.getEmptyInstance();
		target.setCenter(envSize.x/2, envSize.y/2);
	}
	
	public void addTriangle(Triangle t){
		members.add(t);
		t.clearAI_Control();
		t.addAI_Control(new TriangleAttack(t, this));
	}
	
	public void reportShip(Actor a){
		knownEnemies.add(a);
		if(knownEnemies.size() > 10) knownEnemies.remove(0);
		if(target == null) target = a;
	}
	
	public Actor getCurrentTarget(){
		target.setCenter(envSize.x/2, envSize.y/2);
		return target;
	}
	
	public void nextStep(int ms){
		
	}
}
