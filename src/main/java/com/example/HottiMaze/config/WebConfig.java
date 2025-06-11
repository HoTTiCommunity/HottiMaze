package com.example.HottiMaze.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// import java.nio.file.Paths; // 로컬 파일 처리 안 하는 경우 필요 없음

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // @Value("${file.upload-dir:src/main/resources/static/imgs/mazes}")
    // private String uploadDir; // GCS 호스팅 이미지의 경우 더 이상 필요 없음

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // CSS, JS 및 모든 로컬 이미지(예: default-maze.png)에 대한 정적 리소스
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        // 미로 이미지가 아닌 로컬에 정적 이미지가 있다면 유지
        registry.addResourceHandler("/imgs/**")
                .addResourceLocations("classpath:/static/imgs/");

        // 미로 이미지는 GCS URL에서 직접 제공되므로, 명시적인 리소스 핸들러는 필요하지 않습니다.
        // (간단한 제공을 위해 프록시하는 것은 권장되지 않습니다.)

        System.out.println("=== WebConfig 설정 (GCS 통합) ===");
        // System.out.println("Upload Directory (로컬, 더 이상 기본 아님): " + uploadDir); // 제거 또는 주석 처리 가능
        System.out.println("정적 리소스: classpath:/static/");
        System.out.println("=====================");
    }
}