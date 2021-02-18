package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.ListDto;
import org.progmatic.webshop.model.Gender;
import org.progmatic.webshop.model.Type;
import org.progmatic.webshop.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    SearchService service;

    @Autowired
    public SearchController(SearchService s) {
        service = s;
    }

    @GetMapping("/genders")
    public ListDto<Gender> getGenders() {
        return service.getGenders();
    }

    @GetMapping("/types")
    public ListDto<Type> getTypes() {
        return service.getTypes();
    }

}
