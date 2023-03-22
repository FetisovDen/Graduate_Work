package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO for work login request
 */
@Data
public class LoginReq {
    private String password;
    private String username;

}
