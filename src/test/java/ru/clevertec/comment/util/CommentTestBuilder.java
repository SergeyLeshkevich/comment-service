package ru.clevertec.comment.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.comment.entity.Comment;
import ru.clevertec.comment.entity.User;

import java.time.LocalDateTime;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aComment")
public class CommentTestBuilder implements TestBuilder<Comment> {

    private Long id = 1L;
    private LocalDateTime time = LocalDateTime.MIN;
    private String text = "Test text comment";
    private boolean isArchived = false;
    private User user = UserTestBuilder.aUser().build();

    @Override
    public Comment build() {
        return new Comment(id, time, text, user, isArchived, 1L);
    }
}
