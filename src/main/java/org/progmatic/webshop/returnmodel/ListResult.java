package org.progmatic.webshop.returnmodel;

import java.util.ArrayList;
import java.util.List;

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
