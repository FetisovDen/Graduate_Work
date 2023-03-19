package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.entity.Avatar;

@Component
public class AvatarToString {
    String mapAvatarToString(Avatar avatar) {
        if (avatar.getPathAvatar() == null || avatar.getId() == null) {
            return null;
        } else {
            return "/users/avatar/" + avatar.getId();
        }
//    }
//    Avatar mapStringToAvatar(String image) {
//            return null;

}
}
