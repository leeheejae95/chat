package org.chat.chatservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Order(2)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(request -> request.anyRequest().authenticated()) // 로그인 하지 않은 사용자는 접근 안됨
                .oauth2Login(Customizer.withDefaults()) // 로그인은 OAuth2방식으로 할거임
                .csrf(csrf -> csrf.disable()); // 요청시 토큰을 필요로 하기 떄문에 접근이 안돼서 끄기 

        return httpSecurity.build();
    }

    // 상담사일 경우 폼 로그인 사용
    @Order(1)
    @Bean
    public SecurityFilterChain consultantSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/consultants/**", "/login") // 시큐리티에서 제공해주는 폼 로그인 사용
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/consultants").permitAll()
                        .anyRequest().hasRole("CONSULTANT")) // 상담사만 접근가능
                .formLogin(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }
}
