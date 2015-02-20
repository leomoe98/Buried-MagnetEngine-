package Magnet.ApplicationLayer.Events;

import java.util.ArrayList;

import Magnet.GameLogic.Math.Vector2f;

public abstract class ActorMovementListener extends EventListener{
	
public abstract void actorMovementChange(int actorID, Vector2f newPosition, Vector2f newVelocity);
	
	@Override
	public void trigger(Event e) {
		actorMovementChange(((ActorMovementEvent) e).getActorID(), ((ActorMovementEvent) e).getNewPosition(), ((ActorMovementEvent) e).getNewVelocity());
	}

	@Override
	public ArrayList<Event> getEventTypes() {
		ArrayList<Event> eventTypes = new ArrayList<Event>();
		eventTypes.add(new ActorMovementEvent(0, null, null));
		return eventTypes;
	}

}

