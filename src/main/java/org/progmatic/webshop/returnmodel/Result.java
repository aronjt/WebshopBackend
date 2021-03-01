package org.progmatic.webshop.returnmodel;

/**
 * A generic class used for HTTP responses. Extends {@link Feedback}.<br>
 *     Contains an object of generic type.
 * @param <T>
 */
public class Result<T> extends Feedback {

    private  T t;

    public Result(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
