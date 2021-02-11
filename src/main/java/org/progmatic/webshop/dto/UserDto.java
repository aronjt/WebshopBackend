package org.progmatic.webshop.dto;

public class UserDto {

        private long id;
        private String username;
        private String email;
        private String UserRole;

        public UserDto(long id, String username, String email, String userRole) {
                this.id = id;
                this.username = username;
                this.email = email;
                UserRole = userRole;
        }

        public UserDto() {
        }

        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getUserRole() {
                return UserRole;
        }

        public void setUserRole(String userRole) {
                UserRole = userRole;
        }
}
