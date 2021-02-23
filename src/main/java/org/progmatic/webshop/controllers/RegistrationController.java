package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.jpareps.ConfirmationTokenData;
import org.progmatic.webshop.jpareps.UserData;
import org.progmatic.webshop.model.ConfirmationToken;
import org.progmatic.webshop.model.User;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.Message;
import org.progmatic.webshop.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@RestController
public class RegistrationController {
    private final UserData userRepository;
    private final ConfirmationTokenData confirmationTokenRepository;
    @PersistenceContext
    EntityManager entityManager;
    EmailSenderService sendEmail;

    PasswordEncoder encoder;

    @Value("${value.of.url}")
    private String valueOfUrl;

    @Autowired
    public RegistrationController(EmailSenderService sendEmail, EntityManager entityManager, UserData userRepository,
                                  ConfirmationTokenData confirmationTokenRepository, PasswordEncoder encoder) {
        this.sendEmail = sendEmail;
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.encoder = encoder;
    }

    @PostMapping(value = "/register")
    public Feedback registerUser(@RequestBody RegisterUserDto registerUserDto) {
        User existingUser = userRepository.findByUsername(registerUserDto.getEmail());
        Message message;

        ConfirmationToken confirmationToken = null;
        if (existingUser != null) {
            confirmationToken = new ConfirmationToken(existingUser);
            LocalDateTime tomorrow = confirmationToken.getCreatedDate().plusDays(1);
            confirmationToken.setEnableDate(tomorrow);
            confirmationTokenRepository.save(confirmationToken);
            sendEmail.sendEmail(existingUser, EmailSenderHelper.REGISTRATION, confirmationToken, valueOfUrl);
            message = new Message(true, "Confirmation token sent to Old User");
        } else {
            User user = new User(registerUserDto);
            user.setPassword(encoder.encode(registerUserDto.getPassword()));
            userRepository.save(user);

            confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);

            sendEmail.sendEmail(user, EmailSenderHelper.REGISTRATION, confirmationToken, valueOfUrl);
            message = new Message(true, "Confirmation token sent to New User");
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