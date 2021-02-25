package org.progmatic.webshop.services;

import org.progmatic.webshop.dto.OrderDto;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.jpareps.AdminData;
import org.progmatic.webshop.jpareps.EmailData;
import org.progmatic.webshop.model.ConfirmationToken;
import org.progmatic.webshop.model.Email;
import org.progmatic.webshop.model.ExtraData;
import org.progmatic.webshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.Properties;

@Service
public class EmailSenderService {
    @Value("${value.of.url}")
    private String valueOfUrl;
    private final AdminData adminData;
    private String fromPassword;


    @Value("${spring.mail.host}")
    private String emailWebSite;
    @Value("${spring.mail.port}")
    private String port;
    @Value("${value.of.mainpage}")
    private String mainpage;
    private final EmailData emailData;

    @Autowired
    public EmailSenderService(AdminData adminData, EmailData emailData) {
        this.adminData = adminData;
        this.emailData = emailData;
    }


    public Email setEmail(String messageType) {
        return emailData.findByMessageType(messageType);
    }

    @Transactional
    public Session prepareSession(
    ) {
        ExtraData aData = adminData.findAdminDataById(EmailSenderHelper.ID);
        fromPassword = aData.getSecret();
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", emailWebSite);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EmailSenderHelper.ADMIN_EMAIL_ADDRESS, fromPassword);
                    }
                });
        return session;
    }

    @Transactional
    public void prepareConfirmationEmail(User toUser, String messageType, ConfirmationToken confirmationToken) {

        ExtraData aData = adminData.findAdminDataById(EmailSenderHelper.ID);
        fromPassword = aData.getSecret();
        Email emailDataByMessageType = setEmail(messageType);

        String toEmail = toUser.getUsername();

        InternetAddress addressFrom = null;
        try {
            addressFrom = new InternetAddress(EmailSenderHelper.ADMIN_EMAIL_ADDRESS);
        } catch (AddressException e) {
            e.printStackTrace();
        }
        MimeMessage message = new MimeMessage(prepareSession());


        String subject = emailDataByMessageType.getSubject();
        try {
            message.setSender(addressFrom);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);


            message.setContent(
                    "<h1>"+ subject+"</h1>"
                            + getMessageTextWithConfirmationToken(toUser, emailDataByMessageType, confirmationToken),
                    "text/html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
transportEmail(message);
    }

    public String getMessageTextWithConfirmationToken(User toUser, Email email, ConfirmationToken confirmationToken) throws MessagingException {
        String text = "<p style=\"font-size:20px\">Hello " +
                toUser.getFirstName() +
                ",<br>" +
                email.getMessageText()
                + "<a href=\""
                + valueOfUrl + confirmationToken.getConfirmationToken()
                + "\">"
                + valueOfUrl + confirmationToken.getConfirmationToken()
                + "</a><br><br>For more info visit <a href=\""
                + mainpage
                + "\">our website</a>.</p>";
        return text;
    }

    @Transactional
    public void transportEmail( MimeMessage message) {

        Transport transport = null;
        try {
            transport = prepareSession().getTransport();
        } catch (
                NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            transport.connect();
            Transport.send(message);
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }}

        @Transactional
        public void prepareSuccesfulOrderEmail(OrderDto order, User loggedInUser,String messageType) {

            ExtraData aData = adminData.findAdminDataById(EmailSenderHelper.ID);
            fromPassword = aData.getSecret();
            Email emailDataByMessageType = setEmail(messageType);

            String toEmail = loggedInUser.getUsername();

            String fromEmail = EmailSenderHelper.ADMIN_EMAIL_ADDRESS;
            InternetAddress addressFrom = null;
            try {
                addressFrom = new InternetAddress(fromEmail);
            } catch (AddressException e) {
                e.printStackTrace();
            }
            MimeMessage message = new MimeMessage(prepareSession());


            String subject = emailDataByMessageType.getSubject();
            try {
                message.setSender(addressFrom);
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
                message.setSubject(subject);


                message.setContent(
                        "<h1>"+subject +"</h1>"+
                        getMessageTextForOrder(loggedInUser,emailDataByMessageType),
                        "text/html");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            transportEmail(message);

        }

    public String getMessageTextForOrder(User toUser, Email email) throws MessagingException {
        String text = "<p style=\"font-size:20px\">Hello " +
                toUser.getFirstName() +
                ",<br>" +
                email.getMessageText()
                + "<a href=\""

                + "</a><br><br>For more info visit <a href=\""
                + mainpage
                + "\">our website</a>.</p>";
        return text;
    }
    }

