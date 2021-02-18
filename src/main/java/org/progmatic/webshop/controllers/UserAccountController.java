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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@RestController
public class UserAccountController {

    @Autowired
    EmailSenderService sendEmail;
    @Autowired
    private UserData userRepository;
    @Autowired
    private ConfirmationTokenData confirmationTokenRepository;
private LocalDate localdate;

    @PostMapping(value = "/register")
    public FeedbackDto registerUser(@RequestBody RegisterUserDto registerUserDto) {
        User existingUser = userRepository.findByUsername(registerUserDto.getEmail());
        FeedbackDto feedbackDto = new FeedbackDto();

        ConfirmationToken confirmationToken=null;
        if (existingUser != null ) {
            confirmationToken = confirmationTokenRepository.findByUserIdAndEnable(existingUser.getId(),true);
        if (checkTheDate(confirmationToken.getCreatedDate(),LocalDate.now())) {
                feedbackDto.setMessage("This email already exists!");
                return feedbackDto;
            }
        else{
            confirmationToken.setEnable(false);
            confirmationTokenRepository.save(confirmationToken);

            confirmationToken = new ConfirmationToken(existingUser);
            confirmationTokenRepository.save(confirmationToken);
            sendEmail.sendEmail(existingUser, EmailSenderHelper.REGISTRATION, confirmationToken);
            feedbackDto.setMessage("successfulRegisteration");
        }
        }
         else {
             User user = new User(registerUserDto);
            userRepository.save(user);

             confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);

//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setTo(user.getEmail());
//            mailMessage.setSubject("Complete Registration!");
//            mailMessage.setFrom("chand312902@gmail.com");
//            mailMessage.setText("To confirm your account, please click here : "
//                    + "http://localhost:8080/confirm-account?token=" + confirmationToken.getConfirmationToken());

//            emailSenderService.sendEmail(mailMessage);
            sendEmail.sendEmail(user, EmailSenderHelper.REGISTRATION, confirmationToken);
            feedbackDto.setMessage("successfulRegisteration");
        }

        return feedbackDto;
    }
    public boolean checkTheDate(Date createdDate, LocalDate now){
//        ez amúgy localdate.now
//        String sourceDate = String.valueOf(now);
        String sourceDate = "2021-02-19";

//        String sourceDate = String.valueOf(LocalDate.now());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date myDate = null;
        try {
            myDate =  format.parse(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        java.util.Date tomorrow= addDays(createdDate,1);
        if (tomorrow.after(myDate)){
            return true;
        }
        return false;
    }
    public java.util.Date addDays(java.util.Date date, int days)
    {
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