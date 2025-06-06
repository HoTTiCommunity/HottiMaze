package com.example.HottiMaze.config;

import com.example.HottiMaze.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest; // PathRequest를 사용하기 위함

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (개발 편의 목적, 운영 시 고려 필요)

                .authorizeHttpRequests(auth -> auth
                        // 특정 경로에 대한 접근 허용 설정 (WebSecurityCustomizer가 무시하는 경로는 여기서 제거)
                        .requestMatchers(
                                "/login", "/sign-up", // 로그인 및 회원가입 페이지
                                "/", // 메인 페이지
                                "/post/**", // 게시글 관련 (모든 사용자)
                                "/mazes/**", // 미로 관련 페이지 (모든 사용자)
                                "/api/categories/**" // 게시글 API (좋아요/싫어요 등)
                        ).permitAll()
                        .requestMatchers("/api/user/checkin").authenticated() // 출석체크 API는 인증된 사용자만 허용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )

                .formLogin(form -> form
                        .loginPage("/login")       // 커스텀 로그인 페이지 경로
                        .defaultSuccessUrl("/")    // 로그인 성공 시 기본 리다이렉트 URL
                        .failureUrl("/login?error")// 로그인 실패 시 리다이렉트될 URL
                        .permitAll() // 로그인 관련 페이지는 모두 접근 허용
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")       // 로그아웃 URL
                        .logoutSuccessUrl("/")      // 로그아웃 성공 시 리다이렉트될 URL
                        .permitAll() // 로그아웃 관련 경로는 모두 접근 허용
                );
        // H2 콘솔 사용을 위해 프레임 옵션 비활성화
        http.headers(headers ->
                headers.frameOptions(frame -> frame.disable())
        );

        return http.build();
    }

    // WebSecurityCustomizer 빈 (정적 리소스 무시 및 h2-console 무시)
    // 이 빈은 정적 리소스에 Spring Security 필터 체인이 적용되지 않도록 합니다.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                // 1. 일반적인 정적 리소스 경로 무시 (css, js, images 등)
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                // 2. H2 콘솔 경로 무시
                .requestMatchers("/h2-console/**")
                // 3. 업로드된 미로 이미지가 저장되는 경로를 명시적으로 무시
                //    application.properties의 file.upload-dir이 static/imgs/mazes이고
                //    WebConfig에서 /static/imgs/mazes/** 와 /imgs/mazes/** 로 매핑되므로
                //    여기에 해당하는 웹 경로를 모두 무시하도록 추가합니다.
                .requestMatchers("/static/imgs/mazes/**", "/imgs/mazes/**");
    }
}