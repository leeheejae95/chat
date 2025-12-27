package org.chat.chatservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chat.chatservice.entities.ChatRoom;
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
    public ChatRoom creatChatroom(@AuthenticationPrincipal CustomOAuth2User oAuth2User, @RequestParam String title) {
        return chatService.createChatRoom(oAuth2User.getMember(), title);
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
    public List<ChatRoom> chatroomList(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        return chatService.getChatroomlist(oAuth2User.getMember());
    }

}
