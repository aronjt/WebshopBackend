package org.progmatic.webshop.services;

import org.progmatic.webshop.dto.ListDto;
import org.progmatic.webshop.model.Gender;
import org.progmatic.webshop.model.Type;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class SearchService {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public ListDto<Gender> getGenders() {
        return new ListDto<>(em.createQuery("SELECT g FROM Gender g", Gender.class).getResultList());
    }

    @Transactional
    public ListDto<Type> getTypes() {
        return new ListDto<>(em.createQuery("SELECT t FROM Type t", Type.class).getResultList());
    }

}
