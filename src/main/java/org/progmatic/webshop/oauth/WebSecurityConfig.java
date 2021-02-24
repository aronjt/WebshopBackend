package org.progmatic.webshop.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    DataSource dataSource;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.antMatcher("/**").authorizeRequests()                  //we dont need to restrict any specific HTTP method
//                .antMatchers("/").permitAll()            // the "/" URL is allowed for everyone
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login();



        http.authorizeRequests()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/customer/**").authenticated()
        .anyRequest().permitAll()
        .and()
        .formLogin()
        .loginPage("/login")
        .usernameParameter("email")
        .permitAll()
        .defaultSuccessUrl("/")
        .and()
                .oauth2Login()
                .loginPage("/login")
        .userInfoEndpoint().userService(oath2UserService)
                .and()
                .successHandler(successHandler)
                .and()
        .logout().permitAll();
    }
    @Autowired
    CustomOath2UserService oath2UserService;
@Autowired
    private Oauth2LoginSuccessHandler successHandler;
}
