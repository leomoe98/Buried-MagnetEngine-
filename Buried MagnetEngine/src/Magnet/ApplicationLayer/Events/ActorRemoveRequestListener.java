package Magnet.ApplicationLayer.Events;

import java.util.ArrayList;

public abstract class ActorRemoveRequestListener extends EventListener{
	
public abstract void actorRemoveRequest(int actorID);
	
	@Override
	public void trigger(Event e) {
		actorRemoveRequest(((ActorRemoveRequestEvent) e).getActorID());
	}

	@Override
	public ArrayList<Event> getEventTypes() {
		ArrayList<Event> eventTypes = new ArrayList<Event>();
		eventTypes.add(new ActorRemoveRequestEvent(0));
		return eventTypes;
	
	}

}
