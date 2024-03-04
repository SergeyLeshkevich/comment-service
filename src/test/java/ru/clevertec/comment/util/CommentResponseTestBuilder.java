package ru.clevertec.comment.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.comment.entity.dto.CommentResponse;
import ru.clevertec.comment.entity.dto.UserResponse;

import java.time.LocalDateTime;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentResponse")
public class CommentResponseTestBuilder implements TestBuilder<CommentResponse> {

    private Long id = 1L;
    private Long newsId = 1L;
    private LocalDateTime time = LocalDateTime.parse("2024-01-16T14:18:08.537");
    private String text = "Test text comment";
    private UserResponse userResponse = UserResponseBuilder.aUserResponse().build();

    @Override
    public CommentResponse build() {
        return new CommentResponse(id, time, text, userResponse, newsId);
    }
}
