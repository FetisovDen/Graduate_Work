package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

/**
 * DTO for display list ads
 */
@Data
public class ResponseWrapperAdsDto {
    private Integer count;
    private List<AdsDto> results;

    public ResponseWrapperAdsDto(Integer count, List<AdsDto> results) {
        this.count = count;
        this.results = results;
    }
}
