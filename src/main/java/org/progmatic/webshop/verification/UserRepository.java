package org.progmatic.webshop.verification;

import org.progmatic.webshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmailIgnoreCase(String email);
}
