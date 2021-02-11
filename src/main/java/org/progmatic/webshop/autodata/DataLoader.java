package org.progmatic.webshop.autodata;

import org.progmatic.webshop.helpers.UserDataHelper;
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

    @Autowired
    public DataLoader(PasswordEncoder encoder, UserData userData) {
        this.encoder = encoder;
        this.userData = userData;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        createAdmin();
    }

    public void createAdmin() {
        long usersNum = userData.count();

        if (usersNum == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("MRirdatlan007"));
            admin.setEmail("webshopmnager@gmail.com");
            admin.setAddress("1234 Bp., Pf. 666.");
            admin.setPhoneNumber("06 1 234 5678");
            admin.setUserRole(UserDataHelper.ROLE_ADMIN);
            userData.save(admin);

            LOG.debug("admin created");
        }
    }

}
