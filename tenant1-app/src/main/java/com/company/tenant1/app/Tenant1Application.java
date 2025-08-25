package com.company.tenant1.app;

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
@ComponentScan(basePackages = {"com.company.core", "com.company.tenant1"})
public class Tenant1Application implements CommandLineRunner {

    @Autowired
    private OrderProcessor orderProcessor;

    public static void main(String[] args) {
        SpringApplication.run(Tenant1Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== TENANT1 APPLICATION DEMO ===");
        
        User user = new User("user1", "John Doe", "john@example.com", "123-456-7890");
        
        System.out.println("\n--- Processing Regular Order ---");
        Order order1 = new Order("order1", "user1", new BigDecimal("1000"));
        order1.setUser(user);
        orderProcessor.process(order1);
        
        System.out.println("\n--- Processing High-Value Order (Audit Required) ---");
        Order order2 = new Order("order2", "user1", new BigDecimal("15000"));
        order2.setUser(user);
        orderProcessor.process(order2);
        
        System.out.println("\n=== DEMO COMPLETED ===\n");
    }
}