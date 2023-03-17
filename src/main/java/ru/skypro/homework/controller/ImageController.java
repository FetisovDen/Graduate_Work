package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class ImageController {
    private final ImageService imageService;
    private final AdsService adsService;

    public ImageController(ImageService imageService, AdsService adsService) {
        this.imageService = imageService;
        this.adsService = adsService;
    }
    @PreAuthorize("hasRole('USER')")
    @PatchMapping(value = "{id}/image",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<byte[]> updateAdsImage( @PathVariable int id,
                                                  @RequestPart("image") MultipartFile image,
                                                  Authentication authentication) {
        byte[] img = imageService.updateImage(authentication.getName(), id, image);
        return ResponseEntity.ok()
                .contentLength(img.length)
                .contentType(MediaType.IMAGE_JPEG)
                .body(img);
    }
}
