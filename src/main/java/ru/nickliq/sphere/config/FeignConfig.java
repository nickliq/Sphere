package ru.nickliq.sphere.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

@Configuration
public class FeignConfig {

    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
