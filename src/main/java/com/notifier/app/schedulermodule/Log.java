package com.notifier.app.schedulermodule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Log {

    private List<String> fileLog = new ArrayList<>();

    // mensajes testing desde el 01 al 10 de enero 
    public Log(){
        String[] mensajes = {
            "2023-01-01 14:30:00::Este es un nuevo libro de sistemas",
            "2023-01-02 14:30:00::Error en el sistema principal",
            "2023-01-03 14:30:00::Registro exitoso",
            "2023-01-04 14:30:00::Actualización de software disponible",
            "2023-01-05 14:30:00::Inicio de sesión de usuario",
            "2023-01-06 14:30:00::Cierre de sesión de usuario",
            "2023-01-07 14:30:00::Archivo borrado",
            "2023-01-08 14:30:00::Problema de red detectado",
            "2023-01-09 14:30:00::Respuesta del servidor",
            "2023-01-10 14:30:00::Nueva notificación"
        };

        for (int i = 0; i < 10; i++) {
            fileLog.add(mensajes[i]);
        }
    }


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
            return result;
        }
    }


    public void addNewEntry(String messageToSave) {
        synchronized(this.fileLog){
            fileLog.add(fileLog.size(), messageToSave); // Agrega al final 
        }

    }

}
