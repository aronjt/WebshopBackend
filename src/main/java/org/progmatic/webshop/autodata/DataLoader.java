package org.progmatic.webshop.autodata;

import org.progmatic.webshop.helpers.ClothDataHelper;
import org.progmatic.webshop.helpers.UserDataHelper;
import org.progmatic.webshop.model.Clothes;
import org.progmatic.webshop.model.Gender;
import org.progmatic.webshop.model.Type;
import org.progmatic.webshop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DataLoader.class);

    private PasswordEncoder encoder;

    private UserData userData;
    private TypeData typeData;
    private GenderData genderData;
    private ClothesData clothesData;

    @Autowired
    public DataLoader(PasswordEncoder encoder, UserData userData, TypeData typeData, GenderData genderData,
                      ClothesData clothesData) {
        this.encoder = encoder;
        this.userData = userData;
        this.typeData = typeData;
        this.genderData = genderData;
        this.clothesData = clothesData;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        createAdmin();
        createTypes();
        createGenders();
        createClothes();
    }

    public void createAdmin() {
        long usersNum = userData.count();

        /* TODO
            password should be encoded
         */
        if (usersNum == 0) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setPassword("MRirdatlan007");
            admin.setEmail("webshopmnager@gmail.com");
            admin.setAddress("1234 Bp., Pf. 666.");
            admin.setPhoneNumber("06 1 234 5678");
            admin.setUserRole(UserDataHelper.ROLE_ADMIN);
            userData.save(admin);

            LOG.info("admin created with email {}, and added to database", admin.getUsername());
        }
    }

    public void createTypes() {
        long typeNum = typeData.count();

        if (typeNum == 0) {
            Type shirt = new Type();
            shirt.setType(ClothDataHelper.TYPE_TSHIRT);
            typeData.save(shirt);

            LOG.info("{} type added to database", shirt.getType());

            Type pullover = new Type();
            pullover.setType(ClothDataHelper.TYPE_PULLOVER);
            typeData.save(pullover);

            LOG.info("{} type added to database", pullover.getType());

            Type pants = new Type();
            pants.setType(ClothDataHelper.TYPE_PANTS);
            typeData.save(pants);

            LOG.info("{} type added to database", pants.getType());
        }
    }

    public void createGenders() {
        long genderNum = genderData.count();

        if (genderNum == 0) {
            Gender male = new Gender();
            male.setGender(ClothDataHelper.GENDER_MALE);
            genderData.save(male);

            LOG.info("{} gender added to database", male.getGender());

            Gender female = new Gender();
            female.setGender(ClothDataHelper.GENDER_FEMALE);
            genderData.save(female);

            LOG.info("{} gender added to database", female.getGender());

            Gender child = new Gender();
            child.setGender(ClothDataHelper.GENDER_CHILD);
            genderData.save(child);

            LOG.info("{} gender added to database", child.getGender());

            Gender unisex = new Gender();
            unisex.setGender(ClothDataHelper.GENDER_UNISEX);
            genderData.save(unisex);

            LOG.info("{} gender added to database", unisex.getGender());

        }
    }

    public void createClothes() {
        long clothNum = clothesData.count();

        if (clothNum == 0) {
            createShirts();
        }
    }

    private void createShirts() {
        Type type = typeData.findByType(ClothDataHelper.TYPE_TSHIRT);
        Gender male = genderData.findByGender(ClothDataHelper.GENDER_MALE);
        Gender female = genderData.findByGender(ClothDataHelper.GENDER_FEMALE);
        Gender unisex = genderData.findByGender(ClothDataHelper.GENDER_UNISEX);

        float price1 = 19.99f;
        float price2 = 14.99f;
        float price3 = 24.99f;

        Clothes shirt1 = new Clothes();
        shirt1.setName("Lansketon T-Shirt");
        shirt1.setDetails("The best shirt for fans!");
        shirt1.setPrice(price1);
        shirt1.setColor(ClothDataHelper.COLOR_BLACK);
        shirt1.setType(type);
        shirt1.setGender(male);
        clothesData.save(shirt1);

        LOG.info("added new {} to the database with name and price: {}, {}",
                type.getType(), shirt1.getName(), shirt1.getPrice());

        Clothes shirt2 = new Clothes();
        shirt2.setName("Hello Kitty");
        shirt2.setDetails("Cutest t-shirt of the world!");
        shirt2.setPrice(price2);
        shirt2.setColor(ClothDataHelper.COLOR_PINK);
        shirt2.setType(type);
        shirt2.setGender(female);
        clothesData.save(shirt2);

        LOG.info("added new {} to the database with name and price: {}, {}",
                type.getType(), shirt2.getName(), shirt2.getPrice());

        Clothes shirt3 = new Clothes();
        shirt3.setName("Progmatic");
        shirt3.setDetails("Best Academy of The World");
        shirt3.setPrice(price3);
        shirt3.setColor(ClothDataHelper.COLOR_WHITE);
        shirt3.setType(type);
        shirt3.setGender(unisex);
        clothesData.save(shirt3);

        LOG.info("added new {} to the database with name and price: {}, {}",
                type.getType(), shirt3.getName(), shirt3.getPrice());

    }

}
