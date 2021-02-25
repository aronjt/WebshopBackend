package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.*;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.model.User;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.services.EmailSenderService;
import org.progmatic.webshop.services.MyUserDetailsService;
import org.progmatic.webshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderService service;
    private final EmailSenderService sendEmail;
    private final MyUserDetailsService uds;

    @Autowired
    public OrderController(OrderService service,EmailSenderService sendEmail,MyUserDetailsService uds) {
        this.service = service;
        this.sendEmail = sendEmail;
        this.uds = uds;
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
        Feedback orderFeedback=service.sendOrder(order);

        if (orderFeedback.isSuccess()){
//            todo emailkuldes
            sendEmail.prepareSuccesfulOrderEmail(order,uds.getLoggedInUser(), EmailSenderHelper.SHOPPING);
        }
        return orderFeedback;
    }

}
