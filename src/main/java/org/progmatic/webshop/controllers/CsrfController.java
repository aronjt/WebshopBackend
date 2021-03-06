package org.progmatic.webshop.controllers;

import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.ListResult;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controls our home! Beware!
 */
@RestController
public class CsrfController {

    /**
     * Endpoint for getting CSRF-token.
     * @param token is the token that will be sent back
     * @return the {@link CsrfToken} in a {@link ListResult}
     */
    @GetMapping("/csrf")
    public Feedback getCsrf(CsrfToken token) {
        ListResult<CsrfToken> tokenList = new ListResult<>();
        tokenList.getList().add(token);
        tokenList.setSuccess(true);
        return tokenList;
    }

}
