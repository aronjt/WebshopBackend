package org.progmatic.webshop.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Gender {

    @Id
    private String gender;

    public Gender() {
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
