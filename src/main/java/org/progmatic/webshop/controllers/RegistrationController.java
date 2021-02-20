package org.progmatic.webshop.controllers;

import org.progmatic.webshop.jpareps.ConfirmationTokenData;
import org.progmatic.webshop.jpareps.UserData;
import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.model.ConfirmationToken;
import org.progmatic.webshop.model.User;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.Message;
import org.progmatic.webshop.services.EmailSenderService;
import org.progmatic.webshop.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@RestController
public class RegistrationController {
    private final UserData userRepository;
    private final ConfirmationTokenData confirmationTokenRepository;
    private final RegistrationService registrationService;
    @PersistenceContext
    EntityManager entityManager;
    EmailSenderService sendEmail;

    /* TODO
        try this (because in application.properties, there is this link):
        @Value("${value.of.url}")
     */
    @Value("${value.of.url}")
    private String valueOfUrl;

    @Autowired
    public RegistrationController(EmailSenderService sendEmail, EntityManager entityManager, UserData userRepository,
                                  ConfirmationTokenData confirmationTokenRepository, RegistrationService registrationService) {
        this.sendEmail = sendEmail;
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.registrationService = registrationService;
    }

    @PostMapping(value = "/register")
    public Feedback registerUser(@RequestBody RegisterUserDto registerUserDto) {
        User existingUser = userRepository.findByUsername(registerUserDto.getEmail());
        Message message;

        ConfirmationToken confirmationToken = null;
        if (existingUser != null) {
//            confirmationToken = entityManager.createQuery("SELECT t FROM ConfirmationToken t" +
//                    " WHERE t.user.id = :id order by t.enableDate DESC  ", ConfirmationToken.class)
//                    .setParameter("id", existingUser.getId()).getResultList().get(0);
//            if (registrationService.checkTheDate(confirmationToken.getCreatedDate())) {
//                feedbackDto.setMessage("Please try again later(2 days after previous confirmation.)");
//                return feedbackDto;
//            } else {

                confirmationToken = new ConfirmationToken(existingUser);
                Date tomorrow = registrationService.addDays(confirmationToken.getCreatedDate(), 1);
                confirmationToken.setEnableDate(tomorrow);
                confirmationTokenRepository.save(confirmationToken);
                sendEmail.sendEmail(existingUser, EmailSenderHelper.REGISTRATION, confirmationToken,valueOfUrl);
                message =  new Message(true, "Confirmation token sent to Old User");
//            }
        } else {
            User user = new User(registerUserDto);
            userRepository.save(user);

            confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);

            sendEmail.sendEmail(user, EmailSenderHelper.REGISTRATION, confirmationToken, valueOfUrl);
            message =  new Message(true, "Confirmation token sent to New User");
        }

        return message;
    }


    @GetMapping(value = "/confirm-account")
    public Feedback confirmUserAccount(@RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        Message message;
        if (token != null) {
            User user = userRepository.findByUsername(token.getUser().getUsername());
            user.setEnabled(true);
//           T O D O letesztelni hogy save nélkül működik -e
//            válasz nem
//            köszi, Máté, hogy benne hagytad!!! ^^ ~Ria
            userRepository.save(user);
            message = new Message(true, "account verified");
        } else {
            message = new Message("The link is invalid or broken!");
        }

        return message;
    }
}