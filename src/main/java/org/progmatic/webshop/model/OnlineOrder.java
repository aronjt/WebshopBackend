package org.progmatic.webshop.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class OnlineOrder extends BaseEntity {

    //TODO if price is float than totalPrice should be float as well. Is it a hungarian webshop or not?
    //TODO same in OrderDTO
    private int totalPrice;
    private boolean isFinish;

    @ManyToOne
    private User user;

    @OneToMany
    private List<PurchasedClothes> purchasedClothesList;

    public OnlineOrder() {
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
