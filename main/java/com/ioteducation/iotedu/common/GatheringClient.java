package com.ioteducation.iotedu.common;

import java.util.Map;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.ioteducation.iotedu.config.WebSocketConfig;
import com.ioteducation.iotedu.web.iotDashboard.IoTDataCheckService;

public class GatheringClient {
    private WebSocketSession webSocketSession;
    private String dvcId;
    private IoTDataCheckService ioTDataCheckService;
    private volatile boolean running;

    public GatheringClient(WebSocketSession session, String dvcId, IoTDataCheckService ioTDataCheckService) {
        this.webSocketSession = session;
        this.dvcId = dvcId;
        this.ioTDataCheckService = ioTDataCheckService;
        this.running = false;
    }

    public WebSocketSession getsession() {
        return this.webSocketSession;
    }

    public void connect() {
        this.running = true;
    }

    public void disconnect() {
        this.running = false;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void sendGatheringData() {
        Runnable gatheringRunnable = () -> {
            try {
                while (this.running) {
                    Map<String, Object> gatheringData = ioTDataCheckService.getLastData(this.dvcId);
                    
                    if (gatheringData != null) {
                        String sendMsg = "{'TYPE': 'SNSR', 'COMMENT' : "
                                    + "{'DATA' : {'DETCT' : '" + gatheringData.get("detct") + "',"
                                    + "'TEMP' : '" + gatheringData.get("temp") + "',"
                                    + "'HUM' : '" + gatheringData.get("hum") +"' }}}";

                        if (this.webSocketSession != null) {
                            webSocketSession.sendMessage(new TextMessage(sendMsg));
                        }

                        // 3초 동안 대기
                        Thread.sleep(3000);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        };

        WebSocketConfig.executorService.submit(gatheringRunnable);
    }
}
