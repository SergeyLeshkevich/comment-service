package ru.clevertec.comment.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.clevertec.comment.config.PostgresSQLContainerInitializer;
import ru.clevertec.comment.entity.Comment;
import ru.clevertec.comment.entity.User;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest extends PostgresSQLContainerInitializer {

    private final TestEntityManager testEntityManager;
    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(TestEntityManager testEntityManager, UserRepository userRepository) {
        this.testEntityManager = testEntityManager;
        this.userRepository = userRepository;
    }

    @Test
    void shouldReturnedUserByUuid() {
        //given
        User user = testEntityManager.find(User.class, 1);
        UUID uuid=UUID.fromString("0bdc4d34-af90-4b42-bba6-f588323c87d7");

        //when
        Optional<User> actual = userRepository.findByUuid(uuid);

        //then
        assertThat(actual).isEqualTo(Optional.of(user));
    }
}