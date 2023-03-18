package ru.skypro.homework.mapper;
import org.springframework.stereotype.Component;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;

import java.time.LocalDateTime;

@Component
public class CommentDtoToEntity {
    private final UserRepository userRepository;

    public CommentDtoToEntity(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    LocalDateTime mapStringToTimestamp(String string) {
        return LocalDateTime.parse(string);
    }

    User map(Integer author) {
        return userRepository.findById(author).orElseThrow(() ->
                    new RuntimeException( author +" - user dont found"));
    }
}
