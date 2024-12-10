package com.sanmarcos.promecal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
/*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }*/
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")  // Aplica CORS a todos los endpoints
            .allowedOrigins("https://promecal-frontend-5rqq.vercel.app/")  // Especifica el origen aquí
            .allowedOriginPatterns("*")  // Permite cualquier origen, como en Postman
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true);
}
}
