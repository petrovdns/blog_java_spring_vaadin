package com.petrovdns.vaadin;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@PWA(
        name = "Petrov BLOG",
        shortName = "BLOG",
        offlinePath = "offline.html",
        offlineResources = {"images/crm.png", "images/offline.webp"}
)
@Theme("my-theme")
@EnableWebSecurity
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        System.setProperty("vaadin.devmode.devTools.enabled", "false");
        SpringApplication.run(Application.class, args);

    }

}
