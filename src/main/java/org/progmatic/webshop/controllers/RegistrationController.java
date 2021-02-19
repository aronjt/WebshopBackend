package org.progmatic.webshop.controllers;

import org.progmatic.webshop.autodata.ConfirmationTokenData;
import org.progmatic.webshop.autodata.UserData;
import org.progmatic.webshop.dto.FeedbackDto;
import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.model.ConfirmationToken;
import org.progmatic.webshop.model.User;
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
    public FeedbackDto registerUser(@RequestBody RegisterUserDto registerUserDto) {
        User existingUser = userRepository.findByUsername(registerUserDto.getEmail());
        FeedbackDto feedbackDto = new FeedbackDto();

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
                feedbackDto.setMessage("Confirmation token sent to Old User");
//            }
        } else {
            User user = new User(registerUserDto);
            userRepository.save(user);

            confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);

            sendEmail.sendEmail(user, EmailSenderHelper.REGISTRATION, confirmationToken, valueOfUrl);
            feedbackDto.setMessage("Confirmation token sent to New User");
        }

        return feedbackDto;
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