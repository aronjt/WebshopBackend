package org.progmatic.webshop.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Helper class for {@link org.progmatic.webshop.controllers.RegistrationController} and
 * {@link org.progmatic.webshop.controllers.PasswordForgottenController}
 */
public class RegistrationService {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationService.class);

    /**
     * Checks that the given time is before LocalDateTime.now().
     * @param enableDate is the time to be checked
     * @return true or false
     */
    public static boolean checkTheDate(LocalDateTime enableDate) {

        if (LocalDateTime.now().isBefore(enableDate)) {
            LOG.info("The last verification email was sent in the last 2 days. After 2 days system can generate new one. ");
            return true;
        } else {
            LOG.info("The last verification email was sent more then 2 days ago.The system now generates new one. ");
            return false;
        }
    }
}
