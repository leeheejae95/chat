package org.chat.chatservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.chat.chatservice.enums.Gender;

import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor // 빈생성자 추가
@AllArgsConstructor // 생성자 추가
@Getter
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @Id
    Long id;
    String email;
    String nickName;
    String name;
    @Enumerated(EnumType.STRING)
    Gender gender;
    String phoneNumber;
    LocalDate birthDay;
    String role;
}
