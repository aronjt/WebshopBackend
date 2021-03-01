package org.progmatic.webshop.model;

import javax.persistence.*;

/**
 * Entity for email sending. The table contains the information about emails that will be sent to user.<br>
 *     Columns:
 *     <ul>
 *         <li>String messageType</li>
 *         <li>String subject</li>
 *         <li>String attachedFile</li>
 *         <li>String messageText</li>
 *         <li>Long emailId</li>
 *     </ul>
 */
@Entity
@Table
public class Email {
    private String messageType;
    private String subject;
    private String attachedFile;
    private String messageText;

    @Id
    @GeneratedValue
    private Long emailId;

    public Email() {}

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAttachedFile() {
        return attachedFile;
    }

    public void setAttachedFile(String attachedFile) {
        this.attachedFile = attachedFile;
    }

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

}