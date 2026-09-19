package com.mvpnest.fleetmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ensure path ends with a slash
        String path = uploadDir.endsWith("/") ? uploadDir : uploadDir + "/";

        // Map root /uploads/** to cover all subfolders (users, vehicles, documents, etc.)
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + path);
    }

}