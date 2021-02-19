package org.progmatic.webshop.services;

import org.progmatic.webshop.autodata.EmailData;
import org.progmatic.webshop.model.Email;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.model.User;
import org.progmatic.webshop.model.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import javax.transaction.Transactional;
import java.util.Properties;

@Service
public class EmailSenderService {

private String valueOfUrl;

    /* TODO
        try this (because in application.properties, there is this link):
        @Value("${value.of.password}")
     */
    @Value("${value.of.password}")
    private String fromPassword;

    @Autowired
    private EmailData emailData;


    public EmailSenderService() {
    }

    public Email setEmail(String messageType) {
        Email emailDataByMessageType = emailData.findByMessageType(messageType);
        return emailDataByMessageType;
    }

    @Transactional
    public void sendEmail(User toUser, String messageType, ConfirmationToken confirmationToken, String valueOfUrl) {
     this.valueOfUrl=valueOfUrl;
        Email emailDataByMessageType = setEmail(messageType);

        String toEmail = toUser.getUsername();
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        String fromEmail = EmailSenderHelper.ADMIN_EMAIL_ADDRESS;
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, fromPassword);
                    }
                });

        Transport transport = null;
        InternetAddress addressFrom = null;
        try {
            transport = session.getTransport();
            addressFrom = new InternetAddress(fromEmail);
        } catch (NoSuchProviderException | AddressException e) {
            e.printStackTrace();
        }
        MimeMessage message = new MimeMessage(session);


        String subject = emailDataByMessageType.getSubject();
        try {
            message.setSender(addressFrom);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);



            message.setContent(
                    "<h1>Verification message</h1>"
                            +getMessageTextWithConfirmationToken(toUser, emailDataByMessageType, confirmationToken),
                    "text/html");
            transport.connect();
            Transport.send(message);
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String getMessageText(User toUser, Email email) throws MessagingException {
        String text = "Hello " +
                toUser.getFirstName() +
                ",\n" +
                email.getMessageText() +
                "\n For more info visit our website.";
        return text;
    }

    public String getMessageTextWithConfirmationToken(User toUser, Email email, ConfirmationToken confirmationToken) throws MessagingException {
        String text = "Hello " +
                toUser.getFirstName() +
                ",\n" +
                email.getMessageText()
                + valueOfUrl+ confirmationToken.getConfirmationToken() +
                "\n For more info visit our website.";
        return text;
    }
}