package org.progmatic.webshop.autodata;

import org.progmatic.webshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//@Repository("userRepository")
public interface UserDataByUsername extends JpaRepository<User, String> {
    User findByUsernameIgnoreCase(String username);
}
