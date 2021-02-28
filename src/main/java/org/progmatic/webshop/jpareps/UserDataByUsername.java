package org.progmatic.webshop.jpareps;

import org.progmatic.webshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataByUsername extends JpaRepository<User, String> {
    User findByUsernameIgnoreCase(String username);
}
