package ru.clevertec.comment.entity.dto;

import lombok.AllArgsConstructor;

import java.util.UUID;

public record UserRequest(

        UUID uuid,

        String userName) {
}
