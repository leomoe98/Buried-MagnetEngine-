package Magnet.ApplicationLayer.Events;

import java.util.ArrayList;

public abstract class ActorVelocityRequestListener extends EventListener{
	
	public abstract void actorVelocityRequest(int actorID, float velocity, boolean onXAxis);
	
	@Override
	public void trigger(Event e) {
		actorVelocityRequest(((ActorVelocityRequestEvent) e).getActorID(), ((ActorVelocityRequestEvent) e).getVelocity(), ((ActorVelocityRequestEvent) e).onXAxis());
	}

	@Override
	public ArrayList<Event> getEventTypes() {
		ArrayList<Event> eventTypes = new ArrayList<Event>();
		eventTypes.add(new ActorVelocityRequestEvent(0, 0, false));
		return eventTypes;
	}

}
