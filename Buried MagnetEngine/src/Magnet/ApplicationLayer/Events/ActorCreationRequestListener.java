package Magnet.ApplicationLayer.Events;

import java.util.ArrayList;

public abstract class ActorCreationRequestListener extends EventListener{

	public abstract void actorCreationRequest(int actorID, ObjectConstruct actor);
	
	@Override
	public void trigger(Event e) {
		actorCreationRequest(((ActorCreationRequestEvent) e).getActorID(), ((ActorCreationRequestEvent) e).getActor());
	}

	@Override
	public ArrayList<Event> getEventTypes() {
		ArrayList<Event> eventTypes = new ArrayList<Event>();
		eventTypes.add(new ActorCreationRequestEvent(0, null));
		return eventTypes;
	}

}
