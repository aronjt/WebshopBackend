package org.progmatic.webshop.autodata;

import org.progmatic.webshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserData extends JpaRepository<User, String> {
    public User findByUsername(String username);
}
