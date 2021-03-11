package org.progmatic.webshop.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.progmatic.webshop.dto.ClothDto;
import org.progmatic.webshop.dto.FilterClothesDto;
import org.progmatic.webshop.dto.StockDto;
import org.progmatic.webshop.model.Clothes;
import org.progmatic.webshop.model.QClothes;
import org.progmatic.webshop.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for helping {@link org.progmatic.webshop.controllers.ClothesController}.
 */
@Service
public class ClothesService {

    private static final Logger LOG = LoggerFactory.getLogger(ClothesService.class);

    @PersistenceContext
    EntityManager em;

    /**
     *
     * @return all clothes represented in the database
     */
    @Transactional
    public List<ClothDto> getAllClothes() {
        List<Clothes> clothes = em.createQuery("SELECT c FROM Clothes c", Clothes.class).getResultList();
        List<ClothDto> clothDtoList = new ArrayList<>();
        for (Clothes clothe : clothes) {
            clothDtoList.add(new ClothDto(clothe));
        }
        LOG.info("all clothes found: {} orders in list", clothDtoList.size());
        return clothDtoList;
    }

    /**
     *
     * @param id is the id of the cloth
     * @return one cloth with the given id
     */
    @Transactional
    public ClothDto getOneCloth(long id) {
        Clothes clothes = em.find(Clothes.class, id);
        LOG.info("one cloth given back");
        return new ClothDto(clothes);
    }

    /**
     *
     * @param gender is the gender of the clothes
     * @return the clothes with the given gender
     */
    @Transactional
    public List<ClothDto> getClothesFromGender(String gender) {
        List<Clothes> clothesList = em.createQuery
                ("SELECT c FROM Clothes c JOIN FETCH c.gender WHERE c.gender.gender = :gender", Clothes.class)
                .setParameter("gender", gender)
                .getResultList();
        List<ClothDto> clothDtoList = new ArrayList<>();
        for (Clothes clothes : clothesList) {
            clothDtoList.add(new ClothDto(clothes));
        }
        LOG.info("all clothes found by gender: {} orders in list", clothDtoList.size());
        return clothDtoList;
    }

    /**
     * Adds a new {@link Clothes} to the database.
     * @param clothDto contains the cloth's short data
     */
    @Transactional
    public Clothes addNewCloth(ClothDto clothDto) {
        Clothes c = new Clothes(clothDto);
        em.persist(c);
        LOG.info("one cloth added to database");
        return c;
    }

    /**
     * Filters the clothes.
     * @param filter is the filter
     * @return list of the clothes that have been found
     */
    public List<ClothDto> filterClothes(FilterClothesDto filter) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        BooleanBuilder whereCondition = new BooleanBuilder();
        if (!StringUtils.isEmpty(filter.getName())) {
            whereCondition.and(QClothes.clothes.name.like("%" + filter.getName() + "%"));
        }
        if (!StringUtils.isEmpty(filter.getGender())) {
            whereCondition.and(QClothes.clothes.gender.gender.eq(filter.getGender()));
        }
        if (!StringUtils.isEmpty(filter.getType())) {
            whereCondition.and(QClothes.clothes.type.type.eq(filter.getType()));
        }
        if (!StringUtils.isEmpty(filter.getColor())) {
            whereCondition.and(QClothes.clothes.color.eq(filter.getColor()));
        }
        if (filter.getPriceMin() >= 0 && filter.getPriceMax() > 0 && filter.getPriceMin() < filter.getPriceMax()) {
            whereCondition.and(QClothes.clothes.price.between(filter.getPriceMin(), filter.getPriceMax()));
        }
        List<Clothes> clothesList = queryFactory.selectFrom(QClothes.clothes).where(whereCondition).fetch();
        List<ClothDto> clothDtoList = new ArrayList<>();
        for (Clothes clothes : clothesList) {
            clothDtoList.add(new ClothDto(clothes));
        }
        LOG.info("Clothes been filtered");
        return clothDtoList;
    }

    /**
     *
     * @param id is the cloth's id
     * @return the cloth with the given id represented in the {@link Stock}
     */
    @Transactional
    public StockDto getStockLevel(long id) {
        List<Stock> stock = em.createQuery("SELECT s FROM Stock s WHERE s.clothes.id = :id", Stock.class)
                .setParameter("id", id).getResultList();
        StockDto stockDto = new StockDto();
        for (Stock stock1 : stock) {
            switch (stock1.getSize()) {
                case "S" -> stockDto.setSizeS(stock1.getQuantity());
                case "M" -> stockDto.setSizeM(stock1.getQuantity());
                case "L" -> stockDto.setSizeL(stock1.getQuantity());
                case "XL" -> stockDto.setSizeXl(stock1.getQuantity());
            }
        }
        LOG.info("Stock level has given back");
        return stockDto;
    }

    @Transactional
    public void addClothToStock(Clothes c) {
        List<Stock> stocks = em.createQuery("SELECT s FROM Stock s", Stock.class).getResultList();
        boolean alreadyInStock = false;
        for (Stock s : stocks) {
            if (s.getClothes().equals(c)) {
                alreadyInStock = true;
                break;
            }
        }
        if (!alreadyInStock) {
            Stock s = new Stock();
            s.setClothes(c);
            em.persist(s);
        }
    }


}
