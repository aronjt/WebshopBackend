package org.progmatic.webshop.configurators;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.progmatic.webshop.oauth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * {@link WebSecurityConfigurerAdapter} class to configure web security.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper mapper;
    private final TokenStore tokenStore;
    private final TokenFilter tokenFilter;
    private final CustomOath2UserService oath2UserService;
    private final Oauth2LoginSuccessHandler successHandler;

    @Autowired
    public WebSecConfig(ObjectMapper mapper, TokenStore tokenStore, TokenFilter tokenFilter, CustomOath2UserService service,
                        Oauth2LoginSuccessHandler handler) {
        this.mapper = mapper;
        this.tokenStore = tokenStore;
        this.tokenFilter = tokenFilter;
        oath2UserService = service;
        successHandler = handler;
    }

    /* changes made:
        all fields are now private final
        @Autowired has been removed from field CustomOath2UserService oath2UserService
        CustomOath2UserService oath2UserService field is initialized inside the constructor
        Oauth2LoginSuccessHandler successHandler field is initialized inside the constructor
        TODO check oauth working
            TODO also clear this class...
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConfigurationProperties("spring.session.cookie")
    public DefaultCookieSerializer cookieSerializer() {
        return new DefaultCookieSerializer();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "X-CSRF-TOKEN"));
        configuration.setAllowCredentials(true);
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "https://progmatic-webshop-project.herokuapp.com"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "HEAD", "OPTIONS"));

        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .formLogin()
                .permitAll()
                .defaultSuccessUrl("/login/success", true)
                .and()
                .logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
//                .oauth2Login()
//                .authorizationEndpoint()
//                .authorizationRequestRepository(new InMemoryRequestRepository())
//                .and()
                .oauth2Login()
                .loginPage("/login")
        .userInfoEndpoint().userService(oath2UserService)
                .and()
                .successHandler(successHandler)
                .and()
        .logout().permitAll();
    }

//                .successHandler(this::successHandler)
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(this::authenticationEntryPoint);
//        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
}

//    private void successHandler(HttpServletRequest request, HttpServletResponse httpServletResponse, org.springframework.security.core.Authentication authentication) {
//        String token = tokenStore.generateToken(authentication);
//        try {
//            httpServletResponse.getWriter().write(mapper.writeValueAsString(Collections
//                    .singletonMap("accessToken", token)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private void authenticationEntryPoint(HttpServletRequest request, HttpServletResponse httpServletResponse, org.springframework.security.core.AuthenticationException e) throws IOException {
//        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        httpServletResponse.getWriter().write(mapper.writeValueAsString(Collections.singletonMap("error", "Unauthenticated")));
//    }

//}
