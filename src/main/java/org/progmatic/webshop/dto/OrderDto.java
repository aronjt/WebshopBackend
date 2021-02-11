package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.PurchasedClothes;
import org.progmatic.webshop.model.User;

import java.util.List;

public class OrderDto {

    private int id;
    //TODO if price is float than totalPrice should be float as well. Is it a hungarian webshop or not?
    private int totalPrice;
    private boolean isFinish;
    private User user;
    private List<PurchasedClothes> purchasedClothesList;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PurchasedClothes> getPurchasedClothesList() {
        return purchasedClothesList;
    }

    public void setPurchasedClothesList(List<PurchasedClothes> purchasedClothesList) {
        this.purchasedClothesList = purchasedClothesList;
    }
}
