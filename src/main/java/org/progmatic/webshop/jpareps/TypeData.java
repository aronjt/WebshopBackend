package org.progmatic.webshop.jpareps;

import org.progmatic.webshop.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeData extends JpaRepository<Type, String> {
    Type findByType(String type);
}
