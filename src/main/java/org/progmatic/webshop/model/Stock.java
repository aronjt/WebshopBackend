package org.progmatic.webshop.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Entity for stock.<br>
 *     Columns:
 *     <ul>
 *         <li>long id</li>
 *         <li>String size</li>
 *         <li>int quantity</li>
 *         <li>{@link Clothes} clothes</li>
 *     </ul>
 */
@Entity
public class Stock {

    @Id
    @GeneratedValue
    private long id;

    private String size;

    private int quantity;

    @ManyToOne
    private Clothes clothes;

    public Stock() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Clothes getClothes() {
        return clothes;
    }

    public void setClothes(Clothes clothes) {
        this.clothes = clothes;
    }
}
