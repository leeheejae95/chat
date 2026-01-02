package org.chat.chatservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chat.chatservice.dto.ChatRoomDto;
import org.chat.chatservice.dto.MemberDto;
import org.chat.chatservice.entities.ChatRoom;
import org.chat.chatservice.entities.Member;
import org.chat.chatservice.enums.MemberRole;
import org.chat.chatservice.repositories.ChatroomRepository;
import org.chat.chatservice.repositories.MemberRepository;
import org.chat.chatservice.vo.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsultantService implements UserDetailsService {

    private final PasswordEncoder  passwordEncoder;
    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByName(username).get();

        if (MemberRole.fromCode(member.getRole()) != MemberRole.CONSULTANT) {
            throw new UsernameNotFoundException("상담사 권한이 없는 사용자입니다."); // 시큐리티 컨텍스트에 맞춰 예외 선택
        }

        return new CustomUserDetails(member, null);
    }

    public MemberDto saveMember(MemberDto memberDto) {
        Member member = MemberDto.to(memberDto); // DTO객체를 엔티티에 저장
        member.updatePassword(memberDto.password(), memberDto.confirmedPassword(), passwordEncoder);
        member = memberRepository.save(member);

        return MemberDto.from(member); // 엔티티 객체를 DTO로 변환해 리턴
    }

    public List<ChatRoomDto> getAllChatrooms() {
        List<ChatRoom> chatRoomList = chatroomRepository.findAll();

        return chatRoomList.stream()
                .map(ChatRoomDto::from)
                .toList();
    }
}
