package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.*;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.ListResult;
import org.progmatic.webshop.returnmodel.Message;
import org.progmatic.webshop.services.ClothesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClothesController {

    private final ClothesService clothesService;

    @Autowired
    public ClothesController(ClothesService clothesService) {
        this.clothesService = clothesService;
    }

    @GetMapping("/clothes")
    public Feedback getAllClothes() {
        return new ListResult<>(clothesService.getAllClothes());
    }

    @PostMapping("/clothes")
    public Feedback addNewCloth(@RequestBody ClothDto cloth) {
        clothesService.addNewCloth(cloth);
        return new Message(true, "Cloth successfully added");
    }

    @GetMapping("/clothes/{id}")
    public Feedback getOneCloth(@PathVariable("id") long id) {
        ListResult<ClothDto> cloth = new ListResult<>();
        cloth.getList().add(clothesService.getOneCloth(id));
        return cloth;
    }

    @GetMapping(value = "/clothes", params = "gender")
    public Feedback getClothesFromGender(@RequestParam("gender") String gender) {
        return new ListResult<>(clothesService.getClothesFromGender(gender));
    }

    @PostMapping(value = "/clothes/filter")
    public Feedback filterClothes(@RequestBody FilterClothesDto filter) {

        return new ListResult<>(clothesService.filterClothes(filter));
    }

    @PutMapping("/clothes/{id}")
    public Feedback editCloth(@PathVariable("id") long id) {
        return null;
    }

    @GetMapping("/stock/{id}")
    public Feedback getStockLevel(@PathVariable("id") long id) {
        ListResult<StockDto> clothDtoListResult = new ListResult<>();
        clothDtoListResult.getList().add(clothesService.getStockLevel(id));
        return clothDtoListResult;
    }


}
