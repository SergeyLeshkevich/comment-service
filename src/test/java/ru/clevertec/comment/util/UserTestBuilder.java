package ru.clevertec.comment.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.comment.entity.User;

import java.util.UUID;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aUser")
public class UserTestBuilder implements TestBuilder<User> {

    private Long id = 1L;
    private UUID uuid = UUID.fromString("0bdc4d34-af90-4b42-bba6-f588323c87d7");
    private String userName = "Test userName comment";

    @Override
    public User build() {
        return new User(id, uuid, userName);
    }
}
