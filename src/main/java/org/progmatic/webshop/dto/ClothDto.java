package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.Gender;
import org.progmatic.webshop.model.Image;
import org.progmatic.webshop.model.Type;

public class ClothDto {

    private long id;
    private String name;
    private String details;
    private float price;
    private String color;



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


}
