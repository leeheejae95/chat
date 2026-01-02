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
@RequestMapping("/chats")
@RestController
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatRoomDto createChatroom(@AuthenticationPrincipal CustomOAuth2User user, @RequestParam String title) {
        ChatRoom chatroom = chatService.createChatRoom(user.getMember(), title);

        return ChatRoomDto.from(chatroom);
    }

    @PostMapping("/{chatroomId}")
    public Boolean joinChatroom(@AuthenticationPrincipal CustomOAuth2User user, @PathVariable Long chatroomId, @RequestParam(required = false) Long currentChatroomId) {
        return chatService.joinChat(user.getMember(), chatroomId, currentChatroomId);
    }

    @DeleteMapping("/{chatroomId}")
    public Boolean leaveChatroom(@AuthenticationPrincipal CustomOAuth2User user, @PathVariable Long chatroomId) {
        return chatService.exitChatroom(user.getMember(), chatroomId);
    }

    @GetMapping
    public List<ChatRoomDto> getChatroomList(@AuthenticationPrincipal CustomOAuth2User user) {
        List<ChatRoom> chatroomList = chatService.getChatroomlist(user.getMember());

        return chatroomList.stream()
                .map(ChatRoomDto::from)
                .toList();
    }

    @GetMapping("/{chatroomId}/messages")
    public List<ChatMessageDto> getMessageList(@PathVariable Long chatroomId) {
        List<Message> messageList = chatService.getMessageList(chatroomId);
        return messageList.stream()
                .map(message -> new ChatMessageDto(message.getMember().getNickName(), message.getText()))
                .toList();
    }
}

