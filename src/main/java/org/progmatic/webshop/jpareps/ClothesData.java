package org.progmatic.webshop.jpareps;

import org.progmatic.webshop.model.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothesData extends JpaRepository<Clothes, String> {
    public Clothes findByName(String name);
}
