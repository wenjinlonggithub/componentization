package com.company.tenant2.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "tenant.id", havingValue = "tenant2")
@ComponentScan(basePackages = {"com.company.tenant2", "com.company.core"})
public class Tenant2Config {
}