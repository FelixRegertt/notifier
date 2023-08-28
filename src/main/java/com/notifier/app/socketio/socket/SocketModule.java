package com.notifier.app.socketio.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.notifier.app.channels.Channel;
import com.notifier.app.channels.Log;
import com.notifier.app.channels.Manager;
import com.notifier.app.channels.events.EventConsumer;
import com.notifier.app.channels.events.EventQueue;
import com.notifier.app.channels.model.Event;
import com.notifier.app.channels.model.Sub;
import com.notifier.app.db.User;
import com.notifier.app.db.UserService;
import com.notifier.app.socketio.model.Message;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketModule {

    private final SocketIOServer server;
    private EventQueue eventQueue = new EventQueue();
    private String defaultChannel = "default";
    private Manager manager;
    private Log logEvents = new Log();

    @Autowired
    private UserService userService;


    public SocketModule(SocketIOServer server) {
        this.server = server;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener(defaultChannel, Message.class, onNewFileAdded());
        Channel commonChannel = new Channel(defaultChannel);
        manager = new Manager(eventQueue, logEvents);
        manager.addChannel(commonChannel);
        
        Thread consumer = new Thread(new EventConsumer(eventQueue));
        consumer.start();
    }


    private DataListener<Message> onNewFileAdded() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            String messageClient = data.getMessage();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date());
            String messageToSave = time + "::" + messageClient;

            logEvents.addNewEntry(messageToSave); 
            Event event = new Event(defaultChannel, messageClient, senderClient);
            manager.newEvent(event);
        };
    }


    private ConnectListener onConnected() {
        return (client) -> {
            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());

            String userid = client.getHandshakeData().getSingleUrlParam("userid");    
            this.retrieveClientInfo(userid);
            
            Sub sub = new Sub(defaultChannel, client, userid);
            manager.newSub(sub);
            
            String result = manager.newLogin(userid).stream().collect(Collectors.joining(", "));
            client.sendEvent(defaultChannel, result);
        };

    }


    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
            String useridRemoved =  manager.removeSub(client, defaultChannel);
            this.connectionRegister(useridRemoved);     
        };
    }


    private void connectionRegister(String userid) {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(currentDate);
        User u = new User();
        u.setId(userid);
        u.setTime(time);
        userService.update(u);
    }


    private void retrieveClientInfo(String userid) {
        User user = userService.get(userid);   
        if(user ==null)
            return;
        manager.addUserInfo(user);
    }

}
