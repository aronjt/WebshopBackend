package org.progmatic.webshop.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity for extra data (only for admins).<br>
 *     Columns:
 *     <ul>
 *         <li>int id</li>
 *         <li>String secret</li>
 *     </ul>
 */
@Entity
public class ExtraData {
    @Id
    private int id;
    private String secret;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
