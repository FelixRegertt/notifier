package com.notifier.app.channels.model;

import com.corundumstudio.socketio.SocketIOClient;
import com.notifier.app.channels.events.Dispatcher;


public class Event {
    
    private String channelName;
    private SocketIOClient senderClient;
    private String info;


    public Event(String channelName, String info){
        this.channelName = channelName;
        this.info= info;
    }


    public Event(String channelName, String info, SocketIOClient senderClient) {
        this.channelName = channelName;
        this.info= info;
        this.senderClient = senderClient;
    }


    public String getChannelName(){
        return this.channelName;
    }


    public void execution() {
        Dispatcher.getInstance().push(this);
    }


    public String getInfo() {
        return info;
    }


    public SocketIOClient getSenderClient() {
        return senderClient;
    }

}
