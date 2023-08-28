package com.notifier.app.channels.events;

import java.util.ArrayList;
import java.util.List;

import com.notifier.app.channels.model.Event;

public class EventQueue {

    private List<Event> eventsToDispatch = new ArrayList<>();
    private int maxCapacity = 20;


	/*
	 * Publica el evento en la lista y despierta al thread consumer
	 */
    public boolean tryToAdd(Event event) {
        synchronized(this.eventsToDispatch) {

			if(eventsToDispatch.size() == maxCapacity) {
				return false;
			}
			this.eventsToDispatch.add(event);
			this.eventsToDispatch.notify();
			return true;
		}
    }
    

	/*
	 * Retorna el siguiente evento a consumir, durmiento al thread consumer cuando no hay nada
	 */
    public Event getNext() {

		synchronized (this.eventsToDispatch) {
			while (this.eventsToDispatch.isEmpty()) {
				try {
					this.eventsToDispatch.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return this.eventsToDispatch.remove(0);
		}
	}

	
}
