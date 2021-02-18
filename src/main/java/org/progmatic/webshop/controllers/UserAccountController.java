package org.progmatic.webshop.controllers;

import org.progmatic.webshop.autodata.ConfirmationTokenData;
import org.progmatic.webshop.autodata.UserData;
import org.progmatic.webshop.dto.FeedbackDto;
import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.model.ConfirmationToken;
import org.progmatic.webshop.model.User;
import org.progmatic.webshop.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@RestController
public class UserAccountController {
    private final UserData userRepository;
    @PersistenceContext
    EntityManager entityManager;
    EmailSenderService sendEmail;
    private final ConfirmationTokenData confirmationTokenRepository;

    @Autowired
    public UserAccountController(EmailSenderService sendEmail, EntityManager entityManager, UserData userRepository, ConfirmationTokenData confirmationTokenRepository) {
        this.sendEmail = sendEmail;
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @PostMapping(value = "/register")
    public FeedbackDto registerUser(@RequestBody RegisterUserDto registerUserDto) {
        User existingUser = userRepository.findByUsername(registerUserDto.getEmail());
        FeedbackDto feedbackDto = new FeedbackDto();

        ConfirmationToken confirmationToken = null;
        if (existingUser != null) {
            confirmationToken = entityManager.createQuery("SELECT t FROM ConfirmationToken t" +
                    " WHERE t.user.id = :id order by t.enableDate DESC  ", ConfirmationToken.class)
                    .setParameter("id", existingUser.getId()).getResultList().get(0);
            if (checkTheDate(confirmationToken.getCreatedDate())) {
                feedbackDto.setMessage("This email already exists!");
                return feedbackDto;
            } else {

                confirmationToken = new ConfirmationToken(existingUser);
                Date tomorrow = addDays(confirmationToken.getCreatedDate(), 1);
                confirmationToken.setEnableDate(tomorrow);
                confirmationTokenRepository.save(confirmationToken);
                sendEmail.sendEmail(existingUser, EmailSenderHelper.REGISTRATION, confirmationToken);
                feedbackDto.setMessage("Confirmation token sent to Old User");
            }
        } else {
            User user = new User(registerUserDto);
            userRepository.save(user);

            confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);

            sendEmail.sendEmail(user, EmailSenderHelper.REGISTRATION, confirmationToken);
            feedbackDto.setMessage("Confirmation token sent to New User");
        }

        return feedbackDto;
    }

    public boolean checkTheDate(Date createdDate) {
//        ez amúgy localdate.now
        String sourceDate = String.valueOf(LocalDate.now());
//        String sourceDate = "2021-02-20";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date myDate = null;
        try {
            myDate = format.parse(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        java.util.Date tomorrow = addDays(createdDate, 1);
        return tomorrow.after(myDate);
    }

    public java.util.Date addDays(java.util.Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }


    @GetMapping(value = "/confirm-account")
    public FeedbackDto confirmUserAccount(@RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        FeedbackDto feedbackDto = new FeedbackDto();
        if (token != null) {
            User user = userRepository.findByUsername(token.getUser().getUsername());
            user.setEnabled(true);
//           T O D O letesztelni hogy save nélkül működik -e
//            válasz nem
            userRepository.save(user);
            feedbackDto.setMessage("accountVerified");
        } else {
            feedbackDto.setMessage("The link is invalid or broken!");
        }

        return feedbackDto;
    }
}