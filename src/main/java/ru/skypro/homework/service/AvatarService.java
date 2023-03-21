package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AvatarNotFoundException;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.repository.AvatarRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for work with avatar
 */
@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final String avatarDir;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * @param avatarDir
     * @param avatarRepository
     */
    public AvatarService(@Value("${path.to.images.folder}./avatar")String avatarDir, AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
        this.avatarDir = avatarDir;
    }

    /**
     * Method for create add avatar
     *
     * @param user
     * @param multipartFile
     * @return avatar
     */
    public Avatar addAvatar(User user, MultipartFile multipartFile) {
        try{
            byte[] img = multipartFile.getBytes();
            Path path = Path.of(avatarDir, user.getId() + "_" + LocalDateTime.now().format(format) + ".jpg");
            Files.createDirectories(path.getParent());
            Files.write(path, img);
            Avatar avatar = new Avatar();
            avatar.setPathAvatar(path.toString());
            avatarRepository.save(avatar);
            return avatar;
        }catch (IOException io) {
            throw new RuntimeException();}
    }

    /**
     * Method for return image
     *
     * @param idAvatar
     * @return Files.readAllBytes
     */
    public byte[] getImage(int idAvatar) {
        Avatar avatar = avatarRepository.findById(idAvatar).orElseThrow(AvatarNotFoundException::new);
        Path path = Path.of(avatar.getPathAvatar());
        try {
            return Files.readAllBytes(path);
        } catch (IOException | NoSuchFieldError e) {
            throw new ImageNotFoundException();
        }
    }

    /**
     * Method for update avatar
     *
     * @param user
     * @param file
     * @return addAvatar
     */
    public Avatar updateAvatar(User user, MultipartFile file) {
        Avatar ava = user.getAvatar();
        if (ava != null) {
            checkImage(ava);
            try {
                deleteImageFile(Path.of(ava.getPathAvatar()));
            }
            catch (NullPointerException n){
                throw new AvatarNotFoundException();
            }
        }
        return addAvatar(user, file);
    }

    /**
     * Method for check image
     *
     * @param avatar
     */
    private void checkImage(Avatar avatar) {
        if (avatar.getId() == null || avatarRepository.findById(avatar.getId()).isEmpty()) {
            throw new AvatarNotFoundException();
        }
    }

    /**
     * Method for delete image file
     *
     * @param path
     */
    public static void deleteImageFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(path + " file in not removed in file system");
        }
    }
}
