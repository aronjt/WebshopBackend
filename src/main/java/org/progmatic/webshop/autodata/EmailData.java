package org.progmatic.webshop.autodata;

import org.progmatic.webshop.emails.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailData extends JpaRepository<Email, String> {
    public Email findByMessageType(String messageType);
}
