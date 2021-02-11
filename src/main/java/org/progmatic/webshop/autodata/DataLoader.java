package org.progmatic.webshop.autodata;

import org.progmatic.webshop.helpers.ClothDataHelper;
import org.progmatic.webshop.helpers.UserDataHelper;
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

    @Autowired
    public DataLoader(PasswordEncoder encoder, UserData userData, TypeData typeData) {
        this.encoder = encoder;
        this.userData = userData;
        this.typeData = typeData;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        createAdmin();
        createTypes();
    }

    public void createAdmin() {
        long usersNum = userData.count();

        /* TODO
            password should be encoded
         */
        if (usersNum == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("MRirdatlan007");
            admin.setEmail("webshopmnager@gmail.com");
            admin.setAddress("1234 Bp., Pf. 666.");
            admin.setPhoneNumber("06 1 234 5678");
            admin.setUserRole(UserDataHelper.ROLE_ADMIN);
            userData.save(admin);

            LOG.debug("admin created");
        }
    }

    public void createTypes() {
        long typeNum = typeData.count();

        if (typeNum == 0) {
            Type shirt = new Type();
            shirt.setType(ClothDataHelper.TYPE_TSHIRT);
            typeData.save(shirt);

            LOG.debug("{} type created", shirt.getType());

            Type pullover = new Type();
            pullover.setType(ClothDataHelper.TYPE_PULLOVER);
            typeData.save(pullover);

            LOG.debug("{} type created", pullover.getType());

            Type pants = new Type();
            pants.setType(ClothDataHelper.TYPE_PANTS);
            typeData.save(pants);

            LOG.debug("{} type created", pants.getType());
        }
    }

}
