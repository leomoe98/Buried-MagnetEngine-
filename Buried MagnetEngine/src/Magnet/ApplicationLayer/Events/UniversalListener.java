package Magnet.ApplicationLayer.Events;

import java.util.ArrayList;

public abstract class UniversalListener extends EventListener{

	public abstract void universal(Event e);
	
	@Override
	public void trigger(Event e) {
		universal(e);
	}

	@Override
	public ArrayList<Event> getEventTypes() {
		ArrayList<Event> eventTypes = new ArrayList<Event>();
		
		return eventTypes;
	}
	
}
