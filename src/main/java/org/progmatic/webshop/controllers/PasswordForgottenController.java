package org.progmatic.webshop.controllers;

import org.progmatic.webshop.jpareps.ConfirmationTokenData;
import org.progmatic.webshop.jpareps.UserData;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.model.ConfirmationToken;
import org.progmatic.webshop.model.User;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.Message;
import org.progmatic.webshop.services.EmailSenderService;
import org.progmatic.webshop.services.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@RestController
public class PasswordForgottenController {
    private final static Logger LOG = LoggerFactory.getLogger(PasswordForgottenController.class);

    private final UserData userRepository;
    private final ConfirmationTokenData confirmationTokenRepository;
    @PersistenceContext
    EntityManager entityManager;
    EmailSenderService sendEmail;
    PasswordEncoder passwordEncoder;

    @Value("${value.of.password.url}")
    private String valueOfUrl;

    @Autowired
    public PasswordForgottenController(EmailSenderService sendEmail, EntityManager entityManager, UserData userRepository,
                                       ConfirmationTokenData confirmationTokenRepository,
                                        PasswordEncoder passwordEncoder) {
        this.sendEmail = sendEmail;
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @PostMapping(value = "/forgottenpassword/{emailAdress}")
    public Feedback newPassword(@PathVariable("emailAdress") String emailAdress) {
        User existingUser = userRepository.findByUsername(emailAdress);

        Message feedback = new Message();
        ConfirmationToken confirmationToken;
        if (existingUser != null) {
            confirmationToken = new ConfirmationToken(existingUser);
            LocalDateTime tomorrow = confirmationToken.getCreatedDate().plusDays(1);
            confirmationToken.setEnableDate(tomorrow);
            confirmationTokenRepository.save(confirmationToken);
            sendEmail.prepareConfirmationEmail(existingUser, EmailSenderHelper.REGISTRATION, confirmationToken);
            feedback.setSuccess(true);
            feedback.setMessage("Password Confirmation token sent to User");
            LOG.info("Password Confirmation token sent to User");
//            }
        } else {
            feedback.setMessage("User not found");
            LOG.info("User not found");
        }

        return feedback;
    }


    @GetMapping(value = "/confirm-password")
    public Feedback confirmUserAccount(@RequestParam("token") String userConfirmationToken,
                                       @RequestParam("password") String newPassword,
                                       @RequestParam("username") String userName) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(userConfirmationToken);

        Message feedback = new Message();
//       todo ellenőrizni a enabledate et
        if (token != null) {
            LOG.info("Token is not null");
            if (RegistrationService.checkTheDate(token.getEnableDate())) {
                LOG.info("Token is not expired");
                if (userName.equals(token.getUser().getUsername())) {
                    User user = userRepository.findByUsername(token.getUser().getUsername());
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    feedback.setSuccess(true);
                    feedback.setMessage("New password verified");
                    LOG.info("Email validated");
                } else {
                    LOG.info("Email not valid");
                }

            } else {
                feedback.setMessage("The link is invalid or broken! Please try again with another token");
                LOG.info("Token is expired");
            }

        }
        return feedback;
    }

}
