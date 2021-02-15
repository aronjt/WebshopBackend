package org.progmatic.webshop.verification;

import org.progmatic.webshop.dto.FeedbackDto;
import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.dto.UserDto;
import org.progmatic.webshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

//TODO restesíteni
@RestController
public class UserAccountController {

//    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping(value="/register")
//    feedback Dto val tér vissza
    public FeedbackDto registerUser(@RequestBody RegisterUserDto registerUserDto)
    {
        User existingUser = userRepository.findByEmailIgnoreCase(registerUserDto.getEmail());
        FeedbackDto feedbackDto=new FeedbackDto();

//        if(existingUser != null)
//        {
//           feedbackDto.setMessage("This email already exists!");
//           return feedbackDto;
//        }
//        else
//        {
            org.progmatic.webshop.model.User user =new org.progmatic.webshop.model.User(registerUserDto);
            userRepository.save(user);

            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("chand312902@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

//FeedbackDto feedbackDto=new FeedbackDto();
            feedbackDto.setMessage("successfulRegisteration");
//        }

        return feedbackDto;
    }

    @GetMapping(value="/confirm-account")
    public FeedbackDto confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
FeedbackDto feedbackDto = new FeedbackDto();
        if(token != null)
        {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setEnabled(true);
//           TODO letesztelni hogy save nélkül működik -e
//            válasz nem
            userRepository.save(user);
         feedbackDto.setMessage("accountVerified");
        }
        else
        {
            feedbackDto.setMessage("The link is invalid or broken!");
        }

        return feedbackDto;
    }
    // getters and setters
}