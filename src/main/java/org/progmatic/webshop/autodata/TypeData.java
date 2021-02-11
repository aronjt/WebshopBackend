package org.progmatic.webshop.autodata;

import org.progmatic.webshop.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeData extends JpaRepository<Type, String> {
}
