package ru.clevertec.comment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.clevertec.exceptionhandlerstarter.handler.NewsManagementSystemExceptionHandler;

@Configuration
public class AppConfig {

    @Bean
    @Profile("prod")
    public NewsManagementSystemExceptionHandler handler(){
        return new NewsManagementSystemExceptionHandler();
    }
}
