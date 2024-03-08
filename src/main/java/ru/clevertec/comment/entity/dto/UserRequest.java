package ru.clevertec.comment.entity.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserRequest(

        @NotNull
        UUID uuid,

        @NotNull
        String userName) {
}
