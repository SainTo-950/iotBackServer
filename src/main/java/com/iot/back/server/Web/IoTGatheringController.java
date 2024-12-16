package com.iot.back.server.Web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.back.server.Config.MqttPublisher;

@RestController
public class IoTGatheringController {
    @GetMapping("/connectionTest")
    public String connectionTest(@RequestParam String Msg) {
        return "Connection Success!\nMessage is " + Msg;
    }

    @GetMapping("/gatheringData")
    public String gatheringData(@RequestParam String DVC_ID,
                                @RequestParam String DETCT,
                                @RequestParam String TEMP,
                                @RequestParam String HUM) {
        return "Received Message " + DVC_ID + " / " + DETCT + " / " + TEMP + " / " + HUM;
    }
    
    @GetMapping("/startMqttPublish")
    public String getMethodName() {
        MqttPublisher publisher = new MqttPublisher();

        // MQTT 메시지 발행
        publisher.publishMessage("test/topic", "Hello from internal publisher!");

        return "MQTT Message Publish done"; 
    }
    
}