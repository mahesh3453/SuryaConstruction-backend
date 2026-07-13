package com.suryaconstruction.permit;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone;

@SpringBootApplication
public class PermitTrackerApplication {

    @PostConstruct
    public void init() {
        String tz = System.getenv("TZ");
        if (tz == null || tz.isEmpty()) {
            tz = "Asia/Kolkata";
        }
        TimeZone.setDefault(TimeZone.getTimeZone(tz));
        System.out.println("JVM timezone initialized to: " + TimeZone.getDefault().getID());
    }

    public static void main(String[] args) {
        SpringApplication.run(PermitTrackerApplication.class, args);
    }
}
