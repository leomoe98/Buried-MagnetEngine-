package Magnet.ApplicationLayer.Events;

import java.io.Serializable;

public abstract class Event implements Serializable {
	
	private EventListener source = null;
	
	public final EventListener getSource(){
		return source;
	}
	
	public final void setSource(EventListener el){
		source = el;
	}

}
