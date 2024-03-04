package ru.clevertec.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.clevertec.comment.entity.Comment;

import java.util.Optional;

/**
 * Repository interface for Comment entity.
 * This interface extends JpaRepository, providing CRUD operations and custom queries for Comment entities.
 *
 * @author Sergey Leshkevich
 * @version 1.0
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Finds a non-archived comment by its ID and associated news ID.
     *
     * @param id     the ID of the comment.
     * @param newsId the ID of the associated news.
     * @return an Optional containing the found comment or an empty Optional if not found.
     */
    Optional<Comment> findCommentByIdAndNewsIdAndIsArchivedIsFalse(Long id, Long newsId);

    /**
     * Retrieves a page of non-archived comments.
     *
     * @param pageRequest the pagination information.
     * @return a page of non-archived comments.
     */
    Page<Comment> findAllByIsArchivedIsFalse(PageRequest pageRequest);

    /**
     * Retrieves a page of archived comments.
     *
     * @param pageRequest the pagination information.
     * @return a page of archived comments.
     */
    Page<Comment> findAllByIsArchivedIsTrue(PageRequest pageRequest);

    /**
     * Retrieves a page of non-archived comments for a specific news ID.
     *
     * @param idNews      the ID of the news.
     * @param pageRequest the pagination information.
     * @return a page of non-archived comments for the specified news ID.
     */
    Page<Comment> findAllByIsArchivedIsFalseAndNewsId(Long idNews, PageRequest pageRequest);

    /**
     * Retrieves a page of archived comments for a specific news ID.
     *
     * @param idNews      the ID of the news.
     * @param pageRequest the pagination information.
     * @return a page of archived comments for the specified news ID.
     */
    Page<Comment> findAllByIsArchivedIsTrueAndNewsId(Long idNews, PageRequest pageRequest);

    /**
     * Archives all comments associated with a given news ID.
     *
     * @param newsId the ID of the news whose comments are to be archived.
     */
    @Modifying
    @Query("update Comment c set c.isArchived = true where c.newsId = :newsId")
    void archiveByNewsId(@Param("newsId") Long newsId);
}
