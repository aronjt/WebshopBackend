package org.progmatic.webshop.controllers;

import org.progmatic.webshop.autodata.ImageData;
import org.progmatic.webshop.model.Image;
import org.progmatic.webshop.services.ImageService;
import org.progmatic.webshop.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
public class ImageController {
    private final ImageService imageService;
    @Autowired
    ImageData imageData;
    MyUserDetailsService myUserDetailsService;

    @Autowired
    public ImageController(ImageService imageService, MyUserDetailsService userDetailsService) {
        this.imageService = imageService;
        this.myUserDetailsService = userDetailsService;
    }


    @PostMapping("/image")
    public ResponseEntity.BodyBuilder uploadImage(@RequestBody MultipartFile file) throws IOException {

        System.out.println("Original Image Byte Size - " + file.getBytes().length);
        Image img = new Image(file.getOriginalFilename(),
                imageService.compressBytes(file.getBytes()));
        imageData.save(img);
        return ResponseEntity.status(HttpStatus.OK);
    }

//    @GetMapping(path = {"/get/{name}"})
//    public Image getImage(@PathVariable("name") String imageName) throws IOException {
//
//        final Optional<Image> retrievedImage = imageData.findByName(imageName);
//        Image img = new Image(retrievedImage.get().getName(),
//                imageService.decompressBytes(retrievedImage.get().getData()));
//        return img;
//    }
    @GetMapping(path = {"/get/{id}"})
    public Image getImagebyId(@PathVariable("id") Long imageId) throws IOException {

        final Optional<Image> retrievedImage = imageData.findById(imageId);
        Image img = new Image(retrievedImage.get().getName(),
                imageService.decompressBytes(retrievedImage.get().getData()));
        return img;
    }
}

