package org.chat.chatservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(request -> request.anyRequest().authenticated()) // 로그인 하지 않은 사용자는 접근 안됨
                .oauth2Login(Customizer.withDefaults()) // 로그인은 OAuth2방식으로 할거임
                .csrf(csrf -> csrf.disable()); // 요청시 토큰을 필요로 하기 떄문에 접근이 안돼서 끄기 

        return httpSecurity.build();
    }
}
