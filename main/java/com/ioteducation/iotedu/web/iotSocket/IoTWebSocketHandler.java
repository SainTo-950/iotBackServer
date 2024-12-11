package com.ioteducation.iotedu.web.iotSocket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioteducation.iotedu.common.ArduinoClient;
import com.ioteducation.iotedu.common.ArduinoSessionVo;

@Component
public class IoTWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private IoTWebSocketService ioTWebSocketService;
    
    private final ObjectMapper objMapper = new ObjectMapper();
    private List<ArduinoSessionVo> arduinoSessionList = new ArrayList<>();

    @SuppressWarnings("null")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connected : " + session.getId());
        ArduinoSessionVo vo = new ArduinoSessionVo();
        vo.setWebSocketSession(session);
        arduinoSessionList.add(vo);
    }

    @SuppressWarnings("null")
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received ============= " + message.getPayload());
        String payload = message.getPayload();
        // 메시지 JSON 변환
        JsonNode jsonNode = null;
        try {
            jsonNode = objMapper.readTree(payload);
        } catch (Exception e) {
            System.out.println("Json 형태 객체가 아닙니다 -- " + e.getMessage());
            session.sendMessage(new TextMessage("{'TYPE': 'CON', 'COMMENT' : 'EE'}"));
            return;
        }
        
        if (jsonNode.has("TYPE") && jsonNode.has("IP") && jsonNode.has("PORT")) {
            String conType = jsonNode.get("TYPE").asText();
            String ip = jsonNode.get("IP").asText();
            String port = jsonNode.get("PORT").asText();

            if (conType == null || conType.equals("")) {
                session.sendMessage(new TextMessage("{'TYPE': 'CON', 'COMMENT' : 'EE'}"));
                return;
            }

            ArduinoSessionVo tempSessionVo = null;
            for(ArduinoSessionVo ardSession : arduinoSessionList) {
                if (ardSession.getWebSocketSession() == session) {
                    tempSessionVo = ardSession;
                    break;
                }
            }
            if (tempSessionVo == null) {
                System.out.println("제대로 연결된 Session이 아닌 것 같습니다.. 확인이 필요합니다.");
                session.sendMessage(new TextMessage("{'TYPE': 'CON', 'COMMENT' : 'EE'}"));
                return;
            }

            // Connect
            if (conType.equals("CON")) {
                // 아두이노 1개는 하나의 연결만 수립할 수 있음.
                ArduinoClient chkArdClient = null;
                for(ArduinoSessionVo ardSession : arduinoSessionList) {
                    ArduinoClient chkClient = ardSession.getArduinoClient();
                    if (chkClient == null) {
                        continue;
                    }

                    if (chkClient.chkHost(ip, Integer.parseInt(port))) {
                        chkArdClient = chkClient;
                        break;
                    }
                }
                if (chkArdClient != null && chkArdClient.isRunning()) {
                    System.out.println("Arduino client is already running.");
                    session.sendMessage(new TextMessage("{'TYPE': 'CON', 'COMMENT' : 'CE'}"));
                    return;
                }

                ArduinoClient arduinoClient = new ArduinoClient(ip, Integer.parseInt(port), session, "ARD_01", ioTWebSocketService);
                tempSessionVo.setArduinoClient(arduinoClient);
                arduinoClient.connect();
                arduinoClient.sendMessage("Connected");
                arduinoClient.receiveMessages();
                System.out.println("Arduino client connect.");
                session.sendMessage(new TextMessage("{'TYPE': 'CON', 'COMMENT' : 'CS'}"));
            }
            // DisConnect
            else {
                if (tempSessionVo.getArduinoClient() == null || tempSessionVo.getArduinoClient().isRunning() == false) {
                    System.out.println("Arduino client is already suotdown.");
                    session.sendMessage(new TextMessage("{'TYPE': 'CON', 'COMMENT' : 'DE'}"));
                    return;
                }

                tempSessionVo.getArduinoClient().disconnect();
                System.out.println("Arduino client disconnect.");
                session.sendMessage(new TextMessage("{'TYPE': 'CON', 'COMMENT' : 'DS'}"));
            }
        }
    }

    @SuppressWarnings("null")
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Disconnected: " + session.getId());

        ArduinoSessionVo rmVo = null;
        for(ArduinoSessionVo ardSession : arduinoSessionList) {
            if (ardSession.getWebSocketSession() == session) {
                rmVo = ardSession;
                break;
            }
        }

        if (rmVo != null) {
            arduinoSessionList.remove(rmVo);
        }
    }
}
