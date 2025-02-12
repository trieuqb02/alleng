package com.alleng.favourite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FavouriteApplication {
    public static void main(String[] args) {
        SpringApplication.run(FavouriteApplication.class, args);
    }
}
