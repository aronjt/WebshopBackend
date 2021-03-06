package org.progmatic.webshop.model;

import org.progmatic.webshop.dto.ClothDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Entity for Clothes. Table name: Clothes. Extends {@link BaseEntity}.<br>
 *     Columns:
 *     <ul>
 *         <li>String name</li>
 *         <li>String details</li>
 *         <li>float price</li>
 *         <li>String color</li>
 *         <li>{@link Type} type</li>
 *         <li>{@link Gender} gender</li>
 *         <li>{@link Image} image</li>
 *     </ul>
 */
@Entity
public class Clothes extends BaseEntity{

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String details;

    @NotNull
    private float price;

    @NotNull
    @NotBlank
    private String color;

    @ManyToOne
    private Type type;

    @ManyToOne
    private Gender gender;

    @OneToOne
    private Image image;

    public Clothes() {
    }

    /**
     * Works as dozer.
     * @param c is a {@link ClothDto} to be transformed to {@link Clothes}
     */
    public Clothes(ClothDto c) {
        name = c.getName();
        details = c.getDetails();
        price = c.getPrice();
        color = c.getColor();
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
