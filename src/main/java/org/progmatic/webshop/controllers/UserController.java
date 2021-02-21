package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.dto.UserDto;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.ListResult;
import org.progmatic.webshop.returnmodel.Message;
import org.progmatic.webshop.services.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    /* endpoints
    get   /profile
    put  /profile
    get  /users                                          //list all users (admin)
    get  /users/{id}                                   //list one user (admin)
     */

    private final MyUserDetailsService userService;

    @Autowired
    public UserController(MyUserDetailsService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Feedback listAllUser(){
        return userService.listAllUsers();
    }

    @GetMapping("/users/{id}")
    public Feedback getUser(@PathVariable("id") long id) {
        ListResult<UserDto> userDtoList = new ListResult<>();
        userDtoList.getList().add(new UserDto(userService.getUser(id)));
        return userDtoList;
    }

    @GetMapping("/user")
    public Feedback getLoggedInUser() {
        ListResult<UserDto> loggedInUser = new ListResult<>();
        if (loggedInUser.getList() != null) {
            loggedInUser.getList().add(new UserDto(userService.getLoggedInUser()));
            loggedInUser.setSuccess(true);
            return loggedInUser;
        }
        return new Message(false, "No user is logged in");
    }
}
