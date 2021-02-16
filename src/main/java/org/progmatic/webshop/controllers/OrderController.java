package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.*;
import org.progmatic.webshop.model.User;
import org.progmatic.webshop.services.MyUserDetailsService;
import org.progmatic.webshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class OrderController {

    private final OrderService service;
    private final MyUserDetailsService uds;

    @Autowired
    public OrderController(OrderService service, MyUserDetailsService uds) {
        this.service = service;
        this.uds = uds;
    }

    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/orders")
    public List<OrderDto> getAllOrders() {
        return service.getAllOrders();
    }

    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/orders/{id}")
    public OrderDto getOneOrder(@PathVariable("id") long id) {
        return service.getOneOrder(id);
    }

    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    /*@PutMapping("/orders/{id}")
    public OrderDto changeOrder(
            @PathVariable("id") long id,
            @RequestBody OnlineOrder newOrder
    ) {
        return service.changeOrder(id, newOrder);
    }*/

    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/orders/{id}")
    public FeedbackDto changeOrder(@PathVariable("id") long id) {
        return service.finishOrder(id);
    }

    //@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    // maybe no need the annotation above?
    @PostMapping("/orders")
    public OrderDto sendOrder(@RequestBody List<PurchasedClothDto> clothes) {
        return service.sendOrder(clothes);
    }

    /* TODO
        what URL??
     */
    @GetMapping("/valami")
    public RegisterUserDto getUserData() {
        User user = uds.getLoggedInUser();
        if (user != null) {
            return new RegisterUserDto(user);
        }
        return new RegisterUserDto();
    }

    @PostMapping("/valami")
    public FeedbackDto sendUserData(@RequestBody List<PurchasedClothDto> clothes, @RequestBody RegisterUserDto user) {

        return new FeedbackDto();
    }

}
