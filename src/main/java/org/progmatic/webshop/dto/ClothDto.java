package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.Clothes;
import org.progmatic.webshop.model.Gender;
import org.progmatic.webshop.model.Image;
import org.progmatic.webshop.model.Type;

public class ClothDto {

    private long id;
    private String name;
    private String details;
    private float price;
    private String color;
    private long imageId;



    public ClothDto(Clothes clothes) {
        id = clothes.getId();
        name = clothes.getName();
        details = clothes.getDetails();
        price = clothes.getPrice();
        color = clothes.getColor();
        imageId = clothes.getImage().getId();
    }

    public ClothDto() {
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

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
}
