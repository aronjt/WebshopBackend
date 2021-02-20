package org.progmatic.webshop.jpareps;

import org.progmatic.webshop.model.PurchasedClothes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasedClothData extends JpaRepository<PurchasedClothes, String> {
}
