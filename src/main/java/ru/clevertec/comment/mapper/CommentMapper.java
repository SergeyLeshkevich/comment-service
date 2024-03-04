package ru.clevertec.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.clevertec.comment.entity.Comment;
import ru.clevertec.comment.entity.dto.CommentRequest;
import ru.clevertec.comment.entity.dto.CommentResponse;

import java.util.List;

/**
 * Mapper interface for converting between Comment entities and corresponding DTOs.
 *
 * @author Sergey Leshkevich
 * @version 1.0
 */
@Mapper(config = MappersConfig.class)
public interface CommentMapper {

    /**
     * Converts Comment entity to CommentResponse DTO.
     *
     * @param comment Comment entity to be converted.
     * @return Corresponding CommentResponse DTO.
     */
    CommentResponse toViewModel(Comment comment);

    /**
     * Converts CommentRequest DTO to Comment entity.
     *
     * @param dto CommentRequest DTO to be converted.
     * @return Corresponding Comment entity.
     */
    Comment toEntity(CommentRequest dto);

    /**
     * Converts a list of Comment entities to a list of CommentResponse DTOs.
     *
     * @param list List of Comment entities to be converted.
     * @return List of corresponding CommentResponse DTOs.
     */
    List<CommentResponse> toViewModelList(List<Comment> list);

    /**
     * Merges data from CommentRequest DTO into an existing Comment entity.
     *
     * @param comment      Existing Comment entity to be updated.
     * @param commentDto   CommentRequest DTO containing updated data.
     * @return Updated Comment entity.
     */
    Comment merge(@MappingTarget Comment comment, CommentRequest commentDto);
}
