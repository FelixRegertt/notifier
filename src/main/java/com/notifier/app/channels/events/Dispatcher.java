package com.notifier.app.channels.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.corundumstudio.socketio.SocketIOClient;
import com.notifier.app.channels.Channel;
import com.notifier.app.channels.model.Event;
import com.notifier.app.channels.model.Sub;


public class Dispatcher {

    private List<Channel> channels = new ArrayList<>();
    private static Dispatcher instance = new Dispatcher();
    private final Lock lock = new ReentrantLock();

    
    public static Dispatcher getInstance(){
        return instance;
    }


    private Dispatcher(){}

    
    public void addChannel(Channel channel){
        lock.lock();
        this.channels.add(channel);    
        lock.unlock();
    }


    /*
     * Publica el evento en el canal que especifica el parametro
     */
    public void push(Event event){
        String channelName = event.getChannelName();
        lock.lock();
        for (Channel channelCreated : channels) {
            if(channelCreated.getName().equals(channelName)){
                channelCreated.push(event);
                return;      
            }
        }
        lock.unlock();

    }


    /*
     * Agrega la sub en el canal que espeficia
     */
    public void subscribe(Sub sub){
        String channelName = sub.getChannelName();
        lock.lock();
        for (Channel channelCreated : channels) {
            if(channelCreated.getName().equals(channelName)){
                channelCreated.subscribe(sub);
                return;
            }
        }
        lock.unlock();
    }


    /*
     * Elimina la sub del canal, retornando el userid 
     */
    public String remove(SocketIOClient client, String channel) {
        String res = "";
        lock.lock();
        for (Channel channelCreated : channels) {
            if(channelCreated.getName().equals(channel)){
                res =  channelCreated.remove(client);
                break;
            }
        }      
        lock.unlock();
        return res;
    }
}
