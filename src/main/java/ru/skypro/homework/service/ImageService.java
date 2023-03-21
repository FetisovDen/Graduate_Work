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

/**
 * Class for work with image
 */
@Service
public class ImageService {
    private final String imageDir;
    private final ImageRepository imageRepository;
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * @param imageDir
     * @param imageRepository
     * @param adsRepository
     * @param userRepository
     */
    public ImageService(@Value("${path.to.images.folder}./ads") String imageDir, ImageRepository imageRepository,
                        AdsRepository adsRepository, UserRepository userRepository) {
        this.imageDir = imageDir;
        this.imageRepository = imageRepository;
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
    }

    /**
     * Method for create add image
     *
     * @param ads
     * @param multipartFile
     * @return image
     */
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

    /**
     * Method for return image
     *
     * @param idAds
     * @return Files.readAllBytes
     */
    public byte[] getImage(int idAds) {
        Ads ads = adsRepository.findById(idAds).orElseThrow(AdsNotFoundException::new);
        Path path = Path.of(ads.getImage().getPathImage());
        try {
            return Files.readAllBytes(path);
        } catch (IOException | NoSuchFieldError e) {
            throw new ImageNotFoundException();
        }

    }

    /**
     * Method for update image
     *
     * @param username
     * @param idAds
     * @param image
     * @return Files.readAllBytes
     */
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
        Path path = Path.of(imageDir, ads.getUser().getId()+"_"+ LocalDateTime.now().format(format) + Objects.requireNonNull(image.getOriginalFilename()).lastIndexOf('.'));
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

    /**
     * Method for delete image file
     *
     * @param path
     */
    public void deleteImageFile(Path path) {
        AvatarService.deleteImageFile(path);
    }
}
