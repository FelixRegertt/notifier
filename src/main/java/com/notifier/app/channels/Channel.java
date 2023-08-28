package com.notifier.app.channels;

import java.util.ArrayList;
import java.util.List;

import com.corundumstudio.socketio.SocketIOClient;
import com.notifier.app.channels.model.Event;
import com.notifier.app.channels.model.Sub;
import com.notifier.app.socketio.model.Message;
import com.notifier.app.socketio.model.MessageType;


public class Channel {
    
    private String channelName;
    private List<Sub> subs;

    
    public Channel(String channelName){
        this.channelName = channelName;
        this.subs = new ArrayList<>();
    }


    /*
     * Notifica el mensaje a todos los clientes subscriptos al canal 
     */
    public void push(Event event) {

        for (Sub sub : subs) {
            sub.setMessage("Agregado " + event.getInfo());
            SocketIOClient client = sub.getClient();
            client.sendEvent(channelName, new Message(MessageType.SERVER, sub.getMessage()));
        }
    }


    public void subscribe(Sub sub) {
        this.subs.add(sub);
    }

    
    public String getName() {
        return this.channelName;
    }


    /*
     * Elimina el cliente de la lista de subs y retorna su userid
     */
    public String remove(SocketIOClient client) {
        
        String res = "";
        for (int i = 0; i < subs.size(); i++) {
            Sub sub = subs.get(i);
            if(sub.getClient().getSessionId() == client.getSessionId()){
                res = sub.getUserid();
                subs.remove(i);
                break;
            }
        }
        return res;
    }

}
