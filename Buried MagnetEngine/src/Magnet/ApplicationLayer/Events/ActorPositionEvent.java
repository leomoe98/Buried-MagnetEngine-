package Magnet.ApplicationLayer.Events;

import Magnet.GameLogic.Math.Vector2f;

public class ActorPositionEvent extends Event{
	
	private int actorID;
	private Vector2f newPosition;
	
	public ActorPositionEvent(int actorID, Vector2f newPosition){
		this.actorID = actorID;
		this.newPosition = newPosition;
	}
	
	public final Vector2f getNewPosition(){
		return newPosition;
	}
	
	public final int getActorID(){
		return actorID;
	}

}
