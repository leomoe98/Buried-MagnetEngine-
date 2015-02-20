package Magnet.ApplicationLayer.Events;

import java.util.ArrayList;

import Magnet.GameLogic.Math.Vector2f;

public abstract class ActorPositionListener extends EventListener{
	
public abstract void actorPositionChange(int actorID, Vector2f newPosition);
	
	@Override
	public void trigger(Event e) {
		actorPositionChange(((ActorPositionEvent) e).getActorID(), ((ActorPositionEvent) e).getNewPosition());
	}

	@Override
	public ArrayList<Event> getEventTypes() {
		ArrayList<Event> eventTypes = new ArrayList<Event>();
		eventTypes.add(new ActorPositionEvent(0, null));
		return eventTypes;
	}

}
