package Magnet.ApplicationLayer.Events;

public class ActorVelocityRequestEvent extends Event{
	
	private int actorID;
	private float velocity;
	private boolean onXAxis;
	
	public ActorVelocityRequestEvent(int actorID, float velocity, boolean onXAxis){
		this.actorID = actorID;
		this.velocity = velocity;
		this.onXAxis = onXAxis;
	}
	
	public final float getVelocity(){
		return velocity;
	}
	
	public final int getActorID(){
		return actorID;
	}
	
	public final boolean onXAxis(){
		return onXAxis;
	}

}
