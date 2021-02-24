package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.*;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/orders")
    public Feedback getAllOrders() {
        return service.getAllOrders();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/orders/{id}")
    public Feedback getOneOrder(@PathVariable("id") long id) {
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/orders/{id}")
    public Feedback changeOrder(@PathVariable("id") long id) {
        return service.finishOrder(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/orders")
    public Feedback sendOrder(@RequestBody OrderDto order) {
        return service.sendOrder(order);
    }

}
