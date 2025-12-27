package org.chat.chatservice.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
public class ChatRoom {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    @Id
    long id;

    String title;

    @OneToMany(mappedBy = "chatroom") // 연관관계 주인 설정
    Set<MemberChatroomMapping> memberChatroomMappingSet;
    LocalDateTime createdAt;


}
