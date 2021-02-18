package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.*;
import org.progmatic.webshop.services.ClothesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@RestController
public class ClothesController {

    private ClothesService clothesService;
    private CorsConfigurationSource corsConfigurationSource;

    @Autowired
    public ClothesController(ClothesService clothesService, @Qualifier("corsConfigurationSource") CorsConfigurationSource corsConfigurationSource) {
        this.clothesService = clothesService;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @GetMapping("/clothes")
    public ListDto<ClothDto> getAllClothes() {
        List<ClothDto> clothDtoList = clothesService.getAllClothes();
        return new ListDto<>(clothDtoList);
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
    public ListDto<ClothDto> getOneCloth(@PathVariable("id") long id) {
        ListDto<ClothDto> clothDtoListDto = new ListDto<>();
        clothDtoListDto.getList().add(clothesService.getOneCloth(id));
        return clothDtoListDto;
    }

    @GetMapping(value = "/clothes", params = "gender")
    public ListDto<ClothDto> getClothesFromGender(@RequestParam("gender") String gender) {
        List<ClothDto> clothDtoList = clothesService.getClothesFromGender(gender);
        return new ListDto<>(clothDtoList);
    }

    @PutMapping("/clothes/{id}")
    public FeedbackDto editCloth(@PathVariable("id") long id) {
        return null;
    }

    @GetMapping("/stock/{id}")
    public ListDto<StockDto> getStockLevel(@PathVariable("id") long id) {
        ListDto<StockDto> stockDtoListDto = new ListDto<>();
        stockDtoListDto.getList().add(clothesService.getStockLevel(id));
        return stockDtoListDto;
    }


}
