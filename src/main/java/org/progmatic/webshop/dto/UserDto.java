package org.progmatic.webshop.dto;

import org.progmatic.webshop.model.User;

/**
 * DTO for {@link User} entity.<br>
 *     Contains:
 *     <ul>
 *         <li>long id</li>
 *         <li>String firstName</li>
 *         <li>String lastName</li>
 *         <li>String email</li>
 *         <li>String userRole</li>
 *     </ul>
 */
public class UserDto {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String userRole;

    /**
     * Works as dozer.
     * @param user is the {@link User} that will be transformed to {@link UserDto}
     */
    public UserDto(User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getUsername();
        userRole = user.getUserRole();
    }

    public UserDto() {
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserRole() {
        return userRole;
    }

}
