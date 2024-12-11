package com.ioteducation.iotedu.common;

import org.springframework.web.socket.WebSocketSession;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArduinoSessionVo {
    private WebSocketSession webSocketSession;
    private ArduinoClient arduinoClient;
}
