package org.progmatic.webshop.services;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class OrderService {

    @PersistenceContext
    EntityManager em;

    DozerBeanMapper dozer;

    public OrderService() {
        dozer = new DozerBeanMapper();
    }
}
