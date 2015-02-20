package Magnet.ApplicationLayer.Events;

public class ActorCreationRequestEvent extends Event{
	
	private ObjectConstruct actor;
	private int actorID;
	
	public ActorCreationRequestEvent(int actorID, ObjectConstruct actor){
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
