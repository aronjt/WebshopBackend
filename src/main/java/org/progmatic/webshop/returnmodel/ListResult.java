package org.progmatic.webshop.returnmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic class used for HTTP responses. Extends {@link Feedback}.<br>
 *     Contains a list of generic type. It automatically sets the Feedback's success field to true.
 * @param <T>
 */
public class ListResult<T> extends Feedback {

    private List<T> list = new ArrayList<>();

    public ListResult() {
        setSuccess(true);
    }

    public ListResult(List<T> list) {
        setSuccess(true);
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
