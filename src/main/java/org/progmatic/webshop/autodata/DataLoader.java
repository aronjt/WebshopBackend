package org.progmatic.webshop.autodata;

import org.progmatic.webshop.helpers.ClothDataHelper;
import org.progmatic.webshop.helpers.UserDataHelper;
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

@Component
public class DataLoader implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DataLoader.class);

    private PasswordEncoder encoder;

    private UserData userData;
    private TypeData typeData;
    private GenderData genderData;

    @Autowired
    public DataLoader(PasswordEncoder encoder, UserData userData, TypeData typeData, GenderData genderData) {
        this.encoder = encoder;
        this.userData = userData;
        this.typeData = typeData;
        this.genderData = genderData;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        createAdmin();
        createTypes();
        createGenders();
    }

    public void createAdmin() {
        long usersNum = userData.count();

        /* TODO
            password should be encoded
         */
        if (usersNum == 0) {
            User admin = new User();
          //  admin.setUsername("admin");
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

}
