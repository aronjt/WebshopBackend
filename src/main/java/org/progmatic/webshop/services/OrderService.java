package org.progmatic.webshop.services;

import org.dozer.DozerBeanMapper;
import org.progmatic.webshop.dto.OrderDto;
import org.progmatic.webshop.model.OnlineOrder;
import org.progmatic.webshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class OrderService {

    @PersistenceContext
    EntityManager em;

    DozerBeanMapper dozer;

    @Autowired
    public OrderService(DozerBeanMapper dozer) {
        this.dozer = dozer;
    }

    public OrderDto onlyForTesting() {
        OnlineOrder order = new OnlineOrder();
        order.setTotalPrice(10.99f);
        order.setFinish(true);
        order.setUser(new User());
        order.setId(1);
        order.setPurchasedClothesList(new ArrayList<>());

        return dozer.map(order, OrderDto.class);
    }

    @Transactional
    public OrderDto getOneOrder(long id) {
        OnlineOrder order = em.find(OnlineOrder.class, id);
        if (order != null) {
            return dozer.map(order, OrderDto.class);
        }
        return new OrderDto();
    }

}
