package org.progmatic.webshop.autodata;

import org.progmatic.webshop.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderData extends JpaRepository<Gender, String> {
    public Gender findByGender(String gender);
}
