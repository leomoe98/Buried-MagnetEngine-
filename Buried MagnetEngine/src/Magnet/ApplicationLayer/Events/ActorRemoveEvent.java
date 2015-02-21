package Magnet.ApplicationLayer.Events;

public class ActorRemoveEvent extends Event{
		
	private int actorID;
	
	public ActorRemoveEvent(int actorID){
		this.actorID = actorID;
	}
	
	public final int getActorID(){
		return actorID;
	}

	
}
