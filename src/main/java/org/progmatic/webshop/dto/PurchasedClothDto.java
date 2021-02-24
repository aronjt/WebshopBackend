package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.PurchasedClothes;

public class PurchasedClothDto {

    private long clothesId;
    private int quantity;
    private String size;

    public PurchasedClothDto() {}

    public PurchasedClothDto(PurchasedClothes cloth) {
        clothesId = cloth.getClothes().getId();
        quantity = cloth.getQuantity();
        size = cloth.getSize();
    }

    public long getClothesId() {
        return clothesId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

}
