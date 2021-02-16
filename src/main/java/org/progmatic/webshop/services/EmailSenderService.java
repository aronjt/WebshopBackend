package org.progmatic.webshop.services;

import org.progmatic.webshop.autodata.EmailData;
import org.progmatic.webshop.model.Email;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.model.User;
import org.progmatic.webshop.model.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Properties;
@Service
public class EmailSenderService {
//    @Autowired
//    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailData emailData;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    public EmailSenderService() {
    }
public void preparationForSending(){

}
public Email setEmail (String messageType){
    Email emailDataByMessageType = emailData.findByMessageType(messageType);
    return emailDataByMessageType;
}
    @Transactional
    public void sendEmail(User toUser, String messageType, ConfirmationToken confirmationToken) {
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

        String fromEmail =EmailSenderHelper.ADMIN_EMAIL_ADDRESS;
        final User fromUser = (User) myUserDetailsService.loadUserByUsername(fromEmail);
        String fromPassword = fromUser.getPassword();
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
            Multipart emailcontect = new MimeMultipart();
            MimeBodyPart textBodyPart =new MimeBodyPart();
            if (messageType.equals(EmailSenderHelper.REGISTRATION)) {
                textBodyPart.setText(getMessageTextWithConfirmationToken(toUser,emailDataByMessageType,confirmationToken));
            }
            else {
                textBodyPart.setText(getMessageText(toUser, emailDataByMessageType));
            }
            MimeBodyPart imageAttachment = new MimeBodyPart();
            if (emailDataByMessageType.getAttachedFile() != null) {
                imageAttachment.attachFile(emailDataByMessageType.getAttachedFile());
            }
            emailcontect.addBodyPart(textBodyPart);
//            emailcontect.addBodyPart(imageAttachment);

            message.setContent(emailcontect);

            transport.connect();
            Transport.send(message);
            transport.close();
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
    public String getMessageText (User toUser, Email email) throws MessagingException {
        String text ="Hello " +
                toUser.getFirstName() +
                ",\n" +
                email.getMessageText() +
                "\n For more info visit our website.";
        return text;
    }
    public String getMessageTextWithConfirmationToken(User toUser, Email email, ConfirmationToken confirmationToken) throws MessagingException {
//        ConfirmationToken confirmationToken = new ConfirmationToken(toUser);

//        confirmationTokenRepository.save(confirmationToken);
        String text ="Hello " +
                toUser.getFirstName() +
                ",\n" +
                email.getMessageText()
                + EmailSenderHelper.CONFIRM_URL + confirmationToken.getConfirmationToken()+
                "\n For more info visit our website.";
        return text;
    }
}