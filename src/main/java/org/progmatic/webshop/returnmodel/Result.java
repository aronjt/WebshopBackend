package org.progmatic.webshop.returnmodel;

import org.progmatic.webshop.dto.RegisterUserDto;

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
