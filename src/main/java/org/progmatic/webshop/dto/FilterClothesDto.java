package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.Clothes;

/**
 * DTO for filtering clothes' results. It is used when a user wants to search.<br>
 *     Contains:
 *     <ul>
 *         <li>String name</li>
 *         <li>String gender</li>
 *         <li>String type</li>
 *         <li>String color</li>
 *         <li>int priceMin</li>
 *         <li>int priceMax</li>
 *     </ul>
 */
public class FilterClothesDto {

    private String name;
    private String gender;
    private String type;
    private String color;
    private int priceMin;
    private int priceMax;

    public FilterClothesDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public int getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }
}
