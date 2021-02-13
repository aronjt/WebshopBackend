package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.Clothes;
import org.progmatic.webshop.model.PurchasedClothes;

public class PurchasedClothDto {

    private Clothes clothes;
    private int quantity;
    private String size;

    public PurchasedClothDto() {}

    public PurchasedClothDto(PurchasedClothes cloth) {
        clothes = cloth.getClothes();
        quantity = cloth.getQuantity();
        size = cloth.getSize();
    }

    public Clothes getClothes() {
        return clothes;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

}