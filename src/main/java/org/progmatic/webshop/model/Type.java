package org.progmatic.webshop.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity for types.<br>
 *     Columns:
 *     <ul>
 *         <li>String type</li>
 *     </ul>
 */
@Entity
public class Type {

    @Id
    private String type;

    public Type() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
