package org.progmatic.webshop.testconfig;

import org.progmatic.webshop.configurators.DataLoader;
import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.helpers.ClothDataHelper;
import org.progmatic.webshop.jpareps.*;
import org.progmatic.webshop.model.*;
import org.progmatic.webshop.services.ImageService;
import org.progmatic.webshop.testservice.TestService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

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
        addTokensToUser();
        addCloth();
    }

    @Transactional
    void addUserToDB() {
        User user = new User(createUser());
        em.persist(user);
    }

    @Transactional
    void addTokensToUser() {
        ConfirmationToken valid = createToken("burkolorant@gmail.com", LocalDateTime.now().plusDays(100));
        ConfirmationToken invalid = createToken(TestService.USER_EMAIL, LocalDateTime.now().minusDays(100));
        if (valid != null) {
            em.persist(valid);
        }
        if (invalid != null) {
            em.persist(invalid);
        }
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

    public ConfirmationToken createToken(String username, LocalDateTime enableDate) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :uName", User.class)
                    .setParameter("uName", username)
                    .getSingleResult();
            ConfirmationToken token = new ConfirmationToken(user);
            token.setEnableDate(enableDate);
            return token;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public void addCloth() {
        Type t = getTShirtType();
        Gender g = getFemaleGender();

        Clothes c = new Clothes();
        c.setName("Little Bunny");
        c.setDetails("Some description...");
        c.setPrice(99.99f);
        c.setColor(ClothDataHelper.COLOR_PINK);
        c.setType(t);
        c.setGender(g);
        em.persist(c);
    }

    public Gender getFemaleGender() {
        try {
            return em.createQuery("SELECT g FROM Gender g WHERE g.gender = :tName", Gender.class)
                    .setParameter("tName", ClothDataHelper.GENDER_FEMALE)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Type getTShirtType() {
        try {
            return em.createQuery("SELECT t FROM Type t WHERE t.type = :tName", Type.class)
                    .setParameter("tName", ClothDataHelper.TYPE_TSHIRT)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
