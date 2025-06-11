package com.example.HottiMaze.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:src/main/resources/static/imgs/mazes}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 리소스 기본 설정 (JavaScript, CSS 포함)
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // JavaScript 파일 전용 핸들러
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        // CSS 파일 전용 핸들러
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        // 이미지 리소스 설정
        registry.addResourceHandler("/static/imgs/**")
                .addResourceLocations("classpath:/static/imgs/");

        // 미로 이미지 파일 경로 설정 (절대 경로)
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().toString();

        // /static/imgs/mazes/** 경로로 접근
        registry.addResourceHandler("/static/imgs/mazes/**")
                .addResourceLocations("file:" + absolutePath + "/")
                .addResourceLocations("classpath:/static/imgs/mazes/");

        // /imgs/mazes/** 경로로도 접근 가능하도록 추가
        registry.addResourceHandler("/imgs/mazes/**")
                .addResourceLocations("file:" + absolutePath + "/")
                .addResourceLocations("classpath:/static/imgs/mazes/");

        // 디버깅용 로그
        System.out.println("=== WebConfig 설정 ===");
        System.out.println("Upload Directory: " + uploadDir);
        System.out.println("Absolute Path: " + absolutePath);
        System.out.println("Static Resources: classpath:/static/");
        System.out.println("=====================");
    }
}