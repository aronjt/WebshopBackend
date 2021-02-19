package org.progmatic.webshop.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/csrf")
    public CsrfToken getCsrf(CsrfToken token) {
        return token;
    }

}
