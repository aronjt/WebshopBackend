package org.progmatic.webshop.services;

import org.progmatic.webshop.dto.OrderDto;
import org.progmatic.webshop.dto.PurchasedClothDto;
import org.progmatic.webshop.model.*;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.ListResult;
import org.progmatic.webshop.returnmodel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for helping {@link org.progmatic.webshop.controllers.OrderController}.
 */
@Service
public class OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

    @PersistenceContext
    EntityManager em;

    private final MyUserDetailsService uds;

    @Autowired
    public OrderService(MyUserDetailsService uds) {
        this.uds = uds;
    }

    /**
     * Finds one {@link OnlineOrder} with the given id in the database.
     * @param id is the order's id
     * @return a {@link ListResult} contains the wanted order, or a {@link Message} if no order found
     */
    @Transactional
    public Feedback getOneOrder(long id) {
        OnlineOrder order = em.find(OnlineOrder.class, id);
        if (order != null) {
            LOG.debug("order with id {} found, for user {} with {} purchased cloth(es)",
                    id, order.getUser().getUsername(), order.getPurchasedClothesList().size());
            ListResult<OrderDto> toReturn = new ListResult<>();
            toReturn.getList().add(new OrderDto(order));
            return toReturn;
        }
        return new Message("order cannot be found");
    }

    /**
     * Finds all {@link OnlineOrder} represented in the database.
     * @return a {@link ListResult} contains all orders
     */
    @Transactional
    public Feedback getAllOrders() {
        List<OnlineOrder> orders = em.createQuery(
                "SELECT o FROM OnlineOrder o", OnlineOrder.class
        ).getResultList();

        List<OrderDto> orderDtos = new ArrayList<>();
        for (OnlineOrder o : orders) {
            orderDtos.add(new OrderDto(o));
        }

        LOG.info("all orders found: {} orders in list",
                orders.size());

        return new ListResult<>(orderDtos);
    }

    /**
     * Finishes an order with the given id. So the {@link OnlineOrder}'s isFinish field will be set from
     * false to true.
     * @param id is the order's id
     * @return a confirm {@link Message} about the finishing process
     */
    @Transactional
    public Feedback finishOrder(long id) {
        OnlineOrder order = em.find(OnlineOrder.class, id);

        if (order == null) {
            LOG.info("order with id {} cannot be found", id);
            return new Message("order cannot be found");
        }

        if (order.isFinish()) {
            LOG.info("order with id {} is already finished", id);
            return new Message("order is already finished");
        }

        order.setFinish(true);

        LOG.info("order with id {} is now finished", id);

        return new Message(true, "order is finished now");
    }

    /**
     * Saves a new order into the database.<br>
     *     See {@link OrderService#createNewOrder(User)},
     *     {@link OrderService#addPurchasedClothesToDBAndToOrder(List, OnlineOrder)} and
     *     {@link OrderService#changeQuantitiesInStock(List)} for more information.
     * @param orderDto is the new order
     * @return a confirm {@link Message} about the saving process
     */
    @Transactional
    public Feedback sendOrder(OrderDto orderDto) {
        User user = uds.getLoggedInUser();

        if (user != null) {
            OnlineOrder order = createNewOrder(user);

            addPurchasedClothesToDBAndToOrder(orderDto.getPurchasedClothesList(), order);
            order.setTotalPrice(orderDto.getTotalPrice());
            em.persist(order);
            changeQuantitiesInStock(order.getPurchasedClothesList());

            LOG.info("new order created, list size {}, username {}, total price {}",
                    order.getPurchasedClothesList().size(), order.getUser().getUsername(), order.getCreationTime());


            return new Message(true, "order sent successfully");
        }

        LOG.warn("cannot create order, because user is null");

        return new Message(false, "cannot create order, because user is null");
    }

    /**
     * Creates a new {@link OnlineOrder} object.
     * @param user is the {@link User} who sent the order
     * @return the created order
     */
    private OnlineOrder createNewOrder(User user) {
        OnlineOrder order = new OnlineOrder();
        order.setUser(user);
        order.setFinish(false);
        order.setPurchasedClothesList(new ArrayList<>());
        return order;
    }

    /**
     * Creates {@link PurchasedClothes} from every {@link PurchasedClothDto} that are in the list,
     * and saves them into the database with the {@link OnlineOrder}.
     * @param clothes is the list of clothes
     * @param order is the order the {@link PurchasedClothes} belong to
     */
    @Transactional
    public void addPurchasedClothesToDBAndToOrder(List<PurchasedClothDto> clothes, OnlineOrder order) {
        for (PurchasedClothDto c : clothes) {
            PurchasedClothes newC = new PurchasedClothes(c);
            newC.setClothes(em.find(Clothes.class, c.getId()));
            newC.setOnlineOrder(order);
            order.getPurchasedClothesList().add(newC);
            em.persist(newC);
        }
    }

    /**
     * Changes the quantity of the {@link Clothes} in the {@link Stock}
     * by calling {@link OrderService#changeStock(Clothes, int)}.
     * @param clothes is the list of {@link PurchasedClothes}
     */
    @Transactional
    public void changeQuantitiesInStock(List<PurchasedClothes> clothes) {
        for (PurchasedClothes pc : clothes) {
            try {
                Clothes c = em.find(Clothes.class, pc.getClothes().getId());
                changeStock(c, pc.getQuantity());
            } catch (NoResultException e) {
                LOG.warn("cloth with id {} not found in stock",
                        pc.getId());
            }
        }
    }

    /**
     * Changes the quantity of one {@link Clothes} in the {@link Stock}.
     * @param c is the cloth
     * @param quantity is the number that will be subtracted from the stock's original quantity
     */
    @Transactional
    public void changeStock(Clothes c, int quantity) {
        try {
            Stock stock = em.createQuery("SELECT s FROM Stock s WHERE s.clothes = :cloth", Stock.class)
                    .setParameter("cloth", c)
                    .getSingleResult();
            int newQuantity = Math.max(stock.getQuantity() - quantity, 0);
            stock.setQuantity(newQuantity);
        } catch (NoResultException e) {
            LOG.warn("cloth with id {} not found in stock",
                    c.getId());
        }
    }
}