package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.Clothes;

/**
 * DTO for {@link Clothes} entity.<br>
 *     Contains:
 *     <ul>
 *         <li>long id</li>
 *         <li>String name</li>
 *         <li>String details</li>
 *         <li>float price</li>
 *         <li>String color</li>
 *         <li>long imageId</li>
 *     </ul>
 */
public class ClothDto {

    private long id;
    private String name;
    private String details;
    private float price;
    private String color;
    private String gender;
    private String type;
    private long imageId;

    /**
     * Works as dozer.
     * @param clothes is the cloth that will be transformed to {@link ClothDto}
     */
    public ClothDto(Clothes clothes) {
        id = clothes.getId();
        name = clothes.getName();
        details = clothes.getDetails();
        price = clothes.getPrice();
        color = clothes.getColor();
        gender = clothes.getGender().getGender();
        type = clothes.getType().getType();
        if (clothes.getImage() != null) {
            imageId = clothes.getImage().getId();
        }
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
}
