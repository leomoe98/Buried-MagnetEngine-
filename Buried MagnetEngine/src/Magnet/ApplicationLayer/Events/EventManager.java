package Magnet.ApplicationLayer.Events;

import java.util.ArrayList;

public class EventManager {
	
	private static int eventCountPerProcess = 0;
	private static ArrayList<Event> eventQueue = new ArrayList<Event>();
	private static ArrayList<EventListener> listeners = new ArrayList<EventListener>();
	
	public static void queueEvent(Event e){
		eventQueue.add(e);
	}
	
	public static void instantEvent(Event e){
		for(int i = 0; i < eventQueue.size(); i++){
			for(int l = 0; l < listeners.size(); l ++){
				for(int t = 0; t < listeners.get(l).getEventTypes().size(); t++){
					if(listeners.get(l).getEventTypes().get(t).getClass() == e.getClass())listeners.get(l).trigger(e);
				}
			}
		}
	}
	
	public static void processEventQueue(){
		eventCountPerProcess = eventQueue.size();
		for(int i = 0; i < eventQueue.size(); i++){
			for(int l = 0; l < listeners.size(); l ++){
				if(UniversalListener.class.isAssignableFrom(listeners.get(l).getClass()) && listeners.get(l) != eventQueue.get(i).getSource())listeners.get(l).trigger(eventQueue.get(i));
				for(int t = 0; t < listeners.get(l).getEventTypes().size(); t++){
					if(listeners.get(l).getEventTypes().get(t).getClass() == eventQueue.get(i).getClass() && listeners.get(l) != eventQueue.get(i).getSource())listeners.get(l).trigger(eventQueue.get(i));
				}
			}
		}
		eventQueue.clear();
	}
	
	public static void addListener(Object o){
		if(o instanceof EventListener){
			listeners.add((EventListener) o);
		}
		System.out.println("Listener added: " + o.getClass().toString() + " | active Listeners: " + listeners.size());
	}
	
	public static void printListeners(){
		for(int i = 0; i < listeners.size(); i++){
			System.out.println(listeners.get(i).getClass().toString());
		}
	}
	
	public static void removeListener(Object o){
		if(o instanceof EventListener)listeners.remove((EventListener) o);
	}
	
	public static final int getEventCountPerProcess(){
		return eventCountPerProcess;
	}
	
	public static final void clearQueue(){
		eventQueue.clear();
	}
	
	public static final void clear(){
		listeners.clear();
	}

}
