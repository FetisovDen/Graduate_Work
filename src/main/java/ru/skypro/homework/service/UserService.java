package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.PasswordMismatchException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AvatarService avatarService;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, AvatarService avatarService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.avatarService = avatarService;
        this.userMapper = userMapper;
    }

    public NewPasswordDto setPassword(String name, NewPasswordDto newPasswordDto) throws PasswordMismatchException {
        User user = getUser(name);
        if (user.getPassword().equals(newPasswordDto.getCurrentPassword())) {
            user.setPassword(newPasswordDto.getNewPassword());
            userRepository.save(user);
        } else {
            throw new PasswordMismatchException();
        }
        return newPasswordDto;
    }

    private User getUser(String userName) {
        User user = userRepository.findByUserName(userName);
        checkUser(user);
        return user;
    }
    public UserDto getUserDto(String userName) {
        return userMapper.toDTO(getUser(userName));
    }
    public byte[] getUserAvatar(String userName) {
        User user = userRepository.findByUserName(userName);
        checkUser(user);
        Avatar avatar = user.getAvatar();
        Path path = Paths.get(avatar.getPathAvatar());
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public UserDto updateUser(String userName, UserDto body) {
        User oldUser = getUser(userName);
        User newUser = userMapper.toEntity(body);
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getPhone() != null) {
            oldUser.setPhone(newUser.getPhone());
        }
        if (newUser.getFirstName() != null) {
            oldUser.setFirstName(newUser.getFirstName());
        }
        if (newUser.getLastName() != null) {
            oldUser.setLastName(newUser.getLastName());
        }
        oldUser = userRepository.save(oldUser);
        return userMapper.toDTO(oldUser);
    }

    public void updateUserImage(String userName, MultipartFile file) {
        User user = userRepository.findByUserName(userName);
        checkUser(user);
        user.setAvatar(avatarService.updateAvatar(user, file));
        userRepository.save(user);
    }
    private void checkUser(User user ) {
        if (user == null) {
            throw new UserNotFoundException();
        }
    }
}
