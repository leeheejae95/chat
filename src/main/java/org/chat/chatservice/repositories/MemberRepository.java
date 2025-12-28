package org.chat.chatservice.repositories;

import org.chat.chatservice.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email); // 이메일로 회원 정보 조회

    Optional<Member> findByName(String name);

}
