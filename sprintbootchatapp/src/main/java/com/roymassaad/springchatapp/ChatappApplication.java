package com.roymassaad.springchatapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;

@SpringBootApplication
public class ChatappApplication {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(ChatappApplication.class);

        // Check for server configuration to debug in console on start
        // TODO: show full URL
        ServerProperties serverProperties = application.run(args).getBean(ServerProperties.class);

        int port = serverProperties.getPort();

        String contextPath = serverProperties.getServlet().getContextPath();

        System.out.println("Server is running on port: " + port);
        System.out.println("Context path: " + contextPath);
    }

        
}
