package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO for create ads requests
 */
@Data
public class CreateAdsDto {
    private String description;
    private Integer price;
    private String title;
}
