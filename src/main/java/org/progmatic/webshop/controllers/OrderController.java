package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.OrderDto;
import org.progmatic.webshop.model.OnlineOrder;
import org.progmatic.webshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    /* endpoints
        /order
            post
            add new order
        /orders
            get
            list all orders (admin)
        /orders/{id}
            get
            list one order (admin)
        /orders/{id}
            put
            edit one order (admin)
     */

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/orders")
    public List<OrderDto> getAllOrders() {
        return null;
    }

    /**
     * BEWARE!!! FAILED TEST!!!
     * @param id id
     * @return OrderDto
     */
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/orders/{id}")
    public OrderDto getOneOrder(@PathVariable("id") long id) {
        return service.getOneOrder(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/orders/{id}")
    public OrderDto changeOrder(@PathVariable("id") long id) {
        return null;
    }

    @PostMapping("/orders")
    public OrderDto sendOrder(@RequestBody OnlineOrder order) {
        return null;
    }

}
