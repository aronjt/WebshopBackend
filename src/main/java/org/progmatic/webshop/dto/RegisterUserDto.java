package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.User;

/**
 * DTO for {@link User} entity.<br>
 *     Contains:
 *     <ul>
 *         <li>String firstName</li>
 *         <li>String lastName</li>
 *         <li>String password</li>
 *         <li>String email</li>
 *         <li>String country</li>
 *         <li>int zipcode</li>
 *         <li>String city</li>
 *         <li>String address</li>
 *         <li>String phoneNumber</li>
 *         <li>long id</li>
 *     </ul>
 */
public class RegisterUserDto {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String country;
    private int zipcode;
    private String city;
    private String address;
    private String phoneNumber;
    private long id;

    public RegisterUserDto() {
    }

    /**
     * Works as dozer.<br>
     *     Important, that password will be NOT presented because of security maintenance.
     * @param user is the {@link User} that will be transformed to {@link RegisterUserDto}
     */
    public RegisterUserDto(User user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getUsername();
        country = user.getCountry();
        zipcode = user.getZipcode();
        city = user.getCity();
        address = user.getAddress();
        phoneNumber = user.getPhoneNumber();
        password = "";
        id = user.getId();
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public int getZipcode() {
        return zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
