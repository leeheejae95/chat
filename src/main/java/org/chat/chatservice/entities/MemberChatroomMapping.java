package org.chat.chatservice.entities;

import jakarta.persistence.*;

@Entity
public class MemberChatroomMapping {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_chat_room_id")
    @Id
    long id;

    @JoinColumn(name = "member_id")
    @ManyToOne
    Member member;

    @JoinColumn(name = "chatroom_id")
    @ManyToOne
    ChatRoom chatroom;

}
