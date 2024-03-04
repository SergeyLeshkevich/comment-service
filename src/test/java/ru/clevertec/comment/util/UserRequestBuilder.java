package ru.clevertec.comment.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.comment.entity.dto.UserRequest;

import java.util.UUID;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserRequest")
public class UserRequestBuilder implements TestBuilder<UserRequest> {

    private UUID uuid = UUID.fromString("0bdc4d34-af90-4b42-bba6-f588323c87d7");
    private String userName = "Test userName comment";

    @Override
    public UserRequest build() {
        return new UserRequest(uuid, userName);
    }
}
