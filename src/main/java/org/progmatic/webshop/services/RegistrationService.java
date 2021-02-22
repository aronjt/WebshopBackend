package org.progmatic.webshop.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

//@Service
public class RegistrationService {
    private static Logger LOG = LoggerFactory.getLogger(RegistrationService.class);

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
