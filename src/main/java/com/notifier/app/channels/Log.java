package com.notifier.app.channels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Log {

    private List<String> fileLog = new ArrayList<>();
    private Lock lock = new ReentrantLock();


    /*
     * Retorna la lista de sucesos a partir del ultimo hasta la fecha dada
     */
    public List<String> getMessagesUntilDate(Date date) {
        List<String> result = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String userLastConn = dateFormat.format(date);

        lock.lock();
        for (int i = fileLog.size() - 1; i >= 0; i--) {   
            String message = fileLog.get(i);
            String[] parts = fileLog.get(i).split("::", 2);
            String timeLog = parts[0];

            if(timeLog.compareTo(userLastConn) <= 0){
                break;
            }
            result.add(message);
        }
        lock.unlock();

        return result;
    }


    /*
     * Registro de nuevo suceso 
     */
    public void addNewEntry(String messageToSave) {
        lock.lock();
        fileLog.add(fileLog.size(), messageToSave); 
        lock.unlock();
    }

}
