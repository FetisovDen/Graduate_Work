package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.PasswordMismatchException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;


import java.time.LocalDateTime;

/**
 * Class for work with users
 */
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AvatarService avatarService;
    private final UserMapper userMapper;

    /**
     * @param userRepository
     * @param avatarService
     * @param userMapper
     */
    public UserService(UserRepository userRepository, AvatarService avatarService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.avatarService = avatarService;
        this.userMapper = userMapper;
    }

    /**
     * Method for set password
     *
     * @param name
     * @param newPasswordDto
     * @return newPasswordDto
     */
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

    /**
     * Method for return user
     *
     * @param userName
     * @return user
     */
    private User getUser(String userName) {
        User user = userRepository.findByUserName(userName);
        checkUser(user);
        return user;
    }

    /**
     * Method for return user Dto
     *
     * @param userName
     * @return userMapper.toDTO
     */
    public UserDto getUserDto(String userName) {
        return userMapper.toDTO(getUser(userName));
    }

    /**
     * Method for return user avatar
     *
     * @param avatarId
     * @return avatarService.getImage
     */
    public byte[] getUserAvatar(Integer avatarId) {
        return avatarService.getImage(avatarId);
    }

    /**
     * Method for update user
     *
     * @param userName
     * @param body
     * @return userMapper.toDTO
     */
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

    /**
     * Method for update user image
     *
     * @param userName
     * @param file
     */
    public void updateUserImage(String userName, MultipartFile file) {
        User user = userRepository.findByUserName(userName);
        checkUser(user);
        user.setAvatar(avatarService.updateAvatar(user, file));
        userRepository.save(user);
    }

    /**
     * Method for check user
     *
     * @param user
     */
    private void checkUser(User user) {
        if (user == null) {
            throw new UserNotFoundException();
        }
    }

    /**
     * Method for user check
     *
     * @param username
     * @return userRepository.existsUserByUserName
     */
    public boolean userCheck(String username) {
        return userRepository.existsUserByUserName(username);
    }

    /**
     * Method for create add user
     *
     * @param registerReq
     */
    public void addUser(RegisterReq registerReq) {
        User user = userMapper.toEntity(registerReq);
        user.setRegDate(LocalDateTime.now());
        user.setAvatar(new Avatar(1, "zero"));
        user.setRole(Role.USER);
        userRepository.save(user);
    }
    /**
     * Method username upload by username
     *
     * @param username
     * @return userRepository.findByUserName
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username);
    }
}
