package ru.clevertec.comment.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.comment.entity.User;
import ru.clevertec.comment.entity.dto.UserRequest;


/**
 * Mapper interface for converting between User entities and corresponding DTOs.
 *
 * @author Sergey Leshkevich
 * @version 1.0
 */
@Mapper(config = MappersConfig.class)
public interface UserMapper {

    /**
     * Converts UserRequest DTO to User entity.
     *
     * @param userRequest UserRequest DTO to be converted.
     * @return Corresponding User entity.
     */
    User toEntity(UserRequest userRequest);
}
