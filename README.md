# Maven

## Configuración del Proyecto

Antes de ejecutar la aplicación, debes seguir estos pasos de configuración del proyecto:

1. **Limpieza del Proyecto**: Comando para limpiar cualquier construcción anterior y eliminar archivos temporales:

    ```
    mvn clean
    ```

2. **Compilación del Proyecto**: Comando para compilar el proyecto y generar los archivos ejecutables:

    ```
    mvn compile
    ```

## Ejecución de la Aplicación

Para ejecutar la aplicación, sigue estos pasos:

1. **Iniciar la Aplicación**: Iniciar la aplicación Spring Boot:

    ```
    mvn spring-boot:run
    ```

2. **Acceso a la Aplicación**: La aplicación estará disponible en el puerto 8080.

## Descripción del Proyecto

Este proyecto es una aplicación Spring Boot que utiliza WebSockets para mantener a los clientes suscritos a canales de notificaciones

- **Puerto de Escucha**: La aplicación escucha en el puerto 8080 por conexiones entrantes.

- **Comunicación WebSocket**: Utiliza WebSockets para establecer conexiones bidireccionales con los clientes. Los clientes pueden enviar y recibir mensajes de manera asíncrona.

- **Lista de Clientes**: La aplicación mantiene una lista de clientes conectados que están escuchando notificaciones.

- **Notificaciones**: Los mensajes entrantes se colocan en una cola y los hilos consumidores los envían a los clientes conectados.


## Cómo Comunicarse con el Servidor

Puedes comunicarte con el servidor WebSocket utilizando solicitudes WebSocket. A continuación, se describe cómo realizar una solicitud al servidor, junto con un ejemplo utilizando Postman:

### Realizar una Solicitud al Servidor

- **URL del Servidor WebSocket**: `ws://localhost:8080?userid=juan%40gmail.com`

- **Parametros**: `userid=mail_ejemplo%40gmail.com` (escapa el @)

- **Método**: WebSocket, donde el evento por el que se escucha es `default` 

- **Mensaje WebSocket**: El mensaje WebSocket debe estar en el formato JSON y debe contener los siguientes campos:

  ```json
  {
      "type": "CLIENT",
      "message": "nuevo mensaje"
  }