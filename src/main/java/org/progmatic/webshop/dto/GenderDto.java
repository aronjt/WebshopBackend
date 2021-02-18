package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.Gender;

public class GenderDto {

    private String gender;
    private long imageId;

    public GenderDto() {}

    public GenderDto(Gender gender) {
        this.gender = gender.getGender();
        imageId = gender.getImage().getId();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
}
