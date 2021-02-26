package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.OnlineOrder;
import org.progmatic.webshop.model.PurchasedClothes;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {

    private long id;
    private float totalPrice;
    private boolean isFinish;
    private long userId;
    private List<PurchasedClothDto> purchasedClothesList;
    private String comment;

    public OrderDto() {}

    public OrderDto(OnlineOrder order) {
        id = order.getId();
        totalPrice = order.getTotalPrice();
        isFinish = order.isFinish();
        userId = order.getUser().getId();
    }

    public void setPurchasedClothesList(List<PurchasedClothDto> list) {
        purchasedClothesList = list;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public long getUserId() {
        return userId;
    }

    public List<PurchasedClothDto> getPurchasedClothesList() {
        return purchasedClothesList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
