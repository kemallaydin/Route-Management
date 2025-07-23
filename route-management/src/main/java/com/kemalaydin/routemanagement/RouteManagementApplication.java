package com.kemalaydin.routemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RouteManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouteManagementApplication.class, args);
    }

}
