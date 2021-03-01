package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.OnlineOrder;
import org.progmatic.webshop.model.PurchasedClothes;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for {@link OnlineOrder} entity.<br>
 *     Contains:
 *     <ul>
 *         <li>long id</li>
 *         <li>float totalPrice</li>
 *         <li>boolean isFinish</li>
 *         <li>long userId</li>
 *         <li>List purchasedClothesList ({@link PurchasedClothDto})</li>
 *         <li>String comment</li>
 *     </ul>
 */
public class OrderDto {

    private long id;
    private float totalPrice;
    private boolean isFinish;
    private long userId;
    private List<PurchasedClothDto> purchasedClothesList;
    private String comment;

    public OrderDto() {}

    /**
     * Works as dozer.
     * @param order is the order that will be transformed to {@link OrderDto}
     */
    public OrderDto(OnlineOrder order) {
        id = order.getId();
        totalPrice = order.getTotalPrice();
        isFinish = order.isFinish();
        userId = order.getUser().getId();
        if (order.getPurchasedClothesList() != null) {
            addToPurchasedClothesList(order.getPurchasedClothesList());
        }
    }

    public void setPurchasedClothesList(List<PurchasedClothDto> list) {
        purchasedClothesList = list;
    }

    private void addToPurchasedClothesList(List<PurchasedClothes> list) {
        purchasedClothesList = new ArrayList<>();
        for (PurchasedClothes pc : list) {
            purchasedClothesList.add(new PurchasedClothDto(pc));
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
