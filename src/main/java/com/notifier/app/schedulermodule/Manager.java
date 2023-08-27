package com.notifier.app.schedulermodule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.corundumstudio.socketio.SocketIOClient;
import com.notifier.app.persistencemodule.User;
import com.notifier.app.schedulermodule.model.Event;
import com.notifier.app.schedulermodule.model.Sub;
import com.notifier.app.schedulermodule.scheduler.exchange.Channel;
import com.notifier.app.schedulermodule.scheduler.hubsys.Dispatcher;
import com.notifier.app.schedulermodule.scheduler.hubsys.TaskHub;


public class Manager {

    private TaskHub taskHub;
    private Log log;
    private Map<String, String> users = new HashMap<>();


    public Manager(TaskHub taskHub, Log log){
        this.taskHub = taskHub;
        this.log = log;
    }


    public void newEvent(Event task){
        boolean tryAdd = this.taskHub.tryToAdd(task); 
        if (!tryAdd) {
        }
    }


    public void newSub(Sub sub){
        Dispatcher.getInstance().subscribe(sub);
    }


    public void addChannel(Channel channel){
        Dispatcher.getInstance().addExchange(channel);
    }


    public String removeSub(SocketIOClient client, String channel) {
        String userid =  Dispatcher.getInstance().remove(client, channel);
        this.users.remove(userid);
        return userid;
    }


    public String getLastConnection(String userid) {
        return this.users.get(userid);

    }


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
        users.put(user.getId(), user.getTime());
    }



}
