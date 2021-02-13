package org.progmatic.webshop.services;

import org.dozer.DozerBeanMapper;
import org.progmatic.webshop.dto.OrderDto;
import org.progmatic.webshop.dto.UserDto;
import org.progmatic.webshop.model.OnlineOrder;
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


}
