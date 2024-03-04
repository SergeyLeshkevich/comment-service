package ru.clevertec.comment.entity.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime time,
        String text,
        UserResponse user,
        Long newsId) {
}
