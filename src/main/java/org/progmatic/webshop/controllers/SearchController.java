package org.progmatic.webshop.controllers;

import org.progmatic.webshop.returnmodel.Feedback;
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
    public Feedback getGenders() {
        return service.getGenders();
    }

    @GetMapping("/types")
    public Feedback getTypes() {
        return service.getTypes();
    }

}
