package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;

@Mapper(componentModel = "spring", uses = {AvatarToString.class})
public interface UserMapper {
    @Mapping(target = "regDate", source = "regDate", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "image", source = "avatar")
    UserDto toDTO(User user);
    @Mapping(target = "regDate", source = "regDate", dateFormat = "yyyy-MM-dd HH:mm")
    User toEntity(UserDto dto);

    @Mapping(target = "userName", source = "username")
    User toEntity(RegisterReq registerReq);
}
