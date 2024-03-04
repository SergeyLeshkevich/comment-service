package ru.clevertec.comment.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.comment.entity.dto.UserResponse;

import java.util.UUID;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserResponse")
public class UserResponseBuilder implements TestBuilder<UserResponse> {

    private UUID uuid = UUID.fromString("0bdc4d34-af90-4b42-bba6-f588323c87d7");
    private String userName = "Test userName comment";

    @Override
    public UserResponse build() {
        return new UserResponse(uuid,userName);
    }
}
