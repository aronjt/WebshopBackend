package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.*;
import org.progmatic.webshop.model.OnlineOrder;
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

    /* TODO
        what URL??
     */
    /*@GetMapping("/valami_aminek_rohadtul_nem_tudok_rendes_cimet_adni_de_visszaadja_a_belogolt_user_adatait")
    public RegisterUserDto getUserData() {
        User user = uds.getLoggedInUser();
        if (user != null) {
            return new RegisterUserDto(user);
        }
        return new RegisterUserDto();
    }*/

    @GetMapping("/valami_aminek_rohadtul_nem_tudok_rendes_cimet_adni_de_visszaadja_a_belogolt_user_adatait")
    public String getUserData() {
        User user = uds.getLoggedInUser();
        if (user != null) {
            return user.getUserRole();
        }
        return null;
    }

    @PostMapping("/orders")
    public FeedbackDto sendOrder(@RequestBody List<PurchasedClothDto> clothes, @RequestBody RegisterUserDto user) {
        OrderDto order = service.sendOrder(clothes, user);
        return new FeedbackDto(order.getId(), "order successfully sent");
    }

}
