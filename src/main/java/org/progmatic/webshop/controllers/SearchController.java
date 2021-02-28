package org.progmatic.webshop.controllers;

import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controls actions related to clothes' genders and types.<br>
 *     Containing endpoints:
 *     <ul>
 *         <li>/genders, get</li>
 *         <li>/types, get</li>
 *     </ul>
 */
@RestController
public class SearchController {

    SearchService service;

    @Autowired
    public SearchController(SearchService s) {
        service = s;
    }

    /**
     * Endpoint for getting all genders available in the database.<br>
     *     See {@link SearchService#getGenders()} for more information.
     * @return all genders from the database
     */
    @GetMapping("/genders")
    public Feedback getGenders() {
        return service.getGenders();
    }

    /**
     * Endpoint for getting all types available in the database.<br>
     *     See {@link SearchService#getTypes()} for more information.
     * @return all types from the database
     */
    @GetMapping("/types")
    public Feedback getTypes() {
        return service.getTypes();
    }

}
