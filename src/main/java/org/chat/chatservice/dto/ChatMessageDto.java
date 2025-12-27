package org.chat.chatservice.dto;

public record ChatMessageDto ( // 메세지 데이터 전달용
        String sender,
        String message
) {

}
