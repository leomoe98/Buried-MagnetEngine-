package Magnet.ApplicationLayer.Events;

import java.util.ArrayList;

public abstract class GameStateListener extends EventListener{

	public abstract void gameStateChange(int newState);
	
	@Override
	public void trigger(Event e) {
		gameStateChange(((GameStateEvent) e).getGameState());
	}

	@Override
	public ArrayList<Event> getEventTypes() {
		ArrayList<Event> eventTypes = new ArrayList<Event>();
		eventTypes.add(new GameStateEvent(0));
		return eventTypes;
	}
}
