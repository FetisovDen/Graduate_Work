package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO for work with register request
 */
@Data
public class RegisterReq {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}
