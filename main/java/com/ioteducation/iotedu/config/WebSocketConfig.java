package com.ioteducation.iotedu.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.ioteducation.iotedu.web.iotDashboard.IoTDataCheckSocketHandler;
import com.ioteducation.iotedu.web.iotSocket.IoTWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final IoTWebSocketHandler webSocketHandler;
    private final IoTDataCheckSocketHandler ioTDataCheckSocketHandler;
    public static final ExecutorService executorService = Executors.newFixedThreadPool(100);

    public WebSocketConfig(IoTWebSocketHandler webSocketHandler, IoTDataCheckSocketHandler ioTDataCheckSocketHandler) {
        this.webSocketHandler = webSocketHandler;
        this.ioTDataCheckSocketHandler = ioTDataCheckSocketHandler;
    }

    @SuppressWarnings("null")
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws").setAllowedOrigins("*");
        registry.addHandler(ioTDataCheckSocketHandler, "/gathering").setAllowedOrigins("*");
    }
}
