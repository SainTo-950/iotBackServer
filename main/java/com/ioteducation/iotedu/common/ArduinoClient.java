package com.ioteducation.iotedu.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.ioteducation.iotedu.config.WebSocketConfig;
import com.ioteducation.iotedu.web.iotSocket.IoTWebSocketService;

public class ArduinoClient {
    
    private String host;
    private int port;
    private Socket socket;
    private BufferedReader reader;
    private OutputStream writer;
    private volatile boolean running;
    private WebSocketSession webSocketSession;
    private String dvcId;
    private IoTWebSocketService ioTWebSocketService;
    

    public ArduinoClient(String host, int port, WebSocketSession webSocketSession, String dvcId, IoTWebSocketService ioTWebSocketService) {
        this.host = host;
        this.port = port;
        this.webSocketSession = webSocketSession;
        this.dvcId = dvcId;
        this.ioTWebSocketService = ioTWebSocketService;
    }

    public boolean chkHost(String host, int port) {
        if (this.host.equals(host) && this.port == port) {
            return true;
        }

        return false;
    }

    public void connect() throws Exception {
        socket = new Socket(host, port);
        socket.setSoTimeout(180000);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = socket.getOutputStream();
        running = true;
        System.out.println("Connected to " + host + " on port " + port);
    }

    public void sendMessage(String message) throws Exception {
        writer.write((message + "\n").getBytes());
        writer.flush();
    }

    public String readMessage() throws Exception {
        return reader.readLine();
    }

    public void disconnect() throws Exception {
        running = false;
        reader.close();
        writer.close();
        socket.close();
        System.out.println("Disconnected from " + host);
    }

    public void receiveMessages() {
        Runnable iotRunnable = () -> {
            try {
                while (running) {
                    String message = reader.readLine();
                    if (message != null && !message.equals("wait")) {
                        System.out.println("Received: " + message);
                        // IO Data Insert
                        ioTWebSocketService.insertIoData(dvcId, message);
                        if (this.webSocketSession != null) {
                            webSocketSession.sendMessage(new TextMessage("{'TYPE': 'SNSR', 'COMMENT' : " + message + "}"));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (this.webSocketSession != null) {
                        this.webSocketSession.sendMessage(new TextMessage("{'TYPE': 'CON', 'COMMENT' : 'DS'}"));
                    }
                    disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        WebSocketConfig.executorService.submit(iotRunnable);
    }

    public boolean isRunning() {
        return running;
    }
}
