package Magnet.ApplicationLayer.Events;

import java.util.ArrayList;

public abstract class EventListener{
	
	public EventListener(){
		EventManager.addListener(this);
	}
	
	public abstract void trigger(Event e);
	
	public abstract ArrayList<Event> getEventTypes();
}