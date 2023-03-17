package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.entity.Comment;


@Mapper(componentModel = "spring", uses = {CommentDtoToEntity.class})
public interface CommentMapper {
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", expression = "java(comment.getUser().getId())")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd HH:mm")
    CommentDto commentToDTO(Comment comment);

    @Mapping(target = "id", source = "pk")
    @Mapping(target = "user", source = "author")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd HH:mm")
    Comment commentDtoToEntity(CommentDto commentDto);
}


