package org.progmatic.webshop.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class HomeController {

    @GetMapping(path = "/csrf")
    public String getCsrf(HttpServletRequest request, HttpSession session){
        HttpSessionCsrfTokenRepository repo = new HttpSessionCsrfTokenRepository();
        CsrfToken csrf = repo.loadToken(request);
        return csrf.getToken();
    }

}
