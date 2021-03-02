package org.progmatic.webshop.dto;

public class NewPasswordUserDto {
    private String password;
    private String username;

    public NewPasswordUserDto(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
