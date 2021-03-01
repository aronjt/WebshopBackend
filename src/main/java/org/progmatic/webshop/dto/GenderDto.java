package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.Gender;

/**
 * DTO for {@link Gender} entity.<br>
 *     Contains:
 *     <ul>
 *         <li>String gender</li>
 *         <li>long imageId</li>
 *     </ul>
 */
public class GenderDto {

    private String gender;
    private long imageId;

    public GenderDto() {}

    /**
     * Works as dozer.
     * @param gender is the gender that will be transformed to {@link GenderDto}
     */
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
