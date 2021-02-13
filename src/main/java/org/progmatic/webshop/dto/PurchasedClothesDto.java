package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.PurchasedClothes;

public class PurchasedClothesDto {

    private long id;
    private String name;
    private String details;
    private float price;
    private String color;
    private int quantity;
    private String size;

    public PurchasedClothesDto(PurchasedClothes clothes) {
        id = clothes.getId();
        name = clothes.getClothes().getName();
        details = clothes.getClothes().getDetails();
        price = clothes.getClothes().getPrice();
        color = clothes.getClothes().getColor();
        quantity = clothes.getQuantity();
        size = clothes.getSize();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
