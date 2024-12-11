package com.ioteducation.iotedu.web.iotDashboard;

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
import com.ioteducation.iotedu.common.GatheringClient;

@Component
public class IoTDataCheckSocketHandler extends TextWebSocketHandler {
    @Autowired
    private IoTDataCheckService ioTDataCheckService;

    private List<GatheringClient> gatheringClients = new ArrayList<>();;

    private final ObjectMapper objMapper = new ObjectMapper();

    @SuppressWarnings("null")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connected : " + session.getId());
    }

    @SuppressWarnings("null")
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
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

        if (jsonNode.has("TYPE") && jsonNode.has("DVC_ID")) {
            String conType = jsonNode.get("TYPE").asText();
            String dvcId = jsonNode.get("DVC_ID").asText();

            if (conType == null || conType.equals("") || dvcId == null || dvcId.equals("")) {
                session.sendMessage(new TextMessage("{'TYPE': 'CON', 'COMMENT' : 'EE'}"));
                return;
            }

            // 연결 신청일 경우 주기적으로 해당 세션에 데이터를 보냄
            if ("CON".equals(conType)) {
                GatheringClient newClient = new GatheringClient(session, dvcId, ioTDataCheckService);
                newClient.connect();
                newClient.sendGatheringData();
                if (newClient.isRunning()) {
                    gatheringClients.add(newClient);
                    session.sendMessage(new TextMessage("{'TYPE': 'CON', 'COMMENT' : 'CS'}"));
                } else {
                    newClient.disconnect();
                    gatheringClients.remove(newClient);
                    session.sendMessage(new TextMessage("{'TYPE': 'CON', 'COMMENT' : 'CE'}"));
                }
            } else if ("DIS".equals(conType)) {
                for(GatheringClient oldClient : gatheringClients) {
                    if (oldClient.getsession() == session) {
                        oldClient.disconnect();
                        gatheringClients.remove(oldClient);
                        session.sendMessage(new TextMessage("{'TYPE': 'CON', 'COMMENT' : 'DS'}"));
                        break;
                    }
                }
            }
        }
    }

    @SuppressWarnings("null")
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Disconnected: " + session.getId());
    }
}
