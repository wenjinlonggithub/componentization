package com.company.tenant1.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "tenant.id", havingValue = "tenant1")
@ComponentScan(basePackages = {"com.company.tenant1", "com.company.core"})
public class Tenant1Config {
}