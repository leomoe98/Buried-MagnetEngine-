package Magnet.ApplicationLayer.Events;

import java.util.ArrayList;

public abstract class ActorCreationListener extends EventListener{
	
	public abstract void actorCreated(int actorID, ObjectConstruct actor);
	
	@Override
	public void trigger(Event e) {
		actorCreated(((ActorCreationEvent) e).getActorID(), ((ActorCreationEvent) e).getActor());
	}

	@Override
	public ArrayList<Event> getEventTypes() {
		ArrayList<Event> eventTypes = new ArrayList<Event>();
		eventTypes.add(new ActorCreationEvent(0, null));
		return eventTypes;
	}

}
