package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO for display full ads info
 */
@Data
public class FullAdsDto {
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private Integer pk;
    private Integer price;
    private String title;
}
