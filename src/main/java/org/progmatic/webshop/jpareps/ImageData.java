package org.progmatic.webshop.jpareps;

import org.progmatic.webshop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageData extends JpaRepository<Image, Long> {
    Image findByName(String name);

}


