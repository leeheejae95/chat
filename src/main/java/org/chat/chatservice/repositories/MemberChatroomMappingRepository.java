package org.chat.chatservice.repositories;

import org.chat.chatservice.entities.ChatRoom;
import org.chat.chatservice.entities.Member;
import org.chat.chatservice.entities.MemberChatroomMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberChatroomMappingRepository extends JpaRepository<MemberChatroomMapping, Long> {

    Boolean existsByMemberIdAndChatroomId(Long memberId, long chatroomId);

    void deleteByMemberIdAndChatroomId(Long memberId, long chatroomId);

    List<MemberChatroomMapping> findAllByMemberId(Long memberId);

    Optional<MemberChatroomMapping> findByMemberIdAndChatroomId(Long memberId, Long currentChatroomId);
}
