package org.progmatic.webshop.dto;

public class UserDto {

        private long id;
        private String firstName;
        private String lastName;
        private String email;
        private String UserRole;

        public UserDto(long id, String firstName, String lastName, String email, String userRole) {
                this.id = id;
                this.firstName = firstName;
                this.lastName = lastName;
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
