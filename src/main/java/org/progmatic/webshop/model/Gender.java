package org.progmatic.webshop.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Entity for genders.<br>
 *     Columns:
 *     <ul>
 *         <li>String gender</li>
 *         <li>{@link Image} image</li>
 *     </ul>
 */
@Entity
public class Gender {

    @Id
    private String gender;

    @OneToOne
    private Image image;

    public Gender() {}

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
