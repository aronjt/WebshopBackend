package org.progmatic.webshop.dto;

import java.util.List;

public class ListDto<T> {

    private List<T> list;

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
