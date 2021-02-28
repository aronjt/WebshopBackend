package org.progmatic.webshop.jpareps;

import org.progmatic.webshop.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderData extends JpaRepository<Gender, String> {
    Gender findByGender(String gender);
}
