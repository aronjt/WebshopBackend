package org.progmatic.webshop.configurators;

import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures Tomcat server to set Cookies' SameSite property to be None.
 */
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
