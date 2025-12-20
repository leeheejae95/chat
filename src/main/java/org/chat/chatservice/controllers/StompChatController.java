package org.chat.chatservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class StompChatController {

    @MessageMapping("/chats") // /pub/chats
    @SendTo("/sub/chats") // /sub/chats을 구독하고 있는 클라이언트에게 메시지 전달
    public String handlerMessage(@Payload String message) {
        log.info("{} received", message);

        return message;
    }
}
