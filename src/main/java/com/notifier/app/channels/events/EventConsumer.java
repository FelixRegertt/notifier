package com.notifier.app.channels.events;

import com.notifier.app.channels.model.Event;


public class EventConsumer implements Runnable{

    private EventQueue eventQueue;
    
	
	public EventConsumer(EventQueue eventQueue) {
		super();
		this.eventQueue = eventQueue;
	}


	@Override
	public void run() {
		while(true) {
			Event next = this.eventQueue.getNext();	
			next.execution();
		}
	}   

}
