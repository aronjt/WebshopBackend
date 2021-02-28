package org.progmatic.webshop.dto;

/* TODO
    never used class, should be removed
 */

public class FeedbackDto {

    private long id;
    private String message;

    public FeedbackDto() {
    }

    public FeedbackDto(long id, String message) {
        this.id = id;
        this.message = message;
    }

    public FeedbackDto(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
