package com.iot.back.server.Config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPublisher {
    private static final String BROKER_URL = "tcp://localhost:1883";
    private static final String CLIENT_ID = "InternalPublisher";

    public void publishMessage(String topic, String content) {
        try {
            MqttClient client = new MqttClient(BROKER_URL, CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            // 브로커 연결
            client.connect(options);
            System.out.println("Connected to MQTT Broker.");

            // 메시지 생성 및 발행
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(1); // QoS 설정
            client.publish(topic, message);
            System.out.println("Message published: " + content);

            // 연결 종료
            client.disconnect();
            System.out.println("Disconnected from MQTT Broker.");

            client.close();
        } catch (MqttException e) {
            e.printStackTrace();
            System.err.println("Failed to publish message: " + e.getMessage());
        }
    }
}
