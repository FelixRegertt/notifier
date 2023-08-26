package com.notifier.app.schedulermodule.model;

import com.corundumstudio.socketio.SocketIOClient;

public class Sub{

    private String userid;
    private SocketIOClient client;
    private String channelName;
    private String message;


    
    public Sub(String channelName, SocketIOClient client, String userid) {
        this.channelName = channelName;
        this.client = client;
        this.userid = userid;
    }


    public String getChannelName(){
        return this.channelName;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    
    public String getMessage() {
        return message;
    }


    public SocketIOClient getClient() {
        return client;
    }


    public String getUserid() {
        return userid;
    }

}
