package com.example.HottiMaze.config;

import com.example.HottiMaze.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/maze-hints/**").authenticated()
                        .requestMatchers("/api/mazes/*/complete").authenticated()
                        .requestMatchers("/mazes/upload").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/mazes/*/delete").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/user/checkin").authenticated()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers(
                                "/login", "/sign-up", "/",
                                "/post/**", "/mazes/*/quiz", "/mazes/*/",
                                "/api/categories/posts/**",
                                "/api/mazes/*/vote-stats",
                                "/debug/**" // 디버깅용 경로 추가
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        http.headers(headers ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/h2-console/**")
                // 정적 리소스 경로 확장
                .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/imgs/**")
                .requestMatchers("/static/imgs/mazes/**", "/imgs/mazes/**")
                // 추가적인 정적 리소스 경로
                .requestMatchers("/favicon.ico", "/robots.txt");
    }
}