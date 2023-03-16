package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {
    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }


    @GetMapping()
    public ResponseEntity<ResponseWrapperAdsDto> getAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
    public ResponseEntity<CommentDto> addComments(@PathVariable("ad_pk") int adPk, @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(adsService.addComments(adPk, commentDto));
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
    public ResponseEntity<AdsDto> updateAds(Authentication au, @PathVariable int id, @RequestBody CreateAdsDto createAdsDto) {
        return ResponseEntity.ok(adsService.updateAds(au.getName(), id, createAdsDto));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<CommentDto> getComments(@PathVariable("ad_pk") int adPk, @PathVariable int id) {
        return ResponseEntity.ok(adsService.getCommentOfAds(adPk, id));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<CommentDto> deleteComments(Authentication au, @PathVariable("ad_pk") int adPk,
                                                     @PathVariable int id) {
//        add BadRequest
        return ResponseEntity.ok(adsService.deleteCommentOfAds(au.getName(), adPk, id));
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<CommentDto> updateComments(Authentication au, @PathVariable("ad_pk") int adPk,
                                                     @PathVariable int id, @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(adsService.updateComments(au.getName(), adPk, id, commentDto));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Authentication authentication) {
        return ResponseEntity.ok(adsService.getAdsMe(authentication.getName()));
    }
}
