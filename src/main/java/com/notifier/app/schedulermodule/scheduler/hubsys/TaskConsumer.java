package com.notifier.app.schedulermodule.scheduler.hubsys;

import com.notifier.app.schedulermodule.model.Event;


public class TaskConsumer implements Runnable{

    private TaskHub taskHub;
    
	
	public TaskConsumer(TaskHub taskHub) {
		super();
		this.taskHub = taskHub;
	}


	@Override
	public void run() {
		while(true) {
			Event next = this.taskHub.getNext();	
			next.execution();
		}
	}   

}
