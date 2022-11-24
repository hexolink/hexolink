package com.auth.config;

import com.auth.util.JacksonObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {
    @Bean
    public JacksonObjectMapper customObjectMapper() {
        return new JacksonObjectMapper();
    }
}
