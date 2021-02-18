package org.progmatic.webshop.services;

import org.progmatic.webshop.dto.GenderDto;
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
    public ListDto<GenderDto> getGenders() {
        List<Gender> genders = em.createQuery("SELECT g FROM Gender g", Gender.class).getResultList();
        ListDto<GenderDto> toReturn = new ListDto<>();
        for (Gender g : genders) {
            toReturn.getList().add(new GenderDto(g));
        }
        return toReturn;
    }

    @Transactional
    public ListDto<Type> getTypes() {
        return new ListDto<>(em.createQuery("SELECT t FROM Type t", Type.class).getResultList());
    }

}
