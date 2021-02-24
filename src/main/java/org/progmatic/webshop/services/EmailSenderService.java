package org.progmatic.webshop.services;

import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.jpareps.AdminData;
import org.progmatic.webshop.jpareps.EmailData;
import org.progmatic.webshop.model.ConfirmationToken;
import org.progmatic.webshop.model.Email;
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

    private String valueOfUrl;
    private AdminData adminData;
    private String fromPassword;


    @Value("${spring.mail.host}")
    private String emailWebSite;
    @Value("${spring.mail.port}")
    private String port;
    @Value("${value.of.mainpage}")
    private String mainpage;
    private EmailData emailData;

    @Autowired
    public EmailSenderService(AdminData adminData, EmailData emailData) {
        this.adminData = adminData;
        this.emailData = emailData;
    }


    public Email setEmail(String messageType) {
        Email emailDataByMessageType = emailData.findByMessageType(messageType);
        return emailDataByMessageType;
    }

    @Transactional
    public void sendEmail(User toUser, String messageType, ConfirmationToken confirmationToken, String valueOfUrl) {
        this.valueOfUrl = valueOfUrl;

        org.progmatic.webshop.model.AdminData aData= adminData.findAdminDataById(EmailSenderHelper.id);
        fromPassword = aData.getSecret();
        Email emailDataByMessageType = setEmail(messageType);

        String toEmail = toUser.getUsername();
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", emailWebSite);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", port);
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
                            + getMessageTextWithConfirmationToken(toUser, emailDataByMessageType, confirmationToken),
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
}