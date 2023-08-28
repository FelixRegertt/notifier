package com.notifier.app.channels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.corundumstudio.socketio.SocketIOClient;
import com.notifier.app.channels.events.Dispatcher;
import com.notifier.app.channels.events.EventQueue;
import com.notifier.app.channels.model.Event;
import com.notifier.app.channels.model.Sub;
import com.notifier.app.db.User;


public class Manager {

    private EventQueue eventQueue;
    private Log log;
    private Map<String, String> users = new HashMap<>();


    public Manager(EventQueue eventQueue, Log log){
        this.eventQueue = eventQueue;
        this.log = log;
    }


    public void newEvent(Event event){
        this.eventQueue.tryToAdd(event); 
    }


    public void newSub(Sub sub){
        Dispatcher.getInstance().subscribe(sub);
    }


    public void addChannel(Channel channel){
        Dispatcher.getInstance().addChannel(channel);
    }


    public String removeSub(SocketIOClient client, String channel) {
        String userid =  Dispatcher.getInstance().remove(client, channel);
        synchronized(this.users){
            this.users.remove(userid);
        }
        return userid;
    }


    private String getLastConnection(String userid) {
        synchronized(this.users){
            return this.users.get(userid);
        }
    }
    

    /*
     * Busca la ultima conexion del userid, y hace una diferencia respecto a la
     * ultima conexion de ese usuario para retornar todos los sucesos en ese lapso de tiempo
     */
    public List<String> newLogin(String userid){ 
        String fechaStr = this.getLastConnection(userid);
        List<String> res = new ArrayList<>(); 

        if(fechaStr != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date fechaDate = dateFormat.parse(fechaStr);
                res = log.getMessagesUntilDate(fechaDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;

    }


    public void addUserInfo(User user) {
        synchronized(this.users){
            users.put(user.getId(), user.getTime());

        }
    }

}
