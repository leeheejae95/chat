package org.chat.chatservice.repositories;

import org.chat.chatservice.entities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<ChatRoom, Long> {


}
