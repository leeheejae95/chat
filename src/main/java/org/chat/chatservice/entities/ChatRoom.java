package org.chat.chatservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    @Id
    long id;

    String title;

    @OneToMany(mappedBy = "chatroom") // 연관관계 주인 설정
    Set<MemberChatroomMapping> memberChatroomMappingSet;

    LocalDateTime createdAt;

    @Transient
    Boolean hasNewMessage; // 새로운 메시지가 있는지 구분

    public void setHasNewMessage(Boolean hasNewMessage) {
        this.hasNewMessage = hasNewMessage;
    }

    public MemberChatroomMapping addMember(Member member) {
        if(this.memberChatroomMappingSet == null) {
            this.memberChatroomMappingSet = new HashSet<>();
        }

        MemberChatroomMapping memberChatroomMapping = MemberChatroomMapping.builder()
                .member(member)
                .chatroom(this)
                .build();

        this.memberChatroomMappingSet.add(memberChatroomMapping);

        return memberChatroomMapping;
    }
}
