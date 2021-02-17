package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.*;
import org.progmatic.webshop.services.ClothesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
public class ClothesController {

    ClothesService clothesService;

    @Autowired
    public ClothesController(ClothesService clothesService) {
        this.clothesService = clothesService;
    }

    @GetMapping("/clothes")
    public List<ClothDto> getAllClothes() {
        return clothesService.getAllClothes();
    }

    @PostMapping("/clothes")
    public FeedbackDto addNewCloth(@RequestBody ClothDto cloth) {
        try {
            long id = clothesService.addNewCloth(cloth);
            return new FeedbackDto(id, "Added successfully");
        } catch (Exception e) {
            return new FeedbackDto("Fail to add");
        }
    }

    @GetMapping("/clothes/{id}")
    public ClothDto getOneCloth(@PathVariable("id") long id) {
        return clothesService.getOneCloth(id);
    }

    @GetMapping(value = "/clothes", params = "gender")
    public List<ClothDto> getClothesFromGender(@RequestParam("gender") String gender) {
        return clothesService.getClothesFromGender(gender);
    }

    @GetMapping(value = "/clothes", params = "")

    @PutMapping("/clothes/{id}")
    public FeedbackDto editCloth(@PathVariable("id") long id) {
        return null;
    }

    @GetMapping("/stock/{id}")
    public StockDto getStockLevel(@PathVariable("id") long id) {
        return clothesService.getStockLevel(id);
    }

    @GetMapping("/purchased/{id}")
    public PurchasedClothesDto getPurchasedClothes(@PathVariable("id") long id) {
        return clothesService.getPurchasedClothes(id);
    }
}
