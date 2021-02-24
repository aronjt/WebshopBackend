package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.dto.UserDto;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.ListResult;
import org.progmatic.webshop.returnmodel.Message;
import org.progmatic.webshop.services.MyUserDetailsService;
import org.progmatic.webshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final MyUserDetailsService userService;
    private final OrderService orderService;

    @Autowired
    public UserController(MyUserDetailsService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
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

    @GetMapping("/user/order/{id}")
    public Feedback getUsersOrders(@PathVariable("id") long id) {
        return orderService.getUsersOrders(id);
    }

    @PutMapping("/user")
    public Feedback editUser(@RequestBody RegisterUserDto userDto) {
        return userService.editUser(userDto);
    }
}
