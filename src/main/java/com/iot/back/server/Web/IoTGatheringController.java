package com.iot.back.server.Web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    
}