package com.notifier.app.socketio.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.notifier.app.persistencemodule.User;
import com.notifier.app.persistencemodule.UserService;
import com.notifier.app.schedulermodule.Log;
import com.notifier.app.schedulermodule.Manager;
import com.notifier.app.schedulermodule.model.Event;
import com.notifier.app.schedulermodule.model.Sub;
import com.notifier.app.schedulermodule.scheduler.exchange.Channel;
import com.notifier.app.schedulermodule.scheduler.hubsys.TaskConsumer;
import com.notifier.app.schedulermodule.scheduler.hubsys.TaskHub;
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
    private TaskHub taskHub = new TaskHub();
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
        manager = new Manager(taskHub, logEvents);
        manager.addChannel(commonChannel);
        
        Thread consumer = new Thread(new TaskConsumer(taskHub));
        consumer.start();
    }


    //agrega el nuevo evento al sistema y lo agrega al log 
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


    //se conecta un nuevo cliente, se vuelva todo lo nuevo hacia el 
    private ConnectListener onConnected() {
        return (client) -> {
            String room = client.getHandshakeData().getSingleUrlParam("room");
            client.joinRoom(room);
            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());

            String userid = client.getHandshakeData().getSingleUrlParam("userid");    
            this.retrieveClientInfo(userid);
            
            Sub sub = new Sub(defaultChannel, client, userid);
            manager.newSub(sub);
            
            String result = manager.newLogin(userid).stream().collect(Collectors.joining(", "));
            client.sendEvent(defaultChannel, result);
        };

    }


    //se va el cliente y se registra la visita --> debe ser el mail. 
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
