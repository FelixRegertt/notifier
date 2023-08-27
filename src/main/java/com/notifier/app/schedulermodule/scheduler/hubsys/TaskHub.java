package com.notifier.app.schedulermodule.scheduler.hubsys;

import java.util.ArrayList;
import java.util.List;

import com.notifier.app.schedulermodule.model.Event;

public class TaskHub {

    private List<Event> taskToDispatch = new ArrayList<>();
    private int maxCapacity = 20;


    public boolean tryToAdd(Event task) {
        synchronized(this.taskToDispatch) {

			if(taskToDispatch.size() == maxCapacity) {
				return false;
			}
			this.taskToDispatch.add(task);
			this.taskToDispatch.notify();
			return true;
		}
    }
    

    public Event getNext() {

		synchronized (this.taskToDispatch) {
			while (this.taskToDispatch.isEmpty()) {
				try {
					this.taskToDispatch.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return this.taskToDispatch.remove(0);
		}
	}

	
}
