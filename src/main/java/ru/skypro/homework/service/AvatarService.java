package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AvatarNotFoundException;
import ru.skypro.homework.repository.AvatarRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final String avatarDir;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AvatarService(@Value("${path.to.images.folder}./avatar")String avatarDir, AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
        this.avatarDir = avatarDir;
    }

    public Avatar addAvatar(User user, MultipartFile multipartFile) {
        try{
            byte[] img = multipartFile.getBytes();
            Path path = Path.of(avatarDir, user.getId() + "_" + LocalDateTime.now().format(format) + ".jpg");
            Files.createDirectories(path.getParent());
            Files.write(path, img);
            user.getAvatar().setPathAvatar(path.toString());
            avatarRepository.save(user.getAvatar());
            return user.getAvatar();
        }catch (IOException io) {
            throw new RuntimeException();}
    }

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


    private void checkImage(Avatar avatar) {
        if (avatar.getId() == null || avatarRepository.findById(avatar.getId()).isEmpty()) {
            throw new AvatarNotFoundException();
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
