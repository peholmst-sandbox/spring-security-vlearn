package org.vaadin.peholmst.vlearn.spring.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LeakingSecurityContextApp {

    public static void main(String[] args) {
        SpringApplication.run(LeakingSecurityContextApp.class, args);
    }
}
