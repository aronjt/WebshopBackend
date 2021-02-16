package org.progmatic.webshop.controllers;

import org.progmatic.webshop.autodata.ConfirmationTokenData;
import org.progmatic.webshop.autodata.UserData;
import org.progmatic.webshop.dto.FeedbackDto;
import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.model.ConfirmationToken;
import org.progmatic.webshop.services.EmailSenderService;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//TODO restesíteni
@RestController
public class UserAccountController {

    @Autowired
    private UserData userRepository;

@Autowired
EmailSenderService sendEmail;

    @Autowired
    private ConfirmationTokenData confirmationTokenRepository;

//    @Autowired
//    private EmailSenderService emailSenderService;

    @PostMapping(value = "/register")
    public FeedbackDto registerUser(@RequestBody RegisterUserDto registerUserDto) {
        User existingUser = userRepository.findByEmail(registerUserDto.getEmail());
        FeedbackDto feedbackDto = new FeedbackDto();

        if (existingUser != null) {
            feedbackDto.setMessage("This email already exists!");
            return feedbackDto;
        } else {
            User user = new User(registerUserDto);
            userRepository.save(user);

            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);

//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setTo(user.getEmail());
//            mailMessage.setSubject("Complete Registration!");
//            mailMessage.setFrom("chand312902@gmail.com");
//            mailMessage.setText("To confirm your account, please click here : "
//                    + "http://localhost:8080/confirm-account?token=" + confirmationToken.getConfirmationToken());

//            emailSenderService.sendEmail(mailMessage);
            sendEmail.sendEmail(user, EmailSenderHelper.REGISTRATION,confirmationToken);
            feedbackDto.setMessage("successfulRegisteration");
        }

        return feedbackDto;
    }

    @GetMapping(value = "/confirm-account")
    public FeedbackDto confirmUserAccount(@RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        FeedbackDto feedbackDto = new FeedbackDto();
        if (token != null) {
            User user = userRepository.findByEmail(token.getUser().getEmail());
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