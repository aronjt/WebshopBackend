package org.progmatic.webshop.testservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.progmatic.webshop.dto.ClothDto;
import org.progmatic.webshop.dto.FilterClothesDto;
import org.progmatic.webshop.dto.OrderDto;
import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.helpers.ClothDataHelper;
import org.progmatic.webshop.helpers.ImageHelper;
import org.progmatic.webshop.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    /* TODO
        more thorough tests needed!
     */

    @Autowired
    private ObjectMapper objectMapper;

    @PersistenceContext
    EntityManager em;

    public static final String USER_EMAIL = "ertekelek@ertek.el";

    public String createJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T createObject(String json, Class<T> toReturn) {
        try {
            return objectMapper.readValue(json, toReturn);
        } catch (Exception e) {
            return null;
        }
    }

    public long getOneImageId() {
        List<Image> images = em.createQuery("SELECT i FROM Image i", Image.class).getResultList();
        if (images.size() > 0) {
            return images.get(0).getId();
        }
        return -1;
    }

    public MockMultipartFile loadImage() {
        try {
            File file = new File("src/main/resources/images/unisex.png");
            FileInputStream input = new FileInputStream(file);
            return new MockMultipartFile("image",
                    "unisex", ImageHelper.PNG, IOUtils.toByteArray(input));
        } catch (Exception e) {
            return null;
        }
    }

    public long getOneOrderId() {
        List<OnlineOrder> orders = em.createQuery("SELECT o FROM OnlineOrder o", OnlineOrder.class)
                .getResultList();
        if (orders.size() > 0) {
            return orders.get(0).getId();
        }
        return -1;
    }

    public OrderDto createOrder() {
        OrderDto order = new OrderDto();
        order.setTotalPrice(39.99f);
        order.setPurchasedClothesList(new ArrayList<>());
        return order;
    }

    public RegisterUserDto createUser() {
        RegisterUserDto user = new RegisterUserDto();
        user.setFirstName("Egyed");
        user.setLastName("Mindmeg");
        user.setPassword("szilvásbukta");
        user.setEmail("mindmegegyed@gmail.com");
        user.setCountry("Valhalla");
        user.setZipcode(666);
        user.setCity("Ygdrassil");
        user.setAddress("Második ág 1.");
        user.setPhoneNumber("666-66-66");
        return user;
    }

    public ConfirmationToken findToken(User user) {
        try {
            return em.createQuery("SELECT t FROM ConfirmationToken t WHERE t.user = :user", ConfirmationToken.class)
                    .setParameter("user", user)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User findUser() {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :uName", User.class)
                    .setParameter("uName", "burkolorant@gmail.com")
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public long getOneUser() {
        List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
        if (users.size() > 0) {
            return users.get(0).getId();
        }
        return 0;
    }

    public long getUserWithOrder() {
        List<OnlineOrder> orders = em.createQuery("SELECT o FROM OnlineOrder o", OnlineOrder.class).getResultList();
        if (orders.size() > 0) {
            return orders.get(0).getUser().getId();
        }
        return 0;
    }

    public RegisterUserDto userForEditUserTest() {
        RegisterUserDto user = new RegisterUserDto();
        user.setFirstName("Erik");
        user.setLastName("Kis M.");
        user.setCountry("Ország");
        user.setZipcode(6969);
        user.setCity("Város");
        user.setAddress("Cím út 1.");
        user.setPhoneNumber("111-11-11");
        return user;
    }

    public long getOneClothId() {
        List<Clothes> clothes = em.createQuery("SELECT c FROM Clothes c", Clothes.class).getResultList();
        if (clothes.size() > 0) {
            return clothes.get(0).getId();
        }
        return -1;
    }

    public ClothDto createClothes() {
        ClothDto c = new ClothDto();
        c.setName("Tesztelde");
        c.setDetails("Fáradt vagyok értelmes szöveghez...");
        c.setPrice(99.99f);
        c.setDetails(ClothDataHelper.COLOR_PINK);
        return c;
    }

    public FilterClothesDto createNullFilter() {
        return new FilterClothesDto();
    }

    public FilterClothesDto createFilterWithSomeData() {
        FilterClothesDto filter = new FilterClothesDto();
        filter.setGender(ClothDataHelper.GENDER_UNISEX);
        filter.setColor(ClothDataHelper.COLOR_BLACK);
        filter.setPriceMin(10);
        return filter;
    }

    public FilterClothesDto createFilterWithAllData() {
        FilterClothesDto filter = new FilterClothesDto();
        filter.setName("a");
        filter.setGender(ClothDataHelper.GENDER_UNISEX);
        filter.setType(ClothDataHelper.TYPE_TSHIRT);
        filter.setColor(ClothDataHelper.COLOR_BLACK);
        filter.setPriceMin(1);
        filter.setPriceMax(3000);
        return filter;
    }

}
