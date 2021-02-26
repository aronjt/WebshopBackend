package org.progmatic.webshop.oauth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TokenStore {
    private final Map< String , Authentication> cache = new HashMap<>();

public String generateToken ( Authentication authetication){
    String token = UUID.randomUUID().toString();
    cache.put (token,authetication);
    return  token ;
}
public Authentication getAuth (String token){
    return cache.getOrDefault(token, null);
}
}
