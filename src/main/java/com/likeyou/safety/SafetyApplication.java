package com.likeyou.safety;

import jakarta.annotation.PostConstruct;   
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;  

@SpringBootApplication
public class SafetyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafetyApplication.class, args);
    }

    @PostConstruct
    public void generatePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("🔒 Encoded password for admin1234:");
        System.out.println(encoder.encode("admin1234"));
    }
}
