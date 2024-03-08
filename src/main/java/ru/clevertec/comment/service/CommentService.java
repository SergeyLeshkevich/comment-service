package ru.clevertec.comment.service;

import ru.clevertec.comment.entity.dto.CommentRequest;
import ru.clevertec.comment.entity.dto.CommentResponse;
import ru.clevertec.comment.util.PaginationResponse;

import java.util.List;


public interface CommentService {

    CommentResponse get(Long id);

    CommentResponse getCommentByNewsId(Long commentId, Long newsId);

    CommentResponse getFromArchive(Long id);

    PaginationResponse<CommentResponse> getAll(int pageSize, int numberPage);

    PaginationResponse<CommentResponse> getAllFromArchive(int pageSize, int numberPage);

    CommentResponse create(CommentRequest commentDto);

    CommentResponse update(Long id, CommentRequest commentDto);

    PaginationResponse<CommentResponse> getCommentsByIdNews(Long idNews, int pageSize, int numberPage);

    PaginationResponse<CommentResponse> getCommentsByIdNewsFromArchive(Long idNews, int pageSize, int numberPage);

    void archive(Long id);

    void archiveByNewsId(Long newsId);

    List<CommentResponse> search(String searchValue,  Integer offset, Integer limit);
}
