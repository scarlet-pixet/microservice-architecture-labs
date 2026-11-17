package org.example.analytics.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class FeignClientConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    Logger feignLogger() {
        return new OneLineFeignLogger();
    }
}
