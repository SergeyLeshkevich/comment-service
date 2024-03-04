package ru.clevertec.comment.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank
        @Size(min = 1, max = 500)
        String text,

        @NotNull
        UserRequest user,

        @NotNull
        Long newsId) {
}
