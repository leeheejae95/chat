package org.chat.chatservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.chat.chatservice.dto.ChatMessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.*;

@Controller
@Slf4j
public class StompChatController {

    @MessageMapping("/chats") // /pub/chats
    @SendTo("/sub/chats") // /sub/chats을 구독하고 있는 클라이언트에게 메시지 전달
    public ChatMessageDto handlerMessage(@AuthenticationPrincipal Principal principal, @Payload Map<String, String> payload) { // 인증된 유저 정보를 파라미터로 받아옴
        log.info("{} sent {}", principal.getName(), payload);

        return new ChatMessageDto(principal.getName(), payload.get("message"));
    }
}
