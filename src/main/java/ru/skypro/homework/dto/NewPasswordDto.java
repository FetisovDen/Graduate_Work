package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO for change password requests
 */
@Data
public class NewPasswordDto {
    private String currentPassword;
    private String newPassword;
}
