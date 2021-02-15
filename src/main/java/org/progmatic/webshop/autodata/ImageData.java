package org.progmatic.webshop.autodata;

import org.progmatic.webshop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageData extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
//    Optional<Image> findById(String name);

}


