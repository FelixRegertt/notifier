package com.notifier.app.channels.events;

import java.util.ArrayList;
import java.util.List;

import com.corundumstudio.socketio.SocketIOClient;
import com.notifier.app.channels.Channel;
import com.notifier.app.channels.model.Event;
import com.notifier.app.channels.model.Sub;


public class Dispatcher {

    private List<Channel> channels = new ArrayList<>();
    private static Dispatcher instance = new Dispatcher();
    
    public static Dispatcher getInstance(){
        return instance;
    }


    private Dispatcher(){}

    
    public void addChannel(Channel channel){
        synchronized(this.channels){
            this.channels.add(channel);    
        }
    }


    /*
     * Publica el evento en el canal que especifica el parametro
     */
    public void push(Event event){
        String channelName = event.getChannelName();
        synchronized(this.channels){
            for (Channel channelCreated : channels) {
                if(channelCreated.getName().equals(channelName)){
                    channelCreated.push(event);
                    return;      
                }
            }
        }
    }


    /*
     * Agrega la sub en el canal que espeficia
     */
    public void subscribe(Sub sub){
        String channelName = sub.getChannelName();
        synchronized(this.channels){
            for (Channel channelCreated : channels) {
                if(channelCreated.getName().equals(channelName)){
                    channelCreated.subscribe(sub);
                    return;
                }
            }
        }
    }


    /*
     * Elimina la sub del canal, retornando el userid 
     */
    public String remove(SocketIOClient client, String channel) {
        String res = "";
        synchronized(this.channels){
            for (Channel channelCreated : channels) {
                if(channelCreated.getName().equals(channel)){
                    res =  channelCreated.remove(client);
                    break;
                }
            }      
        }
        return res;
    }
}
