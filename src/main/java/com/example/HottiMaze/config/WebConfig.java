package com.example.HottiMaze.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:src/main/resources/static/imgs/mazes}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 기존 이미지 리소스
        registry.addResourceHandler("/static/imgs/**")
                .addResourceLocations("classpath:/static/imgs/");

        // 미로 관련 이미지들 (메인 이미지, 문제 이미지 등)
        registry.addResourceHandler("/static/imgs/mazes/**")
                .addResourceLocations("classpath:/static/imgs/mazes/");

        // 업로드된 파일들을 위한 외부 경로 (개발 환경용)
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");

        // 전체 static 리소스
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}