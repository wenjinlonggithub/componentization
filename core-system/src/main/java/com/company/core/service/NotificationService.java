package com.company.core.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    public void sendEmail(String to, String message) {
        System.out.println("Email sent to " + to + ": " + message);
    }
    
    public void sendSms(String phone, String message) {
        System.out.println("SMS sent to " + phone + ": " + message);
    }
}