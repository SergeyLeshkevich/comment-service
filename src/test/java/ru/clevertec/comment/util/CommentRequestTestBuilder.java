package ru.clevertec.comment.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.comment.entity.User;
import ru.clevertec.comment.entity.dto.CommentRequest;
import ru.clevertec.comment.entity.dto.UserRequest;

import java.time.LocalDateTime;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentRequest")
public class CommentRequestTestBuilder implements TestBuilder<CommentRequest> {

    private Long id = 1L;
    private LocalDateTime time = LocalDateTime.MIN;
    private String text = "Test text comment";
    private UserRequest user = UserRequestBuilder.aUserRequest().build();
    private Long newsId = 1L;
    private boolean isArchived = true;

    @Override
    public CommentRequest build() {
        return new CommentRequest(text, user, newsId);
    }
}
