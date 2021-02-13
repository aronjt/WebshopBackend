package org.progmatic.webshop.services;

import org.dozer.DozerBeanMapper;
import org.progmatic.webshop.dto.OrderDto;
import org.progmatic.webshop.dto.UserDto;
import org.progmatic.webshop.model.OnlineOrder;
import org.progmatic.webshop.model.PurchasedClothes;
import org.progmatic.webshop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private static Logger LOG = LoggerFactory.getLogger(OrderService.class);

    @PersistenceContext
    EntityManager em;

    @Transactional
    public OrderDto getOneOrder(long id) {
        OnlineOrder order = em.find(OnlineOrder.class, id);
        if (order != null) {
            LOG.debug("order with id {} found, for user {} with {} purchased cloth(es)",
                    id, order.getUser().getEmail(), order.getPurchasedClothesList().size());
            return new OrderDto(order);
        }
        return new OrderDto();
    }

    @Transactional
    public List<OrderDto> getAllOrders() {
        List<OnlineOrder> orders = em.createQuery(
                "SELECT o FROM OnlineOrder o", OnlineOrder.class
        ).getResultList();

        List<OrderDto> orderDtos = new ArrayList<>();
        for (OnlineOrder o : orders) {
            orderDtos.add(new OrderDto(o));
        }

        LOG.info("all orders founded: {} orders in list",
                orders.size());

        return orderDtos;
    }

    /* TODO
        how to do dis? only change field "isFinish" from false to true?
     */
    @Transactional
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
    }

    @Transactional
    public OrderDto sendOrder(OrderDto order) {
        User user = em.find(User.class, order.getUserId());
        /* TODO
            write it...
         */
        return null;
    }


    private float sumTotalPrice(List<PurchasedClothes> clothes) {
        float sum = 0;
        for (PurchasedClothes c : clothes) {
            sum += c.getClothes().getPrice();
        }
        return sum;
    }


}
