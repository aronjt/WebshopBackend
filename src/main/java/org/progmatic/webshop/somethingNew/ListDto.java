package org.progmatic.webshop.somethingNew;

import java.util.ArrayList;
import java.util.List;

public class ListDto<T> extends Feedback {

    private List<T> list = new ArrayList<>();

    public ListDto() {}

    public ListDto(boolean success, List<T> list) {
        setSuccess(success);
        this.list = list;
    }


    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
