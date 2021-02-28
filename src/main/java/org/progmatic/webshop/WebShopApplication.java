package org.progmatic.webshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * This application is the backend part of the Clothing WebShop project created by
 * students of Progmatic Academy.<br><br>
 *     The application permit of ordering clothes, handling clothes' information and users' profiles in database.<br><br>
 *     Frontend developers: Gábor Lanszki (project owner), Zoltán Márai, Róbert Sárközi, Márk Weisz<br>
 *     Collaborators: Hédi Besenyei (scrum master), Gábor Komáromi, Péter Nyilasy, Gergő Papp
 * @author Mária Benedek, Áron Józsa-Teleki, Máté Skoda
 */
@SpringBootApplication
//        (exclude = {DataSourceAutoConfiguration.class ,
//        DataSourceTransactionManagerAutoConfiguration.class })
@EnableOAuth2Client
public class WebShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebShopApplication.class, args);
    }
}
