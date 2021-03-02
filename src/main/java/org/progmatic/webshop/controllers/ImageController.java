package org.progmatic.webshop.controllers;

import org.progmatic.webshop.jpareps.ImageData;
import org.progmatic.webshop.model.Image;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.Message;
import org.progmatic.webshop.services.ImageService;
import org.progmatic.webshop.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Controls actions related to images.<br>
 *     Containing endpoints:
 *     <ul>
 *         <li>/image, post</li>
 *         <li>/images/{id}, get</li>
 *     </ul>
 */
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

    /**
     * Endpoint for uploading an image, and saving it in the database.<br>
     *     See {@link ImageService#compressBytes(byte[])} for more information.
     * @param file is the image file that will be uploaded
     * @return a confirm {@link Message} about the uploading process
     * @throws IOException if something went wrong while uploading the image
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/image")
    public Feedback uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        Image img = new Image(file.getOriginalFilename(),
                imageService.compressBytes(file.getBytes()));
        imageData.save(img);
        return new Message(true, "successful image upload");
    }

    /**
     * Endpoint for getting one image with the given id.<br>
     *     See {@link ImageService#decompressBytes(byte[])} for more information.
     * @param imageId is the image's id in the database
     * @return the byte code of the wanted image in a representable image format
     */
    @GetMapping(path = {"/images/{id}"}, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImageById(@PathVariable("id") Long imageId) {
        final Optional<Image> retrievedImage = imageData.findById(imageId);
        if (retrievedImage.isPresent()) {
            Image img = new Image(retrievedImage.get().getName(),
                    imageService.decompressBytes(retrievedImage.get().getData()));
            return img.getData();
        }
        return new byte[]{};
    }
}

