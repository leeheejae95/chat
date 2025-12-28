package org.chat.chatservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chat.chatservice.dto.ChatMessageDto;
import org.chat.chatservice.dto.ChatRoomDto;
import org.chat.chatservice.entities.ChatRoom;
import org.chat.chatservice.entities.Message;
import org.chat.chatservice.services.ChatService;
import org.chat.chatservice.vo.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chats")
@RestController
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/create")
    public ChatRoomDto creatChatroom(@AuthenticationPrincipal CustomOAuth2User oAuth2User, @RequestParam String title) {
        ChatRoom chatRoom = chatService.createChatRoom(oAuth2User.getMember(), title);
        return ChatRoomDto.from(chatRoom);
    }

    @PostMapping("/join/{chatroomId}")
    public Boolean joinChatroom(@AuthenticationPrincipal CustomOAuth2User oAuth2User, @PathVariable Long chatroomId) {
        return chatService.joinChat(oAuth2User.getMember(), chatroomId);
    }

    @DeleteMapping("/exit/{chatroomId}")
    public Boolean exitChatroom(@AuthenticationPrincipal CustomOAuth2User oAuth2User, @PathVariable Long chatroomId) {
        return chatService.exitChatroom(oAuth2User.getMember(), chatroomId);
    }

    @GetMapping("/chatList/{memberId}")
    public List<ChatRoomDto> chatroomList(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        List<ChatRoom> chatRoom = chatService.getChatroomlist(oAuth2User.getMember());
        return chatRoom.stream()
                .map(ChatRoomDto::from)
                .toList();
    }

    @GetMapping("/{chatroomId}/message")
    public List<ChatMessageDto> getMessageList(@PathVariable Long chatroomId) {
        List<Message> messages = chatService.getMessageList(chatroomId);
        return messages.stream()
                .map(message -> new ChatMessageDto(message.getMember().getNickName(), message.getText()))
                .toList();
    }
}
