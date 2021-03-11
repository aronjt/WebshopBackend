package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.*;
import org.progmatic.webshop.model.Clothes;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.Message;
import org.progmatic.webshop.returnmodel.ListResult;
import org.progmatic.webshop.services.ClothesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controls actions related to clothes.<br>
 *     Containing endpoints:
 *     <ul>
 *         <li>/clothes, get</li>
 *         <li>/clothes, post</li>
 *         <li>/clothes?gender=, get</li>
 *         <li>/clothes/{id}, get</li>
 *         <li>/clothes/{id}, put</li>
 *         <li>/clothes/filter, post</li>
 *         <li>/stock/{id}, get</li>
 *     </ul>
 */
@RestController
public class ClothesController {

    private final ClothesService clothesService;

    @Autowired
    public ClothesController(ClothesService clothesService) {
        this.clothesService = clothesService;
    }

    /**
     * Endpoint for getting all clothes represented in the database.<br>
     *      See {@link ClothesService#getAllClothes()} for more information.
     * @return a {@link ListResult} that contains the clothes
     */
    @GetMapping("/clothes")
    public Feedback getAllClothes() {
        return new ListResult<>(clothesService.getAllClothes());
    }

    /**
     * Endpoint for creating a new cloth, saving it in the database.<br>
     *      See {@link ClothesService#addNewCloth(ClothDto)} for more information.
     * @param cloth is the cloth to be saved
     * @return a confirm {@link Message} about the adding process
     */
    @PostMapping("/clothes")
    public Feedback addNewCloth(@RequestBody ClothDto cloth) {
        Clothes c = clothesService.addNewCloth(cloth);
        clothesService.addClothToStock(c);
        return new Message(true, "Cloth successfully added");
    }

    /**
     * Endpoint for getting a cloth with the given id.<br>
     *      See {@link ClothesService#getOneCloth(long)} for more information.
     * @param id is the cloth's id
     * @return a {@link ListResult} that contains the wanted cloth
     */
    @GetMapping("/clothes/{id}")
    public Feedback getOneCloth(@PathVariable("id") long id) {
        ListResult<ClothDto> cloth = new ListResult<>();
        cloth.getList().add(clothesService.getOneCloth(id));
        return cloth;
    }

    /**
     * Endpoint for getting clothes with the given gender.<br>
     *      See {@link ClothesService#getClothesFromGender(String)} for more information.
     * @param gender is the gender to search for (genders can be found in {@link org.progmatic.webshop.helpers.ClothDataHelper})
     * @return a {@link ListResult} that contains the clothes
     */
    @GetMapping(value = "/clothes", params = "gender")
    public Feedback getClothesFromGender(@RequestParam("gender") String gender) {
        return new ListResult<>(clothesService.getClothesFromGender(gender));
    }

    /**
     * Endpoint for filtering the clothes with parameters.<br>
     *      See {@link ClothesService#filterClothes(FilterClothesDto)} for more information.
     * @param filter is a {@link FilterClothesDto}
     * @return a {@link ListResult} that contains the filtered clothes
     */
    @PostMapping(value = "/clothes/filter")
    public Feedback filterClothes(@RequestBody FilterClothesDto filter) {

        return new ListResult<>(clothesService.filterClothes(filter));
    }

    /**
     * Endpoint for editing an existing cloth.
     * @param id is the cloth's id that should be edited
     * @return a confirm {@link Message} about the editing process
     */
    @PutMapping("/clothes/{id}")
    public Feedback editCloth(@PathVariable("id") long id) {
        return new Message("Soz.");
    }

    /**
     * Endpoint for getting available clothes. (Available clothes are represented in the database under Stock table.)<br>
     *     See {@link ClothesService#getStockLevel(long)} for more information.
     * @param id is the cloth's id that will be searched
     * @return a {@link ListResult} that contains the available cloth data
     */
    @GetMapping("/stock/{id}")
    public Feedback getStockLevel(@PathVariable("id") long id) {
        ListResult<StockDto> clothDtoListResult = new ListResult<>();
        clothDtoListResult.getList().add(clothesService.getStockLevel(id));
        return clothDtoListResult;
    }


}
