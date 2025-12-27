package org.chat.chatservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
