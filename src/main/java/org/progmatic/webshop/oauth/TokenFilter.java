package org.progmatic.webshop.oauth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class TokenFilter extends OncePerRequestFilter {
    private final TokenStore tokenStore;


    public TokenFilter(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
  String authToken = httpServletRequest.getHeader("Authorization");

   if(authToken!=null) {
       String token = authToken.split( "")[1];
      Authentication authentication= tokenStore.getAuth(token);
   }
   filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
