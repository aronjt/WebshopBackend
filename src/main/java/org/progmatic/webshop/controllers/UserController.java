package org.progmatic.webshop.controllers;

import org.progmatic.webshop.services.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    /* endpoints
    post /register
    get   /profile
    put  /profile
    get  /user/orders                               //list all orders (user)
    get  /user/orders/{id}                        //list one order (user)
    get  /users                                          //list all users (admin)
    get  /users/{id}                                   //list one user (admin)
     */

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private MyUserDetailsService userService;

    @Autowired
    public UserController(MyUserDetailsService userService) {
        this.userService = userService;
    }



}
