package org.progmatic.webshop.model;

import org.progmatic.webshop.dto.OrderDto;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class OnlineOrder extends BaseEntity {

    private float totalPrice;
    private boolean isFinish;

    @ManyToOne
    private User user;

    @OneToMany
    private List<PurchasedClothes> purchasedClothesList;

    public OnlineOrder() {
    }

    public OnlineOrder(OrderDto orderDto, User user, List<PurchasedClothes> purchasedClothesList) {
        totalPrice = orderDto.getTotalPrice();
        isFinish = orderDto.isFinish();
        this.user = user;
        this.purchasedClothesList = purchasedClothesList;
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
