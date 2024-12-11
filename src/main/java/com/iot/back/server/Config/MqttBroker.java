package com.iot.back.server.Config;

import java.util.Properties;

import org.springframework.stereotype.Component;

import io.moquette.broker.Server;
import io.moquette.broker.config.MemoryConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class MqttBroker {
    private Server mqttBrokerServer;
    
    @PostConstruct
    public void startBroker() {
        try {
            mqttBrokerServer = new Server();
            Properties configProps = new Properties();
            configProps.setProperty("port", "1883");
            configProps.setProperty("host", "0.0.0.0");
            configProps.setProperty("allow_anonymous", "true");
            configProps.setProperty("websocket_port", "9091");

            mqttBrokerServer.startServer(new MemoryConfig(configProps));

            System.out.println("MQTT Broker Started");
        } catch (Exception e) {
            throw new RuntimeException("Failed to start MQTT Broker", e);
        }        
    }

    @PreDestroy
    public void stopBroker() {
        if (this.mqttBrokerServer != null) {
            mqttBrokerServer.stopServer();
            System.out.println("MQTT Broker Stopped");
        }
    }
}
