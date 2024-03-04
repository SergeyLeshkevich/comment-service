package ru.clevertec.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.comment.entity.User;
import ru.clevertec.comment.entity.dto.UserRequest;
import ru.clevertec.comment.mapper.UserMapper;
import ru.clevertec.comment.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for user-related operations.
 * This class provides methods to create users and retrieve them by UUID.
 *
 * @author Sergey Leshkevich
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Creates a new user in the repository.
     *
     * @param userRequest the DTO containing the user's information.
     * @return the newly created User entity.
     */
    public User create(UserRequest userRequest) {

        User user = userMapper.toEntity(userRequest);

        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their UUID.
     *
     * @param uuid the UUID of the user to retrieve.
     * @return an Optional containing the found user or an empty Optional if not found.
     */
    public Optional<User> getByUuiD(UUID uuid) {
        return userRepository.findByUuid(uuid);
    }

}
