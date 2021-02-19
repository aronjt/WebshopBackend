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

    /* TODO
        how to do dis? only change field "isFinish" from false to true?
     */
    /*@Transactional
    public OrderDto changeOrder(long id, OnlineOrder newOrder) {
        OnlineOrder oldOlder = em.find(OnlineOrder.class, id);
        LOG.info("old older with id {} was: {}, {}, {}",
                id, oldOlder.getPurchasedClothesList().size(), oldOlder.getTotalPrice(), oldOlder.isFinish());
        oldOlder.setPurchasedClothesList(newOrder.getPurchasedClothesList());
        oldOlder.setTotalPrice(sumTotalPrice(oldOlder.getPurchasedClothesList()));
        oldOlder.setFinish(newOrder.isFinish());
        LOG.info("new older with id {} is now: {}, {}, {}",
                id, oldOlder.getPurchasedClothesList().size(), oldOlder.getTotalPrice(), oldOlder.isFinish());
        return new OrderDto(oldOlder);
    }*/

    @Transactional
    public Feedback finishOrder(long id) {
        OnlineOrder order = em.find(OnlineOrder.class, id);

        if (order.isFinish()) {
            LOG.info("order with id {} is already finished", id);
            return new Message("order is already finished");
        }

        order.setFinish(true);

        LOG.info("order with id {} is now finished", id);

        return new Message(true, "order is finished now");
    }

    @Transactional
    public Feedback sendOrder(OrderDto orderDto) {
        User user = uds.getLoggedInUser();

        //User user = em.find(User.class, orderDto.getUserId());

        if (user != null) {
            OnlineOrder order = createNewOrder(user);

            addPurchasedClothesToDBAndToOrder(orderDto.getPurchasedClothesList(), order);
            order.setTotalPrice(orderDto.getTotalPrice());
            changeQuantitiesInStock(order.getPurchasedClothesList());

            LOG.info("new order created, list size {}, username {}, total price {}",
                    order.getPurchasedClothesList().size(), order.getUser().getUsername(), order.getCreationTime());

            em.persist(order);

            return createFeedbackMessage(true, "order success");
        }

        LOG.warn("cannot create order, because user is null");

        return createFeedbackMessage(false, "cannot create order, because user is null");

    }

    private Feedback createFeedbackMessage(boolean success, String message) {
        Message m = new Message();
        m.setSuccess(success);
        m.setMessage(message);
        return m;
    }

    private OnlineOrder createNewOrder(User user) {
        OnlineOrder order = new OnlineOrder();
        order.setUser(user);
        order.setFinish(false);
        order.setPurchasedClothesList(new ArrayList<>());
        return order;
    }

    @Transactional
    public void addPurchasedClothesToDBAndToOrder(List<PurchasedClothDto> clothes, OnlineOrder order) {
        for (PurchasedClothDto c : clothes) {
            PurchasedClothes newC = new PurchasedClothes(c);
            newC.setClothes(em.find(Clothes.class, c.getClothesId()));
            newC.setOnlineOrder(order);
            order.getPurchasedClothesList().add(newC);
            em.persist(newC);
        }
    }

    @Transactional
    public void changeQuantitiesInStock(List<PurchasedClothes> clothes) {
        for (PurchasedClothes c : clothes) {
            try {
                Stock stock = em.createQuery("SELECT s FROM Stock s WHERE s.clothes = :cloth", Stock.class)
                        .setParameter("cloth", c)
                        .getSingleResult();
                stock.setQuantity(stock.getQuantity() - c.getQuantity());
            } catch (NoResultException e) {
                LOG.warn("cloth with id {} not found in stock",
                        c.getId());
            }
        }
    }

}




/*
rendelés
ruha 1
ruha 2
ruha 3

ok, tovább -> sendUserData

szállítási adatok
    - űrlap megjelenik
        - user regisztrált?
            - kitöltött adatokkal jelenik meg
        - nem regisztrált?
            - üres űrlap, amit ki kell töltenie
név
cím
tel.
stb.

ok, tovább -> sendOrder()

megerősítés
szupi!
(kap emailt róla)

kösz!
 */
