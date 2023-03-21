package ru.skypro.homework.dto;

import lombok.Data;

/**
 * DTO for work with comments
 */
@Data
public class CommentDto {
    private Integer author;
    private String createdAt;
    private Integer pk;
    private String text;
}
