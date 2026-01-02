package org.chat.chatservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chat.chatservice.dto.ChatRoomDto;
import org.chat.chatservice.entities.ChatRoom;
import org.chat.chatservice.entities.Member;
import org.chat.chatservice.entities.MemberChatroomMapping;
import org.chat.chatservice.entities.Message;
import org.chat.chatservice.repositories.ChatroomRepository;
import org.chat.chatservice.repositories.MemberChatroomMappingRepository;
import org.chat.chatservice.repositories.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService { // A라는 유저가 1번,2번,3번방에 들어갔다라는 정보를 기록하기 위한 객체.

    private final ChatroomRepository chatroomRepository;
    private final MemberChatroomMappingRepository memberChatroomMappingRepository;
    private final MessageRepository messageRepository;

    // 채팅방 생성
    public ChatRoom createChatRoom(Member member, String title) {
        ChatRoom chatRoom = ChatRoom.builder()
                .title(title)
                .createdAt(LocalDateTime.now())
                .build();
        chatRoom = chatroomRepository.save(chatRoom);

        // 방을 만든 사람을 매핑 테이블에 등록
        MemberChatroomMapping mapping = chatRoom.addMember(member);

        memberChatroomMappingRepository.save(mapping);

        return chatRoom; // 3. 저장된 방 객체 반환
    }

    // 다른사람이 만들어 놓은 채팅방 입장
    public Boolean joinChat(Member member, Long newChatroomId, Long currentChatroomId) {
        if(currentChatroomId != null) {
            updateLastCheckedAt(member, currentChatroomId); // currentChatroomId : 현재 채팅방ID
        }
        if(memberChatroomMappingRepository.existsByMemberIdAndChatroomId(member.getId(), newChatroomId)) {
            log.info("이미 참여한 채팅방입니다.");
            return false;
        }

        ChatRoom chatRoom = chatroomRepository.findById(newChatroomId).get();

        // 참여 안한 채팅방 명부에 올리기
        MemberChatroomMapping memberChatroomMapping = MemberChatroomMapping.builder()
                .member(member)
                .chatroom(chatRoom)
                .build();

        memberChatroomMappingRepository.save(memberChatroomMapping);

        return true;
    }

    public void updateLastCheckedAt(Member member, Long currentChatroomId) {
        MemberChatroomMapping memberChatroomMapping = memberChatroomMappingRepository.findByMemberIdAndChatroomId(member.getId(), currentChatroomId)
                .orElseThrow();

        memberChatroomMapping.updateLastCheckedAt();

        memberChatroomMappingRepository.save(memberChatroomMapping);
    }

    // 참여한방 나오기
    @Transactional
    public Boolean exitChatroom(Member member, Long chatroomId) {
        if(!memberChatroomMappingRepository.existsByMemberIdAndChatroomId(member.getId(), chatroomId)) {
            log.info("참여하지 않은 방입니다.");
            return false;
        }

        // 참여했음
        memberChatroomMappingRepository.deleteByMemberIdAndChatroomId(member.getId(), chatroomId);

        return true;
    }

    // 채팅방 목록 가져오기
    public List<ChatRoom> getChatroomlist(Member member) {
        List<MemberChatroomMapping> list = memberChatroomMappingRepository.findAllByMemberId(member.getId());

        return list.stream()
                .map(memberChatroomMapping -> {
                    ChatRoom chatroom = memberChatroomMapping.getChatroom();
                    chatroom.setHasNewMessage(messageRepository.existsByChatroomIdAndCreatedAtAfter(chatroom.getId(), memberChatroomMapping.getLastCheckedAt()));
                    return chatroom;
                })
                .toList();
    }

    public Message saveMessage(Member member, Long chatroomId ,String text) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).get(); // 채팅방 조회

        Message message = Message.builder()
                .text(text)
                .member(member)
                .chatroom(chatroom)
                .createdAt(LocalDateTime.now())
                .build();

        return messageRepository.save(message);
    }

    public List<Message> getMessageList(Long chatroomId) {
        return messageRepository.findAllByChatroomId(chatroomId);
    }

    @Transactional(readOnly = true)
    public ChatRoomDto getChatRoom(Long chatroomId) {
        ChatRoom chatroom = chatroomRepository.findById(chatroomId).get();

        return ChatRoomDto.from(chatroom);
    }
}
