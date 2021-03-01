package org.progmatic.webshop.jpareps;

import org.progmatic.webshop.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailData extends JpaRepository<Email, String> {
    Email findByMessageType(String messageType);
}
