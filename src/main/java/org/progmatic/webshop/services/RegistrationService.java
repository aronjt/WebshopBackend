package org.progmatic.webshop.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Service
public class RegistrationService {
    private static Logger LOG = LoggerFactory.getLogger(RegistrationService.class);

    public boolean checkTheDate(Date createdDate) {
        String sourceDate = String.valueOf(LocalDate.now());

        // Teszteléshez       String sourceDate = "2021-02-20";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date myDate = null;
        try {
            myDate = format.parse(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        java.util.Date tomorrow = addDays(createdDate, 1);
        if (tomorrow.after(myDate)) {
            LOG.info("The last verification email was sent in the last 2 days. After 2 days system can generate new one. ");
            return true;
        } else {
            LOG.info("The last verification email was sent more then 2 days ago.The system now generates new one. ");
            return false;
        }
    }

    public java.util.Date addDays(java.util.Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}
