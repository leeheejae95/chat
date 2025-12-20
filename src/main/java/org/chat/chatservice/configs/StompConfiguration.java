package org.chat.chatservice.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker // WebSocket을 pub / sub으로 관리하겠다
@Configuration
public class StompConfiguration implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { // 클라이언트가 어떤 URL로 WebSocket 연결을 시도할지 결정
        registry.addEndpoint("/stomp/chat");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) { // 메시지 브로커를 설정하여 메시지를 구독자에게 전달하거나 컨트롤러로 보낸다.
        registry.setApplicationDestinationPrefixes("/pub"); // 쿨라이언트가 서버로 보내는 모든 메시지는 무조건 /pub으로 시작해야돼! + MessageMapping으로된 상세주소 함께 처리
        registry.enableSimpleBroker("/sub"); // 구독
    }
}
