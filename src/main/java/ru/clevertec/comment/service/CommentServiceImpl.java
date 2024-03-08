package ru.clevertec.comment.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.comment.entity.Comment;
import ru.clevertec.comment.entity.User;
import ru.clevertec.comment.entity.dto.CommentRequest;
import ru.clevertec.comment.entity.dto.CommentResponse;
import ru.clevertec.comment.mapper.CommentMapper;
import ru.clevertec.comment.repository.CommentRepository;
import ru.clevertec.comment.util.PaginationResponse;
import ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException;
import ru.clevertec.loggingstarter.annotation.Loggable;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing comments on news entities.
 * This class provides methods to retrieve, create, update, and archive comments.
 *
 * @author Sergey Leshkevich
 * @version 1.0
 */
@Service
@Loggable
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final float TEXT_BOOST_FACTOR = 1.2f;
    private static final float USERNAME_BOOST_FACTOR = 1.1f;
    private static final String FIELD_USERNAME = "user.userName";
    private static final String FIELD_TEXT = "text";

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EntityManager entityManager;
    private final UserService userService;

    /**
     * Retrieves a comment by its ID.
     *
     * @param id The ID of the comment to retrieve.
     * @return A {@link CommentResponse} representing the retrieved comment.
     * @throws EntityNotFoundException if the comment is not found or is archived.
     */
    @Override
    @Cacheable(value = "api-cache",key = "#id")
    public CommentResponse get(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (optionalComment.isEmpty() || optionalComment.get().isArchived()) {
            throw EntityNotFoundException.of(Comment.class, id);
        }

        return commentMapper.toViewModel(optionalComment.get());
    }

    /**
     * Retrieves a comment by its ID and news ID, ensuring it is not archived.
     *
     * @param commentId The ID of the comment to retrieve.
     * @param newsId    The ID of the news associated with the comment.
     * @return A {@link CommentResponse} representing the retrieved comment.
     * @throws EntityNotFoundException if the comment is not found or is archived.
     */
    @Override
    public CommentResponse getCommentByNewsId(Long commentId, Long newsId) {
        Optional<Comment> optionalComment = commentRepository.findCommentByIdAndNewsIdAndIsArchivedIsFalse(commentId, newsId);

        if (optionalComment.isEmpty() || optionalComment.get().isArchived()) {
            throw EntityNotFoundException.of(Comment.class, commentId);
        }

        return commentMapper.toViewModel(optionalComment.get());
    }

    /**
     * Retrieves an archived comment by its ID.
     *
     * @param id The ID of the archived comment to retrieve.
     * @return A {@link CommentResponse} representing the retrieved archived comment.
     * @throws EntityNotFoundException if the archived comment is not found or is not archived.
     */
    @Override
    @Cacheable(value = "api-cache",key = "#id")
    public CommentResponse getFromArchive(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (optionalComment.isEmpty() || !optionalComment.get().isArchived()) {
            throw EntityNotFoundException.of(Comment.class, id);
        }

        return commentMapper.toViewModel(optionalComment.get());
    }

    /**
     * Retrieves a paginated list of active comments.
     *
     * @param pageSize   The number of comments per page.
     * @param numberPage The page number to retrieve.
     * @return A {@link PaginationResponse} containing a list of {@link CommentResponse} objects.
     */
    @Override
    public PaginationResponse<CommentResponse> getAll(int pageSize, int numberPage) {
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        Page<Comment> commentPage = commentRepository.findAllByIsArchivedIsFalse(pageRequest);
        PaginationResponse<CommentResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setCountPage(commentPage.getTotalPages());
        paginationResponse.setPageNumber(numberPage);
        paginationResponse.setContent(commentMapper.toViewModelList(commentPage.getContent()));

        return paginationResponse;
    }

    /**
     * Retrieves a paginated list of archived comments.
     *
     * @param pageSize   The number of archived comments per page.
     * @param numberPage The page number to retrieve.
     * @return A {@link PaginationResponse} containing a list of {@link CommentResponse} objects.
     */
    @Override
    public PaginationResponse<CommentResponse> getAllFromArchive(int pageSize, int numberPage) {
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        Page<Comment> commentPage = commentRepository.findAllByIsArchivedIsTrue(pageRequest);
        PaginationResponse<CommentResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setCountPage(commentPage.getTotalPages());
        paginationResponse.setPageNumber(numberPage);
        paginationResponse.setContent(commentMapper.toViewModelList(commentPage.getContent()));

        return paginationResponse;
    }

    /**
     * Creates a new comment.
     *
     * @param commentDto The {@link CommentRequest} containing the information for the new comment.
     * @return A {@link CommentResponse} representing the newly created comment.
     */
    @Override
    @Transactional
    @Cacheable(value = "api-cache",key = "#commentDto.user + #commentDto.text")
    public CommentResponse create(CommentRequest commentDto) {
        Comment comment = commentMapper.toEntity(commentDto);
        User user = userService.getByUuiD(commentDto.user().uuid()).orElse(null);

        if (user == null) {
            user = userService.create(commentDto.user());
            comment.setUser(user);
        } else {
            comment.setUser(user);
        }

        comment.setArchived(false);
        return commentMapper.toViewModel(commentRepository.save(comment));
    }

    /**
     * Updates an existing comment.
     *
     * @param id         The ID of the comment to update.
     * @param commentDto The {@link CommentRequest} containing the updated information for the comment.
     * @return A {@link CommentResponse} representing the updated comment.
     * @throws EntityNotFoundException if the comment is not found or is archived.
     */
    @Override
    @Transactional
    @CachePut(value = "api-cache",key = "#id")
    public CommentResponse update(Long id, CommentRequest commentDto) {
        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (optionalComment.isEmpty() || optionalComment.get().isArchived()) {
            throw EntityNotFoundException.of(Comment.class, id);
        }
        Comment updatedComment = commentRepository.save(commentMapper.merge(optionalComment.get(), commentDto));

        return commentMapper.toViewModel(updatedComment);
    }

    /**
     * Archives a comment by setting its archived status to true.
     *
     * @param id The ID of the comment to archive.
     * @throws EntityNotFoundException if the comment is not found.
     */
    @Override
    @Transactional
    @CacheEvict(value = "api-cache",key = "id")
    public void archive(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> EntityNotFoundException.of(Comment.class, id)
        );
        comment.setArchived(true);
        commentRepository.save(comment);
    }

    /**
     * Archives all comments associated with a news ID.
     *
     * @param newsId The ID of the news for which comments should be archived.
     */
    @Override
    @Transactional
    public void archiveByNewsId(Long newsId) {
        commentRepository.archiveByNewsId(newsId);
    }

    /**
     * Retrieves a paginated list of active comments associated with a news ID.
     *
     * @param idNews     The ID of the news for which comments should be retrieved.
     * @param pageSize   The number of comments per page.
     * @param numberPage The page number to retrieve.
     * @return A {@link PaginationResponse} containing a list of {@link CommentResponse} objects.
     */
    @Override
    public PaginationResponse<CommentResponse> getCommentsByIdNews(Long idNews, int pageSize, int numberPage) {
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        Page<Comment> commentPage = commentRepository.findAllByIsArchivedIsFalseAndNewsId(idNews, pageRequest);
        PaginationResponse<CommentResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setCountPage(commentPage.getTotalPages());
        paginationResponse.setPageNumber(numberPage);
        paginationResponse.setContent(commentMapper.toViewModelList(commentPage.getContent()));

        return paginationResponse;
    }

    /**
     * Retrieves a paginated list of not active comments associated with a news ID.
     *
     * @param idNews     The ID of the news for which comments should be retrieved.
     * @param pageSize   The number of comments per page.
     * @param numberPage The page number to retrieve.
     * @return A {@link PaginationResponse} containing a list of {@link CommentResponse} objects.
     */
    @Override
    public PaginationResponse<CommentResponse> getCommentsByIdNewsFromArchive(Long idNews, int pageSize, int numberPage) {
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        Page<Comment> commentPage = commentRepository.findAllByIsArchivedIsTrueAndNewsId(idNews, pageRequest);
        PaginationResponse<CommentResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setCountPage(commentPage.getTotalPages());
        paginationResponse.setPageNumber(numberPage);
        paginationResponse.setContent(commentMapper.toViewModelList(commentPage.getContent()));

        return paginationResponse;
    }

    /**
     * Searches for comments based on a search value with boosted fields without using Stream.
     * Uses Hibernate Search to perform a search and maps the results to {@link CommentResponse} objects.
     *
     * @param searchValue The value to search for in the "username" and "text" fields.
     * @param offset      The offset for pagination.
     * @param limit       The maximum number of results to fetch.
     * @return A List of {@link CommentResponse} objects representing the search results.
     */
    @Override
    public List<CommentResponse> search(String searchValue, Integer offset, Integer limit) {
        SearchSession searchSession = Search.session(entityManager);

        SearchResult<Comment> searchResult = searchSession.search(Comment.class)
                .where(comment -> comment
                        .bool()
                        .with(b -> {
                            b.must(comment.matchAll());
                            b.must(comment.match()
                                    .field(FIELD_USERNAME)
                                    .boost(USERNAME_BOOST_FACTOR)
                                    .field(FIELD_TEXT)
                                    .boost(TEXT_BOOST_FACTOR)
                                    .matching(searchValue));
                        }))
                .sort(SearchSortFactory::score)
                .fetch(offset, limit);
        List<Comment> commentAllList = searchResult.hits();
        List<Comment> result = commentAllList.stream().filter(comment -> !comment.isArchived()).toList();

        return commentMapper.toViewModelList(result);
    }
}
