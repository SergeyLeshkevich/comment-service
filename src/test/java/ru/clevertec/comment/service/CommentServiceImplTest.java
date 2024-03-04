package ru.clevertec.comment.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.comment.entity.Comment;
import ru.clevertec.comment.entity.User;
import ru.clevertec.comment.entity.dto.CommentRequest;
import ru.clevertec.comment.entity.dto.CommentResponse;
import ru.clevertec.comment.entity.dto.UserRequest;
import ru.clevertec.comment.mapper.CommentMapper;
import ru.clevertec.comment.mapper.UserMapper;
import ru.clevertec.comment.repository.CommentRepository;
import ru.clevertec.comment.util.CommentRequestTestBuilder;
import ru.clevertec.comment.util.CommentResponseTestBuilder;
import ru.clevertec.comment.util.CommentTestBuilder;
import ru.clevertec.comment.util.PaginationResponse;
import ru.clevertec.comment.util.UserRequestBuilder;
import ru.clevertec.comment.util.UserTestBuilder;
import ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Captor
    ArgumentCaptor<Comment> commentTestCaptor;

    @Test
    void testGetShouldGetCommentByIdWhenCommentExistsAndIsNotArchived() {
        // given
        Long id = 1L;
        Comment comment = CommentTestBuilder.aComment().build();
        CommentResponse commentResponse = CommentResponseTestBuilder.aCommentResponse().build();
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        when(commentMapper.toViewModel(comment)).thenReturn(commentResponse);

        // when
        CommentResponse result = commentService.get(id);

        // then
        assertThat(result).isEqualTo(commentResponse);
    }

    @Test
    void testGetShouldThrowEntityNotFoundExceptionWhenCommentDoesNotExist() {
        // given
        Long id = 1L;
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> commentService.get(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Comment with 1 not found");
        verify(commentRepository).findById(id);
        verifyNoInteractions(commentMapper);
    }

    @Test
    void testGetShouldThrowEntityNotFoundExceptionWhenCommentIsArchived() {
        // given
        Long id = 1L;
        Comment comment = CommentTestBuilder.aComment().withArchived(true).build();
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        // when, then
        assertThatThrownBy(() -> commentService.get(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Comment with 1 not found");
        verify(commentRepository).findById(id);
        verifyNoInteractions(commentMapper);
    }

    @Test
    void testGetCommentByNewsIdShouldGetCommentByIdAnaIdNewsWhenCommentExistsAndIsNotArchived() {
        // given
        Long id = 1L;
        Comment comment = CommentTestBuilder.aComment().build();
        CommentResponse commentResponse = CommentResponseTestBuilder.aCommentResponse().build();
        when(commentRepository.findCommentByIdAndNewsIdAndIsArchivedIsFalse(id, id)).thenReturn(Optional.of(comment));
        when(commentMapper.toViewModel(comment)).thenReturn(commentResponse);

        // when
        CommentResponse result = commentService.getCommentByNewsId(id, id);

        // then
        assertThat(result).isEqualTo(commentResponse);
    }

    @Test
    void testGetCommentByNewsShouldThrowEntityNotFoundExceptionWhenCommentDoesNotExist() {
        // given
        Long id = 1L;
        when(commentRepository.findCommentByIdAndNewsIdAndIsArchivedIsFalse(id, id)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> commentService.getCommentByNewsId(id, id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Comment with 1 not found");
        verify(commentRepository).findCommentByIdAndNewsIdAndIsArchivedIsFalse(id, id);
        verifyNoInteractions(commentMapper);
    }


    @Test
    void testGetCommentByNewsShouldThrowEntityNotFoundExceptionWhenCommentIsArchived() {
        // given
        Long id = 1L;
        Comment comment = CommentTestBuilder.aComment().withArchived(true).build();
        when(commentRepository.findCommentByIdAndNewsIdAndIsArchivedIsFalse(id, id)).thenReturn(Optional.of(comment));

        // when, then
        assertThatThrownBy(() -> commentService.getCommentByNewsId(id, id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Comment with 1 not found");
        verify(commentRepository).findCommentByIdAndNewsIdAndIsArchivedIsFalse(id, id);
        verifyNoInteractions(commentMapper);
    }

    @Test
    void testGetFromArchiveShouldGetCommentFromArchiveByIdWhenCommentExistsAndIsArchived() {
        // given
        Long id = 1L;
        Comment comment = CommentTestBuilder.aComment().withArchived(true).build();
        CommentResponse commentResponse = CommentResponseTestBuilder.aCommentResponse().build();

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        when(commentMapper.toViewModel(comment)).thenReturn(commentResponse);

        // when
        CommentResponse result = commentService.getFromArchive(id);

        // then
        assertThat(result).isEqualTo(commentResponse);
    }

    @Test
    void testGetFromArchiveShouldThrowEntityNotFoundExceptionWhenCommentDoesNotExistInArchive() {
        // given
        Long id = 1L;
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> commentService.getFromArchive(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Comment with 1 not found");
        verify(commentRepository).findById(id);
        verifyNoInteractions(commentMapper);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenCommentIsNotArchivedInArchive() {
        // given
        Long id = 1L;
        Comment comment = new Comment();
        comment.setId(id);
        comment.setArchived(false);
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        // when, then
        assertThatThrownBy(() -> commentService.getFromArchive(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Comment with 1 not found");
        verify(commentRepository).findById(id);
        verifyNoInteractions(commentMapper);
    }

    @Test
    void testGetAllShouldGetAllCommentsByPageWhenCommentsExistAndAreNotArchived() {
        // given
        int pageSize = 10;
        int numberPage = 1;
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        List<Comment> comments = List.of(CommentTestBuilder.aComment().build(),
                CommentTestBuilder.aComment().withId(2L).build());
        List<CommentResponse> commentResponses = List.of(CommentResponseTestBuilder.aCommentResponse().build(),
                CommentResponseTestBuilder.aCommentResponse().build());
        Page<Comment> commentPage = new PageImpl<>(comments);
        PaginationResponse<CommentResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setCountPage(1);
        paginationResponse.setPageNumber(1);
        paginationResponse.setContent(commentResponses);
        when(commentRepository.findAllByIsArchivedIsFalse(pageRequest)).thenReturn(commentPage);
        when(commentMapper.toViewModelList(comments)).thenReturn(commentResponses);

        // when
        PaginationResponse<CommentResponse> result = commentService.getAll(pageSize, numberPage);

        // then
        assertThat(result).isEqualTo(paginationResponse);
        verify(commentRepository).findAllByIsArchivedIsFalse(pageRequest);
        verify(commentMapper).toViewModelList(comments);
    }

    @Test
    void shouldGetEmptyPaginationResponseWhenNoCommentsExistOrAreNotArchived() {
        // given
        int pageSize = 10;
        int numberPage = 1;
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        Page<Comment> commentPage = Page.empty(pageRequest);
        PaginationResponse<CommentResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setCountPage(0);
        paginationResponse.setPageNumber(1);
        paginationResponse.setContent(List.of());
        when(commentRepository.findAllByIsArchivedIsFalse(pageRequest)).thenReturn(commentPage);

        // when
        PaginationResponse<CommentResponse> result = commentService.getAll(pageSize, numberPage);

        // then
        assertThat(result).isEqualTo(paginationResponse);
    }

    @Test
    void testGetAllFromArchiveShouldGetAllCommentsFromArchiveByPageWhenCommentsExistAndAreArchived() {
        // given
        int pageSize = 10;
        int numberPage = 1;
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        List<Comment> comments = List.of(CommentTestBuilder.aComment().withArchived(true).build(),
                CommentTestBuilder.aComment().withArchived(true).withId(2L).build());
        List<CommentResponse> commentResponses = List.of(CommentResponseTestBuilder.aCommentResponse().build(),
                CommentResponseTestBuilder.aCommentResponse().build());
        Page<Comment> commentPage = new PageImpl<>(comments);
        PaginationResponse<CommentResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setCountPage(1);
        paginationResponse.setPageNumber(1);
        paginationResponse.setContent(commentResponses);
        when(commentRepository.findAllByIsArchivedIsTrue(pageRequest)).thenReturn(commentPage);
        when(commentMapper.toViewModelList(comments)).thenReturn(commentResponses);

        // when
        PaginationResponse<CommentResponse> result = commentService.getAllFromArchive(pageSize, numberPage);

        // then
        assertThat(result).isEqualTo(paginationResponse);
        verify(commentRepository).findAllByIsArchivedIsTrue(pageRequest);
        verify(commentMapper).toViewModelList(comments);
    }

    @Test
    void testGetAllFromArchiveShouldGetEmptyPaginationResponseWhenNoCommentsExistOrAreArchived() {
        // given
        int pageSize = 10;
        int numberPage = 1;
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        Page<Comment> commentPage = Page.empty(pageRequest);
        PaginationResponse<CommentResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setCountPage(0);
        paginationResponse.setPageNumber(1);
        paginationResponse.setContent(List.of());
        when(commentRepository.findAllByIsArchivedIsTrue(pageRequest)).thenReturn(commentPage);

        // when
        PaginationResponse<CommentResponse> result = commentService.getAllFromArchive(pageSize, numberPage);
        // then
        assertThat(result).isEqualTo(paginationResponse);
    }

    @Test
    void shouldCreateNewUserAndCommentWhenUserDoesNotExist() {
        // given
        CommentRequest commentDto = CommentRequestTestBuilder.aCommentRequest().build();
        CommentResponse commentResponse = CommentResponseTestBuilder.aCommentResponse().build();
        CommentRequest commentRequest = CommentRequestTestBuilder.aCommentRequest().build();
        Comment comment = CommentTestBuilder.aComment().build();
        User user = UserTestBuilder.aUser().build();
        when(userService.getByUuiD(commentDto.user().uuid())).thenReturn(Optional.empty());
        when(userService.create(commentDto.user())).thenReturn(user);
        when(commentMapper.toEntity(commentRequest)).thenReturn(comment);
        when(commentMapper.toViewModel(comment)).thenReturn(commentResponse);
        when(commentRepository.save(comment)).thenReturn(comment);

        // when
        CommentResponse result = commentService.create(commentDto);

        // then
        assertThat(result).isNotNull();
        verify(userService).create(commentDto.user());
        verify(commentRepository).save(comment);
    }

    @Test
    void shouldLinkExistingUserAndCommentWhenUserExists() {
        // given
        CommentRequest commentDto = CommentRequestTestBuilder.aCommentRequest().build();
        CommentRequest commentRequest = CommentRequestTestBuilder.aCommentRequest().build();
        CommentResponse commentResponse = CommentResponseTestBuilder.aCommentResponse().build();
        User existingUser = UserTestBuilder.aUser().build();
        Comment comment = CommentTestBuilder.aComment().build();
        when(userService.getByUuiD(commentDto.user().uuid())).thenReturn(Optional.of(existingUser));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toEntity(commentRequest)).thenReturn(comment);
        when(commentMapper.toViewModel(comment)).thenReturn(commentResponse);

        // when
        CommentResponse result = commentService.create(commentDto);

        // then
        assertThat(result).isNotNull();
        verify(userService, never()).create(commentDto.user());
        verify(commentRepository).save(comment);
    }

    @Test
    void testUpdateShouldUpdateCommentWhenCommentExistsAndIsNotArchivedAndCommentRequestIsValid() {
        // given
        Long id = 1L;
        CommentRequest commentRequest = CommentRequestTestBuilder.aCommentRequest().build();
        Comment comment = CommentTestBuilder.aComment().build();
        Optional<Comment> optionalComment = Optional.of(comment);
        CommentResponse commentResponse = CommentResponseTestBuilder.aCommentResponse().withText("Update").build();
        Comment updatedComment = CommentTestBuilder.aComment().withText("Update").build();
        when(commentRepository.findById(id)).thenReturn(optionalComment);
        when(commentMapper.merge(comment, commentRequest)).thenReturn(updatedComment);
        when(commentRepository.save(updatedComment)).thenReturn(updatedComment);
        when(commentMapper.toViewModel(updatedComment)).thenReturn(commentResponse);

        // when
        CommentResponse result = commentService.update(id, commentRequest);

        // then
        assertThat(result).isEqualTo(commentResponse);
        verify(commentRepository).findById(id);
        verify(commentRepository).save(updatedComment);
        verify(commentMapper).merge(optionalComment.get(), commentRequest);
        verify(commentMapper).toViewModel(updatedComment);
    }

    @Test
    void testUpdateShouldThrowEntityNotFoundExceptionWhenCommentDoesNotExist() {
        // given
        Long id = 1L;
        CommentRequest commentRequest = CommentRequestTestBuilder.aCommentRequest().build();
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> commentService.update(id, commentRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Comment with 1 not found");
        verify(commentRepository).findById(id);
        verifyNoInteractions(commentMapper);
    }

    @Test
    void testArchiveByNewsIdShouldCallMethodCommentRepositoryArchiveByNewsId() {
        // given
        Long id = 1L;

        // when
        commentService.archiveByNewsId(id);

        // then
        verify(commentRepository).archiveByNewsId(id);
    }

    @Test
    void testArchiveShouldThrowEntityNotFoundExceptionWhenCommentDoesNotExist() {
        // given
        Long id = 1L;
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> commentService.archive(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Comment with 1 not found");
        verify(commentRepository).findById(id);
    }

    @Test
    void testArchiveShouldArchivedCommentWhenCommentExistsAndCommentRequestIsValid() {
        // given
        Long id = 1L;
        Comment news = CommentTestBuilder.aComment().build();
        Comment expected = CommentTestBuilder.aComment().withArchived(true).build();
        when(commentRepository.findById(id)).thenReturn(Optional.of(news));

        // when
        commentService.archive(id);

        // then
        verify(commentRepository).findById(id);
        verify(commentRepository).save(commentTestCaptor.capture());
        Comment actual = commentTestCaptor.getValue();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testGetCommentsByIdNewsShouldReturnPaginationResponseWithCommentsByIdNews() {
        // given
        Long idNews = 1L;
        int pageSize = 10;
        int numberPage = 1;
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        List<Comment> comments = List.of(CommentTestBuilder.aComment().withArchived(true).build(),
                CommentTestBuilder.aComment().withArchived(true).withId(2L).build());
        List<CommentResponse> commentResponses = List.of(CommentResponseTestBuilder.aCommentResponse().build(),
                CommentResponseTestBuilder.aCommentResponse().build());
        Page<Comment> commentPage = new PageImpl<>(comments);
        when(commentRepository.findAllByIsArchivedIsFalseAndNewsId(idNews, pageRequest)).thenReturn(commentPage);
        when(commentMapper.toViewModelList(comments)).thenReturn(commentResponses);

        // when
        PaginationResponse<CommentResponse> actualResponse = commentService.getCommentsByIdNews(idNews, pageSize, numberPage);

        // then
        assertThat(actualResponse.getContent()).isEqualTo(commentResponses);
        verify(commentRepository).findAllByIsArchivedIsFalseAndNewsId(idNews, pageRequest);
        verify(commentMapper).toViewModelList(comments);
    }

    @Test
    void testGetCommentsByIdNewsShouldReturnEmptyPaginationResponseWhenNoCommentsByIdNews() {
        // given
        Long idNews = 1L;
        int pageSize = 10;
        int numberPage = 1;
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        Page<Comment> commentPage = Page.empty(pageRequest);

        // when
        when(commentRepository.findAllByIsArchivedIsFalseAndNewsId(idNews, pageRequest)).thenReturn(commentPage);
        PaginationResponse<CommentResponse> actualResponse = commentService.getCommentsByIdNews(idNews, pageSize, numberPage);

        // then
        assertThat(actualResponse.getContent()).isEqualTo(new ArrayList<>());
        assertThat(actualResponse.getCountPage()).isZero();
        assertThat(actualResponse.getPageNumber()).isEqualTo(1);
        verify(commentRepository).findAllByIsArchivedIsFalseAndNewsId(idNews, pageRequest);
    }

    @Test
    void testGetCommentsByIdNewsShouldReturnPaginationResponseWithCommentsByIdNewsFromArchive() {
        // given
        Long idNews = 1L;
        int pageSize = 10;
        int numberPage = 1;
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        List<Comment> comments = List.of(CommentTestBuilder.aComment().withArchived(true).build(),
                CommentTestBuilder.aComment().withArchived(true).withId(2L).build());
        List<CommentResponse> commentResponses = List.of(CommentResponseTestBuilder.aCommentResponse().build(),
                CommentResponseTestBuilder.aCommentResponse().build());
        Page<Comment> commentPage = new PageImpl<>(comments);
        when(commentRepository.findAllByIsArchivedIsTrueAndNewsId(idNews, pageRequest)).thenReturn(commentPage);
        when(commentMapper.toViewModelList(comments)).thenReturn(commentResponses);

        // when
        PaginationResponse<CommentResponse> actualResponse = commentService.getCommentsByIdNewsFromArchive(idNews, pageSize, numberPage);

        // then
        assertThat(actualResponse.getContent()).isEqualTo(commentResponses);
        verify(commentRepository).findAllByIsArchivedIsTrueAndNewsId(idNews, pageRequest);
        verify(commentMapper).toViewModelList(comments);
    }

    @Test
    void testGetCommentsByIdNewsShouldReturnEmptyPaginationResponseWhenNoCommentsByIdNewsFromArchive() {
        // given
        Long idNews = 1L;
        int pageSize = 10;
        int numberPage = 1;
        PageRequest pageRequest = PageRequest.of(numberPage - 1, pageSize);
        Page<Comment> commentPage = Page.empty(pageRequest);

        // when
        when(commentRepository.findAllByIsArchivedIsTrueAndNewsId(idNews, pageRequest)).thenReturn(commentPage);
        PaginationResponse<CommentResponse> actualResponse = commentService.getCommentsByIdNewsFromArchive(idNews, pageSize, numberPage);

        // then
        assertThat(actualResponse.getContent()).isEqualTo(new ArrayList<>());
        assertThat(actualResponse.getCountPage()).isZero();
        assertThat(actualResponse.getPageNumber()).isEqualTo(1);
        verify(commentRepository).findAllByIsArchivedIsTrueAndNewsId(idNews, pageRequest);
    }
}