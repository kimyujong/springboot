package com.likeyou.safety.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트가 구독할 prefix (서버 → 클라이언트)
        config.enableSimpleBroker("/topic");
        // 클라이언트가 메시지 보낼 prefix (클라이언트 → 서버)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 연결 엔드포인트
        registry.addEndpoint("/ws")
                .setAllowedOrigins(
                    "http://localhost:3000", 
                    "http://localhost:5173",
                    "https://likeyousafety.cloud",      // [추가] 프론트엔드 도메인
                    "https://api.likeyousafety.cloud"   // [추가] 백엔드 도메인 (혹시 몰라 추가)
                )
                .withSockJS();
    }
}