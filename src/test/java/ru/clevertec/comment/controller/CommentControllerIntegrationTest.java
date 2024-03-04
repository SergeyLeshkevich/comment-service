package ru.clevertec.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.clevertec.comment.config.PostgresSQLContainerInitializer;
import ru.clevertec.comment.entity.dto.CommentRequest;
import ru.clevertec.comment.entity.dto.CommentResponse;
import ru.clevertec.comment.entity.dto.UserRequest;
import ru.clevertec.comment.entity.dto.UserResponse;
import ru.clevertec.comment.util.CommentRequestTestBuilder;
import ru.clevertec.comment.util.CommentResponseTestBuilder;
import ru.clevertec.comment.util.UserResponseBuilder;
import ru.clevertec.comment.util.UserTestBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CommentControllerIntegrationTest extends PostgresSQLContainerInitializer {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldRetrieveComment() throws Exception {
        //given
        CommentResponse commentResponse = CommentResponseTestBuilder.aCommentResponse().build();

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/comments/1")
                        .contentType("application/json"))
                .andReturn();
        CommentResponse actual = objectMapper.readValue(result.getResponse().getContentAsString(), CommentResponse.class);

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual.text()).isEqualTo(commentResponse.text());
        assertThat(actual.user()).isEqualTo(commentResponse.user());
        assertThat(actual.time()).isEqualTo(commentResponse.time());
        assertThat(actual.id()).isEqualTo(commentResponse.id());
    }

    @Test
    void shouldRetrieveCommentByNews() throws Exception {
        //given
        CommentResponse commentResponse = CommentResponseTestBuilder.aCommentResponse().build();

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/comments/1/news/1")
                        .contentType("application/json"))
                .andReturn();
        CommentResponse actual = objectMapper.readValue(result.getResponse().getContentAsString(), CommentResponse.class);

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual.text()).isEqualTo(commentResponse.text());
        assertThat(actual.user()).isEqualTo(commentResponse.user());
        assertThat(actual.time()).isEqualTo(commentResponse.time());
        assertThat(actual.id()).isEqualTo(commentResponse.id());
    }

    @Test
    void shouldRetrieveAllCommentsByNewsFromArchive() throws Exception {
        //given
        String expected = "{\"pageNumber\":1,\"countPage\":1,\"content\":[{\"id\":3,\"time\":" +
                "\"2024-01-16T14:18:08.537\",\"text\":\"Test text comment\",\"user\":{\"uuid\":" +
                "\"0bdc4d34-af90-4b42-bba6-f588323c87d7\",\"userName\":\"Test userName comment\"},\"newsId\":1}]}";

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/comments/archive/news/1")
                        .param("pageSize", "5")
                        .param("numberPage", "1")
                        .contentType("application/json"))
                .andReturn();
        String actual = result.getResponse().getContentAsString();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldRetrieveCommentFromArchive() throws Exception {
        //given
        CommentResponse commentResponse = CommentResponseTestBuilder.aCommentResponse()
                .withId(3L)
                .withUserResponse(UserResponseBuilder.aUserResponse().build())
                .build();
        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/comments/archive/3")
                        .contentType("application/json"))
                .andReturn();
        CommentResponse actual = objectMapper.readValue(result.getResponse().getContentAsString(), CommentResponse.class);

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual.text()).isEqualTo(commentResponse.text());
        assertThat(actual.user()).isEqualTo(commentResponse.user());
        assertThat(actual.time()).isEqualTo(commentResponse.time());
        assertThat(actual.id()).isEqualTo(commentResponse.id());
    }

    @Test
    void shouldRetrieveAllComments() throws Exception {
        //given
        String expected = "{\"pageNumber\":1,\"countPage\":1,\"content\":[{\"id\":1,\"time\":" +
                "\"2024-01-16T14:18:08.537\",\"text\":\"Test text comment\",\"user\":" +
                "{\"uuid\":\"0bdc4d34-af90-4b42-bba6-f588323c87d7\",\"userName\":\"Test userName comment\"}," +
                "\"newsId\":1},{\"id\":2,\"time\":\"2024-01-16T14:18:08.537\",\"text\":" +
                "\"Test text comment\",\"user\":{\"uuid\":\"0bdc4d34-af90-4b42-bba6-f588323c87d7\"," +
                "\"userName\":\"Test userName comment\"},\"newsId\":1},{\"id\":4,\"time\":" +
                "\"2024-01-16T14:18:08.537\",\"text\":\"Test text comment2\",\"user\":{\"uuid\":" +
                "\"0bdc4d34-af90-4b42-bba6-f588323c87d7\",\"userName\":\"Test userName comment\"}," +
                "\"newsId\":2}]}";

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/comments")
                        .param("pageSize", "5")
                        .param("numberPage", "1")
                        .contentType("application/json"))
                .andReturn();
        String actual = result.getResponse().getContentAsString();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldRetrieveAllCommentsByNewsId() throws Exception {
        //given
        String expected = "{\"pageNumber\":1,\"countPage\":1,\"content\":[{\"id\":1,\"time\":" +
                "\"2024-01-16T14:18:08.537\",\"text\":\"Test text comment\",\"user\":{\"uuid\":" +
                "\"0bdc4d34-af90-4b42-bba6-f588323c87d7\",\"userName\":\"Test userName comment\"}," +
                "\"newsId\":1},{\"id\":2,\"time\":\"2024-01-16T14:18:08.537\",\"text\":\"Test text comment\"," +
                "\"user\":{\"uuid\":\"0bdc4d34-af90-4b42-bba6-f588323c87d7\",\"userName\":" +
                "\"Test userName comment\"},\"newsId\":1}]}";

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/comments/news/1")
                        .param("pageSize", "5")
                        .param("numberPage", "1")
                        .contentType("application/json"))
                .andReturn();
        String actual = result.getResponse().getContentAsString();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldRetrieveAllFromArchiveComments() throws Exception {
        //given
        String expected = "{\"pageNumber\":1,\"countPage\":1,\"content\":[{\"id\":3,\"time\":" +
                "\"2024-01-16T14:18:08.537\",\"text\":\"Test text comment\",\"user\":" +
                "{\"uuid\":\"0bdc4d34-af90-4b42-bba6-f588323c87d7\",\"userName\":" +
                "\"Test userName comment\"},\"newsId\":1}]}";

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/comments/archive")
                        .param("pageSize", "5")
                        .param("numberPage", "1")
                        .contentType("application/json"))
                .andReturn();
        String actual = result.getResponse().getContentAsString();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldCreateNewComment() throws Exception {
        //given
        CommentRequest commentRequest = CommentRequestTestBuilder.aCommentRequest().build();
        CommentResponse commentResponse = CommentResponseTestBuilder.aCommentResponse().withId(5L).build();
        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andReturn();
        CommentResponse actual = objectMapper.readValue(result.getResponse().getContentAsString(), CommentResponse.class);

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(201);
        assertThat(actual.text()).isEqualTo(commentResponse.text());
        assertThat(actual.user()).isEqualTo(commentResponse.user());
        assertThat(actual.time()).isNotNull();
        assertThat(actual.id()).isEqualTo(commentResponse.id());
    }

    @Test
    void shouldUpdateComment() throws Exception {
        //given
        CommentRequest commentRequest = CommentRequestTestBuilder.aCommentRequest().withText("Update text").build();
        CommentResponse commentResponse = CommentResponseTestBuilder.aCommentResponse().withText("Update text").build();
        UserResponse userResponse = UserResponseBuilder.aUserResponse().build();
        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/comments/2")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andReturn();
        CommentResponse actual = objectMapper.readValue(result.getResponse().getContentAsString(), CommentResponse.class);

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual.id()).isEqualTo(2L);
        assertThat(actual.text()).isEqualTo(commentRequest.text());
        assertThat(actual.user()).isEqualTo(userResponse);
        assertThat(actual.time()).isEqualTo(commentResponse.time());
    }

    @Test
    void shouldMoveToArchiveComment() throws Exception {
        //given
        CommentResponse commentResponse = CommentResponseTestBuilder.aCommentResponse().build();

        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/comments/1")
                        .contentType("application/json"))
                .andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/comments/archive/1")
                        .contentType("application/json"))
                .andReturn();
        CommentResponse actual = objectMapper.readValue(result.getResponse().getContentAsString(), CommentResponse.class);

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual.text()).isEqualTo(commentResponse.text());
        assertThat(actual.user()).isEqualTo(commentResponse.user());
        assertThat(actual.time()).isEqualTo(commentResponse.time());
        assertThat(actual.id()).isEqualTo(commentResponse.id());
    }

    @Test
    void shouldMoveToArchiveCommentByNewsId() throws Exception {
        //given
        String expected = "{\"pageNumber\":1,\"countPage\":1,\"content\":[{\"id\":1,\"time\":" +
                "\"2024-01-16T14:18:08.537\",\"text\":\"Test text comment\",\"user\":{\"uuid\":" +
                "\"0bdc4d34-af90-4b42-bba6-f588323c87d7\",\"userName\":\"Test userName comment\"}," +
                "\"newsId\":1},{\"id\":2,\"time\":\"2024-01-16T14:18:08.537\",\"text\":\"Test text comment\"," +
                "\"user\":{\"uuid\":\"0bdc4d34-af90-4b42-bba6-f588323c87d7\",\"userName\":" +
                "\"Test userName comment\"},\"newsId\":1},{\"id\":3,\"time\":\"2024-01-16T14:18:08.537\"," +
                "\"text\":\"Test text comment\",\"user\":{\"uuid\":\"0bdc4d34-af90-4b42-bba6-f588323c87d7\"," +
                "\"userName\":\"Test userName comment\"},\"newsId\":1}]}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/comments/news/1")
                        .contentType("application/json"))
                .andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/comments/archive")
                        .contentType("application/json"))
                .andReturn();

        String actual = result.getResponse().getContentAsString();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldRetrieveAllFoundComments() throws Exception {
        //given
        String expected = "[{\"id\":4,\"time\":\"2024-01-16T14:18:08.537\",\"text\":\"Test text comment2\"," +
                "\"user\":{\"uuid\":\"0bdc4d34-af90-4b42-bba6-f588323c87d7\",\"userName\":" +
                "\"Test userName comment\"},\"newsId\":2}]";

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/comments/search")
                        .param("search", "comment2")
                        .param("offset", "0")
                        .param("limit", "10")
                        .contentType("application/json"))
                .andReturn();
        String actual = result.getResponse().getContentAsString();

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual).isEqualTo(expected);
    }
}

