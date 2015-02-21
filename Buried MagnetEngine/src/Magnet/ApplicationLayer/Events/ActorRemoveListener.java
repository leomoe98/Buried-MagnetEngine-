package Magnet.ApplicationLayer.Events;

import java.util.ArrayList;

public abstract class ActorRemoveListener extends EventListener{
	
	public abstract void actorRemoved(int actorID);
	
	@Override
	public void trigger(Event e) {
		actorRemoved(((ActorRemoveEvent) e).getActorID());
	}

	@Override
	public ArrayList<Event> getEventTypes() {
		ArrayList<Event> eventTypes = new ArrayList<Event>();
		eventTypes.add(new ActorRemoveEvent(0));
		return eventTypes;
	
	}
}
