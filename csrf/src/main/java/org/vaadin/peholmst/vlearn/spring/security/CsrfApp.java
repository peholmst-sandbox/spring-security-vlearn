package org.vaadin.peholmst.vlearn.spring.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CsrfApp {

    public static void main(String[] args) {
        SpringApplication.run(CsrfApp.class, args);
    }
}
