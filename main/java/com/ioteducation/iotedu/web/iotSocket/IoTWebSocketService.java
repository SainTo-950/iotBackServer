package com.ioteducation.iotedu.web.iotSocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class IoTWebSocketService {
    @Autowired
    private IoTWebSocktDAO ioTWebSocktDAO;

    private final ObjectMapper objMapper = new ObjectMapper();

    public int insertIoData(String dvcId, String value) {
        int rtnValue = 0;

        // 메시지 JSON 변환
        JsonNode jsonNode = null;
        try {
            jsonNode = objMapper.readTree(value);
        } catch (Exception e) {
            System.out.println("Json 형태 객체가 아닙니다 -- " + e.getMessage());
            rtnValue = -1;
            return rtnValue;
        }

        if (jsonNode.has("TH")) {
            JsonNode tempHumNode = jsonNode.get("TH");
            Map<String, Object> vo = new HashMap<>();

            vo.put("dvc_id", dvcId);
            vo.put("temp", tempHumNode.get("TEMP").asText());
            vo.put("hum", tempHumNode.get("HUM").asText());

            rtnValue = this.ioTWebSocktDAO.insertIoData(vo);
        }

        return rtnValue;
    }
}
