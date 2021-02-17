package org.progmatic.webshop.dto;

import java.util.ArrayList;
import java.util.List;

public class ListDto<T> {

    private List<T> list = new ArrayList<>();

    public ListDto() {
    }
    public ListDto(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
