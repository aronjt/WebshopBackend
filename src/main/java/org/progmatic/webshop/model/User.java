package org.progmatic.webshop.model;

import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.helpers.UserDataHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class User extends BaseEntity implements UserDetails {

    @Email
    @NotBlank
    @NotNull
    @Column(unique = true)
    private String username;


    @NotBlank
    @NotNull
    private String password;

    @NotBlank
    @NotNull
    private String firstName;

    @NotBlank
    @NotNull
    private String lastName;

    @NotBlank
    @NotNull
    private String country;

    @NotNull
    private int zipcode;

    @NotBlank
    @NotNull
    private String city;

    @NotBlank
    @NotNull
    private String address;

    @NotBlank
    @NotNull
    private String phoneNumber;
    private int points;
    private String UserRole;

    @OneToMany(mappedBy = "user")
    private Set<OnlineOrder> orders;

    private boolean isEnabled;

    public User() {
    }

    public User(RegisterUserDto registerUserDto) {
        firstName=registerUserDto.getFirstName();
        lastName= registerUserDto.getLastName();
        country=registerUserDto.getCountry();
        zipcode=registerUserDto.getZipcode();
        address=registerUserDto.getAddress();
        city=registerUserDto.getCity();
        username =registerUserDto.getEmail();
        password=registerUserDto.getPassword();
        phoneNumber=registerUserDto.getPhoneNumber();
        UserRole= UserDataHelper.ROLE_USER;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String userRole) {
        UserRole = userRole;
    }

    public Set<OnlineOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<OnlineOrder> orders) {
        this.orders = orders;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole));
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
