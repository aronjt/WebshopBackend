package org.progmatic.webshop.emails;

import org.progmatic.webshop.model.User;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class SendEmail {
    public SendEmail(User toUser, Email email) {

//        final String password = getPasswordFromFirstUser;
        String fromPassword = null;
//        String fromEmail = getEmailAdressFromFirstUser;
        String fromEmail = null;
//        String toEmail = user.getEmailAdress();
        String toEmail = null;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

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
        String messageText = email.getMessageText();
        String subject = email.getSubject();
        try {
            message.setSender(addressFrom);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            Multipart emailcontect = new MimeMultipart();

            MimeBodyPart textBodyPart = new MimeBodyPart();
            User user = null;
            textBodyPart.setText("Hello " +
//                    user.getUsername +
                    ",\n" +
                    messageText +
                    "\n For more info visit our website.");

            MimeBodyPart imageAttachment = new MimeBodyPart();
            if (!(email.getAttachedFile().equals(null))) {
                imageAttachment.attachFile(email.getAttachedFile());
            }
            emailcontect.addBodyPart(textBodyPart);
            emailcontect.addBodyPart(imageAttachment);

            message.setContent(emailcontect);

            transport.connect();
            Transport.send(message);
            transport.close();
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}