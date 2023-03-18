package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.exception.RequestDeniedException;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


@Service
public class ImageService {
    private final String imageDir;
    private final ImageRepository imageRepository;
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ImageService(@Value("${path.to.images.folder}./ads") String imageDir, ImageRepository imageRepository,
                        AdsRepository adsRepository, UserRepository userRepository) {
        this.imageDir = imageDir;
        this.imageRepository = imageRepository;
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
    }

    public Image addImage(Ads ads, MultipartFile multipartFile) {
        try {
            byte[] img = multipartFile.getBytes();
            Path path = Path.of(imageDir, ads.getUser().getId()+"_"+ LocalDateTime.now().format(format) + Objects.requireNonNull(multipartFile.getOriginalFilename()).lastIndexOf('.'));
            Files.createDirectories(path.getParent());
            Files.write(path, img);
            Image image = new Image();
            image.setPathImage(path.toString());
            imageRepository.save(image);
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] getImage(int idAds) {
        Ads ads = adsRepository.findById(idAds).orElseThrow(AdsNotFoundException::new);
        Path path = Path.of(ads.getImage().getPathImage());
        try {
            return Files.readAllBytes(path);
        } catch (IOException | NoSuchFieldError e) {
            throw new ImageNotFoundException();
        }

    }
    public byte[] updateImage(String username, int idAds, MultipartFile image) {
        User user = userRepository.findByUserName(username);
        Ads ads = adsRepository.findById(idAds).orElseThrow(AdsNotFoundException::new);
        if(!ads.getUser().equals(user)) {throw new RequestDeniedException();}
        try {
            deleteImageFile(Path.of(ads.getImage().getPathImage()));
        }
        catch (NullPointerException n){
            throw new ImageNotFoundException();
        }
        Image image1 = imageRepository.findById(ads.getImage().getId()).orElseThrow(AdsNotFoundException::new);
        try {
        byte[] img = image.getBytes();
        Path path = Path.of(imageDir, ads.getDescription()+ LocalDateTime.now().format(format) + ".jpg");
        Files.createDirectories(path.getParent());
        Files.write(path, img);
        image1.setPathImage(path.toString());
        } catch (IOException e) {
            throw new ImageNotFoundException();
        }
        imageRepository.save(image1);
        try {
            return Files.readAllBytes(Path.of(image1.getPathImage()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteImageFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(path + " file in not removed in file system");
        }
    }
}
