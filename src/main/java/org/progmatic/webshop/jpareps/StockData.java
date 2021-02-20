package org.progmatic.webshop.jpareps;

import org.progmatic.webshop.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockData extends JpaRepository<Stock, String> {
}
