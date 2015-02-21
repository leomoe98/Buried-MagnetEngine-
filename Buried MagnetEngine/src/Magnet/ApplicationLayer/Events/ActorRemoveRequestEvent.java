package Magnet.ApplicationLayer.Events;

public class ActorRemoveRequestEvent extends Event{
	
	private int actorID;
	
	public ActorRemoveRequestEvent(int actorID){
		this.actorID = actorID;
	}
	
	public final int getActorID(){
		return actorID;
	}

}
