package ru.nickliq.sphere.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class FeignConfig {

    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}