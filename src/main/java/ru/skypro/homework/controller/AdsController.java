package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {
    private final AdsService adsService;
    private final ImageService imageService;

    public AdsController(AdsService adsService, ImageService imageService) {
        this.adsService = adsService;
        this.imageService = imageService;
    }


    @GetMapping()
    public ResponseEntity<ResponseWrapperAdsDto> getAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDto> addAds(Authentication authentication, @RequestPart CreateAdsDto properties, @RequestPart MultipartFile image) {
        return ResponseEntity.ok(adsService.addAds(authentication.getName(), properties, image));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{ad_pk}/comments")
    public ResponseEntity<ResponseWrapperCommentDto> getComments(@PathVariable("ad_pk") int adPk) {
        return ResponseEntity.ok(adsService.getAllCommentsByAdsId(adPk));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{ad_pk}/comments")
    public ResponseEntity<CommentDto> addComments(Authentication authentication,@PathVariable("ad_pk") int adPk, @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(adsService.addComments(authentication.getName(),adPk, commentDto));
    }

   @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<FullAdsDto> getFullAds(@PathVariable int id) {
        return ResponseEntity.ok(adsService.getFullAds(id));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<FullAdsDto> deleteAds(Authentication au, @PathVariable int id) {
        return ResponseEntity.ok(adsService.deleteAds(au.getName(), id));
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<AdsDto> updateAds(Authentication authentication, @PathVariable int id, @RequestBody CreateAdsDto createAdsDto) {
        return ResponseEntity.ok(adsService.updateAds(authentication.getName(), id, createAdsDto));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<CommentDto> getComments(Authentication authentication,@PathVariable("ad_pk") int adPk, @PathVariable int id) {
        return ResponseEntity.ok(adsService.getCommentOfAds(adPk, id));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<CommentDto> deleteComments(Authentication authentication, @PathVariable("ad_pk") int adPk,
                                                     @PathVariable int id) {
//        add BadRequest
        adsService.deleteCommentOfAds(authentication.getName(), adPk, id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<CommentDto> updateComments(Authentication authentication, @PathVariable("ad_pk") int adPk,
                                                     @PathVariable int id, @RequestBody CommentDto commentDto) {
        System.out.println("sdssssssssssssssssss");
        return ResponseEntity.ok(adsService.updateComments(authentication.getName(), adPk, id, commentDto));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Authentication authentication) {
        return ResponseEntity.ok(adsService.getAdsMe(authentication.getName()));
    }
    @GetMapping(value = "/image/{ad_pk}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable int ad_pk) {
        byte[] img = imageService.getImage(ad_pk);
        return ResponseEntity.ok()
                .contentLength(img.length)
                .contentType(MediaType.IMAGE_JPEG)
                .body(img);
    }
}
