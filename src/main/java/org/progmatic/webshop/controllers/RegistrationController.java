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
import org.progmatic.webshop.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

/**
 * Controls actions related to user registration.<br>
 *     Containing endpoints:
 *     <ul>
 *         <li>/register, post</li>
 *         <li>/confirm-account?token=, get</li>
 *     </ul>
 */
@RestController
public class RegistrationController {

    @PersistenceContext
    EntityManager entityManager;

    private final UserData userRepository;
    private final ConfirmationTokenData confirmationTokenRepository;
    private final EmailSenderService sendEmail;
    private final MyUserDetailsService service;

    @Value("${value.of.url}")
    private String valueOfUrl;

    @Autowired
    public RegistrationController(EmailSenderService sendEmail, EntityManager entityManager, UserData userRepository,
                                  ConfirmationTokenData confirmationTokenRepository, MyUserDetailsService service) {
        this.sendEmail = sendEmail;
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.service = service;
    }

    /**
     * Endpoint for registering a new user.<br>
     *     After a successful registration, an account confirmation email will be sent.<br>
     *     See {@link EmailSenderService#prepareConfirmationEmail(User, String, ConfirmationToken, String)} for more information.
     * @param registerUserDto is the data of the new user
     * @return a confirm {@link Message} about the registration process
     */
    @PostMapping(value = "/register")
    public Feedback registerUser(@RequestBody RegisterUserDto registerUserDto) {
        User existingUser = userRepository.findByUsername(registerUserDto.getEmail());
        Message message;

        ConfirmationToken confirmationToken;
        if (existingUser != null) {
            message = new Message(false, "This email address already exists.(login or confirm the token)");
        } else {
            User user = new User(registerUserDto);
            service.createUser(user);
            confirmationToken = new ConfirmationToken(user);
            confirmationTokenRepository.save(confirmationToken);
            sendEmail.prepareConfirmationEmail(user, EmailSenderHelper.REGISTRATION, confirmationToken, valueOfUrl);
            message = new Message(true, "Confirmation token sent to New User");
        }
        return message;
    }

    /**
     * Endpoint for confirming the new user's account.<br>
     *     See {@link EmailSenderService#prepareConfirmationEmail(User, String, ConfirmationToken, String)} for more information.
     * @param confirmationToken is the token that has been sent in email after the registration
     * @return a confirm {@link Message} about the confirmation process
     */
    @GetMapping(value = "/confirm-account")
    public Feedback confirmUserAccount(@RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        Message message;
        if (token != null) {
            User user = userRepository.findByUsername(token.getUser().getUsername());
            if (token.getEnableDate().isAfter(LocalDateTime.now())) {
                user.setEnabled(true);
                userRepository.save(user);
                message = new Message(true, "Account verified");
            } else {
                ConfirmationToken newToken = new ConfirmationToken(user);
                confirmationTokenRepository.save(newToken);
                sendEmail.prepareConfirmationEmail(user, EmailSenderHelper.REGISTRATION, newToken, valueOfUrl);
                message = new Message("This token is broken ! We sent you new one to your email address.");
            }
        } else {
            message = new Message("The token is invalid!");
        }
        return message;
    }
}