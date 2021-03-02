package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.*;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.services.EmailSenderService;
import org.progmatic.webshop.services.MyUserDetailsService;
import org.progmatic.webshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controls actions related to orders.<br>
 *     Containing endpoints:
 *     <ul>
 *         <li>/orders, get</li>
 *         <li>/orders, post</li>
 *         <li>/orders/{id}, get</li>
 *         <li>/orders/{id}, put</li>
 *     </ul>
 *     <br>
 *     All endpoints have {@link PreAuthorize} annotation.
 */
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

    /**
     * Endpoint for getting all orders that are represented in the database.<br>
     *     To see all orders, authorized user must have admin role.<br>
     *     See {@link OrderService#getAllOrders()} for more information.
     * @return the {@link org.progmatic.webshop.returnmodel.ListResult} that contains the orders.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/orders")
    public Feedback getAllOrders() {
        return service.getAllOrders();
    }

    /**
     * Endpoint for getting one order with the id.<br>
     *     To see the order, authorized user must have admin role.<br>
     *     See {@link OrderService#getOneOrder(long)} for more information.
     * @param id is the order's id
     * @return the {@link org.progmatic.webshop.returnmodel.ListResult} that contains the order.
     */
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

    /**
     * Endpoint for changing the order's isFinished field from false to true.<br>
     *      Only admin users have the permission to do this.<br>
     *      See {@link OrderService#finishOrder(long)} for more information.
     * @param id is the order's id
     * @return a confirm {@link org.progmatic.webshop.returnmodel.Message} about the finishing process
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/orders/{id}")
    public Feedback changeOrder(@PathVariable("id") long id) {
        return service.finishOrder(id);
    }

    /**
     * Endpoint for sending a new order. If the process was successfully, a confirmation email will be sent to the user.<br>
     *     User must be logged in to do this.<br>
     *     See {@link OrderService#sendOrder(OrderDto)} for more information.
     * @param order is the order that will be saved in the database
     * @return a confirm {@link org.progmatic.webshop.returnmodel.Message} about the sending process
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/orders")
    public Feedback sendOrder(@RequestBody OrderDto order) {
        Feedback orderFeedback = service.sendOrder(order);

        if (orderFeedback.isSuccess()){
//
            sendEmail.prepareSuccesfulOrderEmail(order,uds.getLoggedInUser(), EmailSenderHelper.SHOPPING);
        }

        return orderFeedback;
    }

}
