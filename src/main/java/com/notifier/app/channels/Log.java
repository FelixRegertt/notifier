package com.notifier.app.channels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Log {

    private List<String> fileLog = new ArrayList<>();


    /*
     * Retorna la lista de sucesos a partir del ultimo hasta la fecha dada
     */
    public List<String> getMessagesUntilDate(Date date) {
        List<String> result = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String userLastConn = dateFormat.format(date);

        synchronized(this.fileLog){
            for (int i = fileLog.size() - 1; i >= 0; i--) {   
                String message = fileLog.get(i);
                String[] parts = fileLog.get(i).split("::", 2);
                String timeLog = parts[0];

                if(timeLog.compareTo(userLastConn) <= 0){
                break;
                }
                result.add(message);
            }
        }
        return result;
    }


    /*
     * Registro de nuevo suceso 
     */
    public void addNewEntry(String messageToSave) {
        synchronized(this.fileLog){
        fileLog.add(fileLog.size(), messageToSave); 
        }
    }

}
