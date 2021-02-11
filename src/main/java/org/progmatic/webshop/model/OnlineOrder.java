package org.progmatic.webshop.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class OnlineOrder extends BaseEntity{

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
}
