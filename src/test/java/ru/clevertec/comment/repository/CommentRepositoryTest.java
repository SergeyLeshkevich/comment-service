package ru.clevertec.comment.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.comment.config.PostgresSQLContainerInitializer;
import ru.clevertec.comment.entity.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest extends PostgresSQLContainerInitializer {

    private final TestEntityManager testEntityManager;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentRepositoryTest(TestEntityManager testEntityManager, CommentRepository commentRepository) {
        this.testEntityManager = testEntityManager;
        this.commentRepository = commentRepository;
    }

    @Test
    void shouldReturnedCommentByIdAndNewsIdWhereCommentIsNotArchived() {
        //given
        Long id = 1L;
        Comment comment = testEntityManager.find(Comment.class, 1);

        //when
        Optional<Comment> actual = commentRepository.findCommentByIdAndNewsIdAndIsArchivedIsFalse(id, id);

        //then
        assertThat(actual).isEqualTo(Optional.of(comment));
    }

    @Test
    void shouldReturnedAllCommentsWhereCommentIsNotArchived() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<Comment> expectedList = new ArrayList<>();
        expectedList.add(testEntityManager.find(Comment.class, 1));
        expectedList.add(testEntityManager.find(Comment.class, 2));
        expectedList.add(testEntityManager.find(Comment.class, 4));

        //when
        Page<Comment> actual = commentRepository.findAllByIsArchivedIsFalse(pageRequest);

        //then
        assertThat(actual.getContent()).isEqualTo(expectedList);
    }

    @Test
    void shouldReturnedAllCommentsWhereCommentIsArchived() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<Comment> expectedList = new ArrayList<>();
        expectedList.add(testEntityManager.find(Comment.class, 3));

        //when
        Page<Comment> actual = commentRepository.findAllByIsArchivedIsTrue(pageRequest);

        //then
        assertThat(actual.getContent()).isEqualTo(expectedList);
    }

    @Test
    void shouldReturnedAllCommentsByNewsIdWhereCommentIsNotArchived() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<Comment> expectedList = new ArrayList<>();
        expectedList.add(testEntityManager.find(Comment.class, 1));
        expectedList.add(testEntityManager.find(Comment.class, 2));

        //when
        Page<Comment> actual = commentRepository.findAllByIsArchivedIsFalseAndNewsId(1L,pageRequest);

        //then
        assertThat(actual.getContent()).isEqualTo(expectedList);
    }

    @Test
    void shouldReturnedAllCommentsByNewsIdWhereCommentIsArchived() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<Comment> expectedList = new ArrayList<>();
        expectedList.add(testEntityManager.find(Comment.class, 3));

        //when
        Page<Comment> actual = commentRepository.findAllByIsArchivedIsTrueAndNewsId(1L,pageRequest);

        //then
        assertThat(actual.getContent()).isEqualTo(expectedList);
    }

    @Test
    void shouldReturnedAllCommentsByIdNewsWhereCommentIsNotArchived() {
        //given
        Long id = 1L;
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<Comment> expectedList = new ArrayList<>();
        expectedList.add(testEntityManager.find(Comment.class, 1));
        expectedList.add(testEntityManager.find(Comment.class, 2));
        expectedList.add(testEntityManager.find(Comment.class, 3));

        //when
        commentRepository.archiveByNewsId(id);
        List<Comment> actual = commentRepository.findAllByIsArchivedIsTrue(pageRequest)
                .getContent()
                .stream()
                .filter(comment -> comment.getNewsId() == 1)
                .collect(Collectors.toList());

        //then
        assertThat(actual).isEqualTo(expectedList);
    }
}
