package org.progmatic.webshop.returnmodel;

/**
 * Elder class for feedback. The application sends feedbacks to the frontend.<br>
 *     Feedback has only a field (boolean success). This field should be true, if the application worked as wanted,
 *     or false if some problem occurred during the execution.
 */
public class Feedback {

    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
