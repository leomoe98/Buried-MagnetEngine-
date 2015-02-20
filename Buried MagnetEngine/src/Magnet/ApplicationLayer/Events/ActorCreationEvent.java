package Magnet.ApplicationLayer.Events;

public class ActorCreationEvent extends Event{
	
	private ObjectConstruct actor;
	private int actorID;
	
	public ActorCreationEvent(int actorID, ObjectConstruct actor){
		this.actor = actor;
		this.actorID = actorID;
	}
	
	public final ObjectConstruct getActor(){
		return actor;
	}
	
	public final int getActorID(){
		return actorID;
	}

}
