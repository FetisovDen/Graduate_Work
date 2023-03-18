package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final UserRepository userRepository;

    private final PasswordEncoder encoder;


    public AuthServiceImpl(UserDetailsService userDetailsService, UserService userService, UserRepository userRepository, PasswordEncoder encoder) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public boolean login(String username, String password) {
        if (!userService.userCheck(username)) {
            log.warn(username + "- UserName not found");
            return false;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String encryptedPassword = userDetails.getPassword();

        return encoder.matches(password, encryptedPassword);
    }

    @Override
    public boolean register(RegisterReq registerReq, Role role) {
        if (userRepository.existsUserByUserName(registerReq.getUsername())) {
            log.warn(registerReq + " - this login is busy");
            return false;
        }
        String encodedPassword = encoder.encode(registerReq.getPassword());
        registerReq.setPassword(encodedPassword);
        userService.addUser(registerReq);
        return true;
    }
}
