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

        registry.addResourceHandler("/uploads/users/**")
                .addResourceLocations("file:" + uploadDir + "/users/");

        registry.addResourceHandler("/uploads/vehicles/**")
                .addResourceLocations("file:" + uploadDir + "/vehicles/");

        registry.addResourceHandler("/uploads/mission-vehicle-photos/**")
                .addResourceLocations("file:" + uploadDir + "/mission-vehicle-photos/");

    }

}