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
        // 기존 classpath 이미지 리소스
        registry.addResourceHandler("/static/imgs/**")
                .addResourceLocations("classpath:/static/imgs/");

        // 업로드된 파일들을 위한 외부 경로
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().toString();
        registry.addResourceHandler("/static/imgs/mazes/**")
                .addResourceLocations("file:" + absolutePath + "/")
                .addResourceLocations("classpath:/static/imgs/mazes/");

        // 전체 static 리소스
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // 추가: 루트 경로에서도 접근 가능하도록
        registry.addResourceHandler("/imgs/mazes/**")
                .addResourceLocations("file:" + absolutePath + "/");
    }
}