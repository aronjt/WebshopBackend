package org.progmatic.webshop.config;

import org.apache.tomcat.jni.Local;
import org.progmatic.webshop.configurators.DataLoader;
import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.jpareps.*;
import org.progmatic.webshop.model.ConfirmationToken;
import org.progmatic.webshop.model.User;
import org.progmatic.webshop.services.ImageService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Component
public class TestDataLoader extends DataLoader {

    @PersistenceContext
    EntityManager em;

    public TestDataLoader(PasswordEncoder encoder, UserData userData, TypeData typeData,
                          GenderData genderData, ClothesData clothesData, StockData stockData,
                          EmailData emailData, OnlineOrderData onlineOrderData, PurchasedClothData pcData,
                          ImageData imageData, ImageService imageservice, AdminData adminData) {
        super(encoder, userData, typeData, genderData, clothesData, stockData, emailData, onlineOrderData,
                pcData, imageData, imageservice, adminData);
    }

    @Override
    public void createUsers() {
        super.createUsers();

        addUserToDB();
        addTokenToUser();
    }

    public RegisterUserDto createUser() {
        RegisterUserDto user = new RegisterUserDto();
        user.setFirstName("Lóránt");
        user.setLastName("Burko");
        user.setPassword("kiscica");
        user.setEmail("burkolorant@gmail.com");
        user.setCountry("Ország");
        user.setZipcode(6969);
        user.setCity("Város");
        user.setAddress("Cím út 1.");
        user.setPhoneNumber("111-11-11");
        return user;
    }

    @Transactional
    void addUserToDB() {
        User user = new User(createUser());
        em.persist(user);
    }

    @Transactional
    void addTokenToUser() {
        User user = em.createQuery("SELECT u FROM User u WHERE u.username = :uName", User.class)
                .setParameter("uName", "burkolorant@gmail.com")
                .getSingleResult();
        ConfirmationToken token = new ConfirmationToken(user);
        token.setEnableDate(LocalDateTime.of(2022,01,01,01,01,01));
        em.persist(token);
    }

}
