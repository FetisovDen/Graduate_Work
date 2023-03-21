package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO for work with users
 */
@Data
public class UserDto {
    private Integer id;
    private String phone;
    private String lastName;
    private String firstName;
    private String email;
    private String city;
    private String regDate;
    private String image;
}
