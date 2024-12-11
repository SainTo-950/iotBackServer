package com.ioteducation.iotedu.web.iotDashboard;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class IoTDataCheckController {
    @Autowired
    private IoTDataCheckService ioTDataCheckService;

    @GetMapping("/connectionTest")
    public String getMethodName(@RequestParam String Msg) {
        return "Connection Success!";
    }
    
    @GetMapping("/gatheringData")
    public String gatheringData(@RequestParam String DVC_ID,
                                @RequestParam String DETCT,
                                @RequestParam String TEMP,
                                @RequestParam String HUM) {
        String rtnVal = "";
        System.out.println("Data Received..........");
        System.out.println("DVC_ID : " + DVC_ID + ", DETCT : " + DETCT + ", TEMP : " + TEMP + ", HUM : " + HUM);

        Map<String, Object> vo = new HashMap<>();
        vo.put("dvc_id", DVC_ID);
        vo.put("detct", "1".equals(DETCT) ? "Y" : "N");
        vo.put("temp", TEMP);
        vo.put("hum", HUM);

        int rtn = ioTDataCheckService.insertGatheringData(vo);

        if (rtn > -1) {
            rtnVal = "Gathering Success!";
        } else {
            rtnVal = "Gathering Fail!";
        }

        return rtnVal;
    }

    @GetMapping("/ledOnOff")
    public String ledOnOff(@RequestParam String ip,
                           @RequestParam String port,
                           @RequestParam String order) {
        this.ioTDataCheckService.requestArduino(ip, port, order);
        return new String();
    }
    
}