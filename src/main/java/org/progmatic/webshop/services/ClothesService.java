package org.progmatic.webshop.services;

import org.progmatic.webshop.dto.ClothDto;
import org.progmatic.webshop.dto.PurchasedClothesDto;
import org.progmatic.webshop.dto.StockDto;
import org.progmatic.webshop.model.Clothes;
import org.progmatic.webshop.model.PurchasedClothes;
import org.progmatic.webshop.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClothesService {

    private static Logger LOG = LoggerFactory.getLogger(ClothesService.class);

    @PersistenceContext
    EntityManager em;

    @Transactional
    public List<ClothDto> getAllClothes() {
        List<Clothes> clothes = em.createQuery("SELECT c FROM Clothes c", Clothes.class).getResultList();
        List<ClothDto> clothDtoList = new ArrayList<>();
        for (Clothes clothe : clothes) {
            clothDtoList.add(new ClothDto(clothe));
        }
        LOG.info("all clothes founded: {} orders in list", clothDtoList.size());
        return clothDtoList;
    }

    @Transactional
    public ClothDto getOneCloth(long id) {
        Clothes clothes = em.find(Clothes.class, id);
        return new ClothDto(clothes);
    }

    @Transactional
    public List<ClothDto> getClothesFromGender(String gender) {
        List<Clothes> clothesList = em.createQuery("SELECT c FROM Clothes c JOIN FETCH c.gender WHERE c.gender.gender = :gender", Clothes.class)
                .setParameter("gender", gender)
                .getResultList();
        List<ClothDto> clothDtoList = new ArrayList<>();
        for (Clothes clothes : clothesList) {
            clothDtoList.add(new ClothDto(clothes));
        }
        LOG.info("all clothes founded: {} orders in list", clothDtoList.size());
        return clothDtoList;
    }

    @Transactional
    public long addNewCloth(ClothDto clothDto) {
        Clothes c = new Clothes(clothDto);
        em.persist(c);
        return c.getId();
    }

    //TODO
    @Transactional
    public long editCloth(long id) {
        em.find(Clothes.class, id);
        return id;
    }

    @Transactional
    public StockDto getStockLevel(long id) {
        List<Stock> stock = em.createQuery("SELECT s FROM Stock s WHERE s.clothes.id = :id", Stock.class).setParameter("id", id).getResultList();
        StockDto stockDto = new StockDto();
        for (Stock stock1 : stock) {
            switch (stock1.getSize()) {
                case "S" -> stockDto.setSizeS(stock1.getQuantity());
                case "M" -> stockDto.setSizeM(stock1.getQuantity());
                case "L" -> stockDto.setSizeL(stock1.getQuantity());
                case "XL" -> stockDto.setSizeXl(stock1.getQuantity());
            }
        }
        return stockDto;
    }

    @Transactional
    public PurchasedClothesDto getPurchasedClothes(long id) {
        PurchasedClothes pc = em.find(PurchasedClothes.class, id);
        return new PurchasedClothesDto(pc);
    }
}
