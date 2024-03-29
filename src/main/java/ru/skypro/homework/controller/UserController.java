package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;
import org.springframework.security.core.Authentication;


@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/set_password")
    public ResponseEntity<NewPasswordDto> setPassword(Authentication authentication,
                                                      @RequestBody NewPasswordDto newPasswordDto) {
        return ResponseEntity.ok(userService.setPassword(authentication.getName(), newPasswordDto));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping( "/me")
    public ResponseEntity<UserDto> getUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserDto(authentication.getName()));
    }

    @GetMapping(value = "/avatar/{ad_pk}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable int ad_pk) {
                byte[] img = userService.getUserAvatar(ad_pk);
        return ResponseEntity.ok()
                .contentLength(img.length)
                .contentType(MediaType.IMAGE_JPEG)
                .body(img);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(Authentication authentication, @RequestBody UserDto body) {
        return ResponseEntity.ok(userService.updateUser(authentication.getName(), body));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping(path = "/me/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserDto> updateUserImage(@RequestPart("image") MultipartFile image,
                                                   Authentication authentication) {
        userService.updateUserImage(authentication.getName(), image);
        return ResponseEntity.ok().build();
    }

}
