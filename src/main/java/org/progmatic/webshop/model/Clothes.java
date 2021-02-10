package org.progmatic.webshop.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Clothes extends BaseEntity{

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String details;

    @NotNull
    @NotBlank
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

    @ManyToMany
    private List<OnlineOrder> orderList;

    public Clothes() {
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

    public List<OnlineOrder> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OnlineOrder> orderList) {
        this.orderList = orderList;
    }
}