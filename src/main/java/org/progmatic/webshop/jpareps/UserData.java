package org.progmatic.webshop.jpareps;

import org.progmatic.webshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserData extends JpaRepository<User, String> {
    User findByUsername(String username);
}
