package com.likeyou.safety.global.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DashboardWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 대시보드로 실시간 이벤트 전송
     */
    public void sendDashboardUpdate(Object data) {
        log.info("[WebSocket] 대시보드 업데이트 전송");
        messagingTemplate.convertAndSend("/topic/dashboard", data);
    }

    /**
     * 낙상 감지 알림 전송
     */
    public void sendFallAlert(Object fallEvent) {
        log.info("[WebSocket] 낙상 감지 알림 전송");
        messagingTemplate.convertAndSend("/topic/fall", fallEvent);
    }

    /**
     * 혼잡도 알림 전송
     */
    public void sendCrowdAlert(Object crowdEvent) {
        log.info("[WebSocket] 혼잡도 알림 전송");
        messagingTemplate.convertAndSend("/topic/crowd", crowdEvent);
    }

    /**
     * SOS 알림 전송
     */
    public void sendSOSAlert(Object sosEvent) {
        log.info("[WebSocket] SOS 알림 전송");
        messagingTemplate.convertAndSend("/topic/sos", sosEvent);
    }
}