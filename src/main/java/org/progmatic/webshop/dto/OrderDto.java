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

    public OrderDto() {}

    public OrderDto(OnlineOrder order) {
        id = order.getId();
        totalPrice = order.getTotalPrice();
        isFinish = order.isFinish();
        userId = order.getUser().getId();

        setPurchasedClothesList(order.getPurchasedClothesList());
    }

    public void setPurchasedClothesList(List<PurchasedClothes> purchasedClothesList) {
        this.purchasedClothesList = new ArrayList<>();
        for (PurchasedClothes c : purchasedClothesList) {
            this.purchasedClothesList.add(new PurchasedClothDto(c));
        }
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



}
