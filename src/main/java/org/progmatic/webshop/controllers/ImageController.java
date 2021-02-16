package org.progmatic.webshop.controllers;

import org.progmatic.webshop.autodata.ImageData;
import org.progmatic.webshop.dto.FeedbackDto;
import org.progmatic.webshop.model.Image;
import org.progmatic.webshop.services.ImageService;
import org.progmatic.webshop.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
public class ImageController {
    private final ImageService imageService;

    ImageData imageData;
    MyUserDetailsService myUserDetailsService;

    @Autowired
    public ImageController(ImageService imageService, MyUserDetailsService userDetailsService, ImageData imageData) {
        this.imageService = imageService;
        this.myUserDetailsService = userDetailsService;
        this.imageData = imageData;
    }


    @PostMapping("/image")
    public FeedbackDto uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        Image img = new Image(file.getOriginalFilename(),
                imageService.compressBytes(file.getBytes()));
        imageData.save(img);
        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setMessage("Successful image upload");
        return feedbackDto;
    }

    @GetMapping(path = {"/images/{id}"}, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImageById(@PathVariable("id") Long imageId) throws IOException {
        final Optional<Image> retrievedImage = imageData.findById(imageId);
        Image img = new Image(retrievedImage.get().getName(),
                imageService.decompressBytes(retrievedImage.get().getData()));
        return img.getData();
    }
}

