package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;

/**
 * Interface for authorisation
 */
public interface AuthService {
    boolean login(String userName, String password);
    boolean register(RegisterReq registerReq, Role role);
}
