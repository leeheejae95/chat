package org.chat.chatservice.dto;

import org.chat.chatservice.entities.ChatRoom;

import java.time.LocalDateTime;

public record ChatRoomDto(
        Long id,
        String title,
        Integer memberCount,
        LocalDateTime createdAt,
        Boolean hasNewMessage
) {
    public static ChatRoomDto from(ChatRoom chatRoom) {
        return new ChatRoomDto(chatRoom.getId(), chatRoom.getTitle(), chatRoom.getMemberChatroomMappingSet().size() ,chatRoom.getCreatedAt(), chatRoom.getHasNewMessage());
    }

}
