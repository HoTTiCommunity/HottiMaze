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
        registry.addResourceHandler("/static/imgs/**")
                .addResourceLocations("classpath:/static/imgs/");
      
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().toString();
      
        registry.addResourceHandler("/static/imgs/mazes/**")
                .addResourceLocations("file:" + absolutePath + "/")
                .addResourceLocations("classpath:/static/imgs/mazes/");
      
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
      
        registry.addResourceHandler("/imgs/mazes/**")
                .addResourceLocations("file:" + absolutePath + "/");
    }
}