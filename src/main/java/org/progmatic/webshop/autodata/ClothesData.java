package org.progmatic.webshop.autodata;

import org.progmatic.webshop.model.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothesData extends JpaRepository<Clothes, String> {
}
