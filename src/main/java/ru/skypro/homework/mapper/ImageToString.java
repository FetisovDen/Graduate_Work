package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.entity.Image;
@Component
public class ImageToString {
    String mapImageToString(Image image) {
            if (image.getPathImage() == null || image.getId() == null) {
                return null;
            } else {
                return "/ads/image/" + image.getId();
            }
        }
}
