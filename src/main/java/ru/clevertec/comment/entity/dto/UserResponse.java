package ru.clevertec.comment.entity.dto;

import java.io.Serializable;
import java.util.UUID;

public record UserResponse(

        UUID uuid,

        String userName) implements Serializable {
}
