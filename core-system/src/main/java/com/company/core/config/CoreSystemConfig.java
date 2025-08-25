package com.company.core.config;

import com.company.core.processor.DefaultOrderProcessor;
import com.company.core.processor.OrderProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreSystemConfig {

    @Bean
    @ConditionalOnMissingBean(OrderProcessor.class)
    public OrderProcessor defaultOrderProcessor() {
        return new DefaultOrderProcessor();
    }
}