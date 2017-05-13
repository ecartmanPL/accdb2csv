package net.siekiera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Accdb2csvApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Accdb2csvApplication.class);
        app.setLogStartupInfo(false);
        app.run(args);
    }
}