package com.example.HottiMaze.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 기존 이미지 리소스
        registry.addResourceHandler("/static/imgs/**")
                .addResourceLocations("classpath:/static/imgs/");

        // 미로 관련 이미지들 (메인 이미지, 문제 이미지 등)
        registry.addResourceHandler("/static/imgs/mazes/**")
                .addResourceLocations("classpath:/static/imgs/mazes/");

        // 전체 static 리소스
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}