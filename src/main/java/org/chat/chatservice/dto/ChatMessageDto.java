package org.chat.chatservice.dto;

public record ChatMessageDto ( // 메세지 데이터 전달용
        String sender, // 보내는 사람
        String message // 메시지 내용
) {

}
