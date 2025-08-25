package com.company.tenant2.app;

import com.company.core.model.Order;
import com.company.core.model.User;
import com.company.core.processor.OrderProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;

@SpringBootApplication
@ComponentScan(basePackages = {"com.company.core", "com.company.tenant2"})
public class Tenant2Application implements CommandLineRunner {

    @Autowired
    private OrderProcessor orderProcessor;

    public static void main(String[] args) {
        SpringApplication.run(Tenant2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== TENANT2 ENTERPRISE APPLICATION DEMO ===");
        
        User user = new User("user2", "Jane Smith", "jane@enterprise.com", "987-654-3210");
        
        System.out.println("\n--- Processing Small Enterprise Order ---");
        Order order1 = new Order("order1", "user2", new BigDecimal("3000"));
        order1.setUser(user);
        orderProcessor.process(order1);
        
        System.out.println("\n--- Processing Large Enterprise Order (Complex Validation) ---");
        Order order2 = new Order("order2", "user2", new BigDecimal("25000"));
        order2.setUser(user);
        orderProcessor.process(order2);
        
        System.out.println("\n=== ENTERPRISE DEMO COMPLETED ===\n");
    }
}