package org.chat.chatservice.repositories;

import org.chat.chatservice.entities.MemberChatroomMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<MemberChatroomMapping, Long> {


}
