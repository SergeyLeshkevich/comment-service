package ru.clevertec.comment.service;

import ru.clevertec.comment.entity.User;
import ru.clevertec.comment.entity.dto.UserRequest;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User create(UserRequest userRequest);

    Optional<User> getByUuiD(UUID uuid);
}
