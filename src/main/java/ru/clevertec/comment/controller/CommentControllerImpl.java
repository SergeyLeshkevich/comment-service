package ru.clevertec.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.comment.entity.dto.CommentRequest;
import ru.clevertec.comment.entity.dto.CommentResponse;
import ru.clevertec.comment.service.CommentService;
import ru.clevertec.comment.util.PaginationResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentControllerImpl implements CommentController {

    private final CommentService service;

    @Override
    public ResponseEntity<CommentResponse> getById(Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.get(id));
    }

    @Override
    public ResponseEntity<CommentResponse> getByIdNews(Long idComment, Long idNews) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getCommentByNewsId(idComment, idNews));
    }

    @Override
    public ResponseEntity<CommentResponse> getFromArchive(Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getFromArchive(id));
    }

    @Override
    public ResponseEntity<PaginationResponse<CommentResponse>> getAll(int pageSize, int numberPage) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAll(pageSize, numberPage));
    }

    @Override
    public ResponseEntity<PaginationResponse<CommentResponse>> getAllByIdNews(Long idNews, int pageSize, int numberPage) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getCommentsByIdNews(idNews, pageSize, numberPage));
    }

    @Override
    public ResponseEntity<PaginationResponse<CommentResponse>> getAllByIdNewsFromArchive(Long idNews, int pageSize, int numberPage) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getCommentsByIdNewsFromArchive(idNews, pageSize, numberPage));
    }

    @Override
    public ResponseEntity<PaginationResponse<CommentResponse>> getAllFromArchive(int pageSize, int numberPage) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllFromArchive(pageSize, numberPage));
    }

    @Override
    public ResponseEntity<CommentResponse> create(CommentRequest commentDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(commentDto));
    }

    @Override
    public ResponseEntity<CommentResponse> update(Long id, CommentRequest commentDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.update(id, commentDto));
    }


    @Override
    public ResponseEntity<Void> moveToArchiveByNewsId(Long newsId) {
        service.archiveByNewsId(newsId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> moveToArchive(Long id) {
        service.archive(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<List<CommentResponse>> search(String searchValue, Integer offset, Integer limit) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.search(searchValue,offset,limit));
    }
}
