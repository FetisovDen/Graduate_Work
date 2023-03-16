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
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;
    private final AdsService adsService;

    public ImageController(ImageService imageService, AdsService adsService) {
        this.imageService = imageService;
        this.adsService = adsService;
    }

    @GetMapping(value = "/images/{idAds}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getImage(@PathVariable int idAds) {
        byte[] img = imageService.getImage(idAds);
        return ResponseEntity.ok()
                .contentLength(img.length)
                .contentType(MediaType.IMAGE_JPEG)
                .body(img);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping(value = "/{idAds}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateAdsImage(Authentication au, @PathVariable int idAds, @RequestPart("image") MultipartFile image) {
        byte[] img = imageService.updateImage(au.getName(), idAds, image);
        return ResponseEntity.ok()
                .contentLength(img.length)
                .contentType(MediaType.IMAGE_JPEG)
                .body(img);
    }
}
