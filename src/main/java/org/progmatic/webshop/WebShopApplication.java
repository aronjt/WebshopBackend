package org.progmatic.webshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringBootApplication
//        (exclude = {DataSourceAutoConfiguration.class ,
//        DataSourceTransactionManagerAutoConfiguration.class })
@EnableOAuth2Client
public class WebShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebShopApplication.class, args);
    }
}
