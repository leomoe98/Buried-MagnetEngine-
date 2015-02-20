package Magnet.ApplicationLayer.Events;

import Magnet.GameLogic.Math.Vector2f;

public class ActorMovementEvent extends Event{
	
	private int actorID;
	private Vector2f newPosition;
	private Vector2f newVelocity;
	
	public ActorMovementEvent(int actorID, Vector2f newPosition,Vector2f newVelocity){
		this.actorID = actorID;
		this.newPosition = newPosition;
		this.newVelocity = newVelocity;
	}
	
	public final Vector2f getNewPosition(){
		return newPosition;
	}
	
	public final Vector2f getNewVelocity(){
		return newVelocity;
	}
	
	public final int getActorID(){
		return actorID;
	}

}
	
