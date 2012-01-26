package org.bitducks.spoofing.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Registry {
	
	HashMap<Event, Set<EventReciever>> registry;
	
	public Registry() {
		this.registry = new HashMap<Event, Set<EventReciever>>();
	}
	
	public void addReciever( Event event, EventReciever reciever ) {
		if( !this.registry.containsKey(event) ) {
			this.registry.put(event, new HashSet());
		}
		
		this.registry.get(event).add(reciever);
	}
	
	public void sendResponse(Event event, EventResponse response) {
		for( EventReciever reciever: this.registry.get(event) ) {
			reciever.recieveEvent(response);
		}
	}

}
