package org.progmatic.webshop.model;

import org.progmatic.webshop.dto.PurchasedClothDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Entity for purchased clothes.<br>
 *     Columns:
 *     <ul>
 *         <li>long id</li>
 *         <li>{@link Clothes} clothes</li>
 *         <li>{@link OnlineOrder} onlineOrder</li>
 *         <li>int quantity</li>
 *         <li>String size</li>
 *     </ul>
 */
@Entity
public class PurchasedClothes {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Clothes clothes;

    @ManyToOne
    private OnlineOrder onlineOrder;

    private int quantity;

    private String size;

    public PurchasedClothes() {}

    /**
     * Works as dozer.
     * @param cloth is a {@link PurchasedClothDto} that will be transformed to {@link PurchasedClothes}
     */
    public PurchasedClothes(PurchasedClothDto cloth) {
        quantity = cloth.getQuantity();
        size = cloth.getSize();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Clothes getClothes() {
        return clothes;
    }

    public void setClothes(Clothes clothes) {
        this.clothes = clothes;
    }

    public OnlineOrder getOnlineOrder() {
        return onlineOrder;
    }

    public void setOnlineOrder(OnlineOrder onlineOrder) {
        this.onlineOrder = onlineOrder;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


}