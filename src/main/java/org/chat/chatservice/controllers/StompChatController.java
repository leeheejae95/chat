package org.chat.chatservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chat.chatservice.dto.ChatMessageDto;
import org.chat.chatservice.entities.Message;
import org.chat.chatservice.services.ChatService;
import org.chat.chatservice.vo.CustomOAuth2User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class StompChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chats/{chatroomId}") // /pub/chats
    @SendTo("/sub/chats/{chatroomId}") // /sub/chats을 구독하고 있는 클라이언트에게 메시지 전달
    public ChatMessageDto handlerMessage(Principal principal, @DestinationVariable Long chatroomId, @Payload Map<String, String> payload) { // 인증된 유저 정보를 파라미터로 받아옴
        log.info("{} sent {} in {}", principal.getName(), payload, chatroomId);

        CustomOAuth2User oAuth2User = (CustomOAuth2User) ((AbstractAuthenticationToken) principal).getPrincipal();
        Message message = chatService.saveMessage(oAuth2User.getMember(), chatroomId, payload.get("message"));
        simpMessagingTemplate.convertAndSend("/sub/chats/news", chatroomId); // 채팅방에 새 소식이 왔다라는걸 알림
        return new ChatMessageDto(principal.getName(), payload.get("message"));
    }
}
