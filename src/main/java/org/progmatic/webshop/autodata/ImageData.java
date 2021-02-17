package org.progmatic.webshop.autodata;

import org.progmatic.webshop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageData extends JpaRepository<Image, Long> {
    Image findByName(String name);

}


