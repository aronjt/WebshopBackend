package org.progmatic.webshop.returnmodel;

/**
 *
 */
public class Message extends Feedback {

    private String message;

    public Message() {}

    public Message(String message) {
        this.message = message;
    }

    public Message(boolean success, String message) {
        setSuccess(success);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
