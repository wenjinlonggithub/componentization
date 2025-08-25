package com.company.core.config;

import com.company.core.processor.OrderProcessor;
import com.company.core.processor.UniversalProcessor;
import com.company.core.service.NotificationService;
// NOTE: Spring imports commented out for standalone compilation
// import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

/**
 * Core System Configuration
 * Provides default beans when no tenant-specific implementations are available
 * NOTE: Spring annotations commented out for standalone compilation
 */
// @Configuration
public class CoreSystemConfig {
    
    /**
     * Default order processor when no tenant customization is available
     */
    // @Bean
    // @ConditionalOnMissingBean(OrderProcessor.class)
    public UniversalProcessor universalProcessor() {
        return new UniversalProcessor();
    }
    
    /**
     * Default notification service
     */
    // @Bean
    // @ConditionalOnMissingBean(NotificationService.class)
    public NotificationService notificationService() {
        return new NotificationService();
    }
}