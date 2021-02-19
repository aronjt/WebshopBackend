package org.progmatic.webshop.services;

import org.progmatic.webshop.dto.GenderDto;
import org.progmatic.webshop.dto.ListDto;
import org.progmatic.webshop.model.Gender;
import org.progmatic.webshop.model.Type;
import org.progmatic.webshop.returnmodel.ListResult;
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
    public ListResult<GenderDto> getGenders() {
        List<Gender> genders = em.createQuery("SELECT g FROM Gender g", Gender.class).getResultList();
        ListResult<GenderDto> toReturn = new ListResult<>();
        for (Gender g : genders) {
            toReturn.getList().add(new GenderDto(g));
        }
        return toReturn;
    }

    @Transactional
    public ListResult<Type> getTypes() {
        return new ListResult<>(em.createQuery("SELECT t FROM Type t", Type.class).getResultList());
    }

}
