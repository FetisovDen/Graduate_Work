package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseWrapperCommentDto {
    private Integer count;
    private List<CommentDto> results;

    public ResponseWrapperCommentDto(Integer count, List<CommentDto> results) {
        this.count = count;
        this.results = results;
    }
}
