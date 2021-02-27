package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.Clothes;
import org.progmatic.webshop.model.PurchasedClothes;

public class PurchasedClothDto {

    private long id;
    private String name;
    private int quantity;
    private String size;
    private long imageId;
    private float subTotal;

    public PurchasedClothDto() {}

    public PurchasedClothDto(PurchasedClothes cloth) {
        id = cloth.getClothes().getId();
        quantity = cloth.getQuantity();
        size = cloth.getSize();
        name = cloth.getClothes().getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
