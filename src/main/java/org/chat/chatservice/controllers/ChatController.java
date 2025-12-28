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

    @PostMapping(value = "/create", name = "채팅방 생성")
    public ChatRoomDto creatChatroom(@AuthenticationPrincipal CustomOAuth2User oAuth2User, @RequestParam String title) {
        ChatRoom chatRoom = chatService.createChatRoom(oAuth2User.getMember(), title);
        return ChatRoomDto.from(chatRoom);
    }

    @PostMapping(value = "/join/{chatroomId}", name = "채팅방 참여")
    public Boolean joinChatroom(@AuthenticationPrincipal CustomOAuth2User oAuth2User, @PathVariable Long chatroomId, @RequestParam(required = false) Long currentChatroomId) {
        return chatService.joinChat(oAuth2User.getMember(), chatroomId, currentChatroomId);
    }

    @DeleteMapping(value = "/exit/{chatroomId}", name = "채팅방 퇴장")
    public Boolean exitChatroom(@AuthenticationPrincipal CustomOAuth2User oAuth2User, @PathVariable Long chatroomId) {
        return chatService.exitChatroom(oAuth2User.getMember(), chatroomId);
    }

    @GetMapping(value = "/chatList/{memberId}", name = "채팅방 목록 조회")
    public List<ChatRoomDto> chatroomList(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        List<ChatRoom> chatRoom = chatService.getChatroomlist(oAuth2User.getMember());
        return chatRoom.stream()
                .map(ChatRoomDto::from)
                .toList();
    }

    @GetMapping(value = "/{chatroomId}/message", name = "채팅 목록 조회")
    public List<ChatMessageDto> getMessageList(@PathVariable Long chatroomId) {
        List<Message> messages = chatService.getMessageList(chatroomId);
        return messages.stream()
                .map(message -> new ChatMessageDto(message.getMember().getNickName(), message.getText()))
                .toList();
    }
}
