package org.progmatic.webshop.configurators;

import org.apache.catalina.Context;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfiguration {

    public static final Logger LOGGER = LoggerFactory.getLogger(TomcatConfiguration.class);

    @Bean
    WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        LOGGER.info("TomcatConfiguration cookieProcessorCustomizer called");
        return tomcatServletWebServerFactory -> tomcatServletWebServerFactory.addContextCustomizers(context -> {
            Rfc6265CookieProcessor processor = new Rfc6265CookieProcessor();
            processor.setSameSiteCookies("None");
            context.setCookieProcessor(processor);
        });
    }
}
