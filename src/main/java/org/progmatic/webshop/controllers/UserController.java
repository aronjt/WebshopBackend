package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.dto.UserDto;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.ListResult;
import org.progmatic.webshop.returnmodel.Message;
import org.progmatic.webshop.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

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
        ListResult<RegisterUserDto> loggedInUser = new ListResult<>();
        if (userService.getLoggedInUser() != null) {
            loggedInUser.setSuccess(true);
            loggedInUser.getList().add(userService.getLoggedInRegisterDto());
            return loggedInUser;
        }
        return new Message(false, "No user is logged in");
    }

    @GetMapping("/user/order")
    public Feedback getUsersOrders() {
        return userService.getUserOrders();
    }

    @PutMapping("/user")
    public Feedback editUser(@RequestBody RegisterUserDto userDto) {
        return userService.editUser(userDto);
    }
}
