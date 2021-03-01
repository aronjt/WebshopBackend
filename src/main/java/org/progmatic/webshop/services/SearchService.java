package org.progmatic.webshop.services;

import org.progmatic.webshop.dto.GenderDto;
import org.progmatic.webshop.model.Gender;
import org.progmatic.webshop.model.Type;
import org.progmatic.webshop.returnmodel.ListResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Service for helping {@link org.progmatic.webshop.controllers.SearchController}.
 */
@Service
public class SearchService {

    @PersistenceContext
    EntityManager em;

    /**
     * Finds all genders represented in the database.
     * @return a {@link ListResult} that contains the genders
     */
    @Transactional
    public ListResult<GenderDto> getGenders() {
        List<Gender> genders = em.createQuery("SELECT g FROM Gender g", Gender.class).getResultList();
        ListResult<GenderDto> toReturn = new ListResult<>();
        for (Gender g : genders) {
            toReturn.getList().add(new GenderDto(g));
        }
        return toReturn;
    }

    /**
     * Finds all types represented in the database.
     * @return a {@link ListResult} that contains the types
     */
    @Transactional
    public ListResult<Type> getTypes() {
        return new ListResult<>(em.createQuery("SELECT t FROM Type t", Type.class).getResultList());
    }

    /**
     * Finds all colors represented in the database (as a color of a cloth).
     * @return a {@link ListResult} that contains the colors (one color may belong to more than one cloth,
     * but every color will be represented only once in the list)
     */
    @Transactional
    public ListResult<String> getColors() {
        return new ListResult<>(em.createQuery("SELECT DISTINCT c.color FROM Clothes c", String.class).getResultList());
    }

}
