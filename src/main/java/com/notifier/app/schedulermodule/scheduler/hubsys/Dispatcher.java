package com.notifier.app.schedulermodule.scheduler.hubsys;

import java.util.ArrayList;
import java.util.List;

import com.corundumstudio.socketio.SocketIOClient;
import com.notifier.app.schedulermodule.model.Event;
import com.notifier.app.schedulermodule.model.Sub;
import com.notifier.app.schedulermodule.scheduler.exchange.Channel;


public class Dispatcher {

    private List<Channel> exchanges = new ArrayList<>();
    private static Dispatcher instance = new Dispatcher();

    
    public static Dispatcher getInstance(){
        return instance;
    }


    private Dispatcher(){}

    
    public void addExchange(Channel exchange){
        synchronized(this.exchanges){
            this.exchanges.add(exchange);    
        }
    }


    public void push(Event event){
        String channelName = event.getChannelName();
        synchronized(this.exchanges){
            //busca el exchange por nombre y realiza el push
            for (Channel exchangesCreated : exchanges) {
                if(exchangesCreated.getName().equals(channelName)){
                    exchangesCreated.push(event);
                    return;      
                }
            }
        }
        
    }


    public void subscribe(Sub sub){
        String channelName = sub.getChannelName();
        synchronized(this.exchanges){
            //busca el exchange por nombre y realiza la sub
            for (Channel exchangesCreated : exchanges) {
                if(exchangesCreated.getName().equals(channelName)){
                    exchangesCreated.subscribe(sub);
                    return;
                }
            }
        }
        
    }


    public String remove(SocketIOClient client, String channel) {
        String res = "";
        synchronized(this.exchanges){
            for (Channel exchangesCreated : exchanges) {
                if(exchangesCreated.getName().equals(channel)){
                    res =  exchangesCreated.remove(client);
                    break;
                }
            }
        }

        return res; //TODO
    }

}
