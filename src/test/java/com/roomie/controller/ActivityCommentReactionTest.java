package com.roomie.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomie.entity.Activity;
import com.roomie.entity.ActivityComment;
import com.roomie.entity.ActivityReaction;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.service.ActivityCommentService;
import com.roomie.service.ActivityReactionService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ActivityCommentReactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActivityCommentService activityCommentService;

    @MockitoBean
    private ActivityReactionService activityReactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private ActivityComment testComment;
    private ActivityReaction testReaction;

    @BeforeEach
    void setUp() {
        Profile profile = new Profile(
            "John Doe",
            "john@example.com",
            25,
            "oauth-123"
        );
        Activity activity = new Activity(1, false);

        testComment = new ActivityComment("Great job!");
        testComment.setProfile(profile);
        testComment.setActivity(activity);

        testReaction = new ActivityReaction("👍");
        testReaction.setProfile(profile);
        testReaction.setActivity(activity);
    }

    @Test
    void getCommentsByActivity_returns200() throws Exception {
        when(activityCommentService.getCommentsByActivity(1L)).thenReturn(
            List.of(testComment)
        );

        mockMvc
            .perform(get("/api/activity-comments/activity/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].comment").value("Great job!"));
    }

    @Test
    void getCommentsByActivity_returnsEmpty_whenNone() throws Exception {
        when(activityCommentService.getCommentsByActivity(99L)).thenReturn(
            List.of()
        );

        mockMvc
            .perform(get("/api/activity-comments/activity/99"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getCommentsByProfile_returns200() throws Exception {
        when(activityCommentService.getCommentsByProfile(1L)).thenReturn(
            List.of(testComment)
        );

        mockMvc
            .perform(get("/api/activity-comments/profile/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].comment").value("Great job!"));
    }

    @Test
    void getCommentById_returns200_whenFound() throws Exception {
        when(activityCommentService.getCommentById(1L)).thenReturn(testComment);

        mockMvc
            .perform(get("/api/activity-comments/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.comment").value("Great job!"));
    }

    @Test
    void getCommentById_returns404_whenNotFound() throws Exception {
        when(activityCommentService.getCommentById(99L)).thenThrow(
            new ResourceNotFoundException("ActivityComment", 99L)
        );

        mockMvc
            .perform(get("/api/activity-comments/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void addComment_returns201() throws Exception {
        when(
            activityCommentService.addComment(1L, 1L, "Great job!")
        ).thenReturn(testComment);

        Map<String, Object> payload = Map.of(
            "profileId",
            1,
            "activityId",
            1,
            "comment",
            "Great job!"
        );

        mockMvc
            .perform(
                post("/api/activity-comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.comment").value("Great job!"));
    }

    @Test
    void addComment_returns404_whenProfileNotFound() throws Exception {
        when(activityCommentService.addComment(99L, 1L, "comment")).thenThrow(
            new ResourceNotFoundException("Profile", 99L)
        );

        Map<String, Object> payload = Map.of(
            "profileId",
            99,
            "activityId",
            1,
            "comment",
            "comment"
        );

        mockMvc
            .perform(
                post("/api/activity-comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void updateComment_returns200() throws Exception {
        when(activityCommentService.updateComment(1L, "Updated!")).thenReturn(
            testComment
        );

        mockMvc
            .perform(
                put("/api/activity-comments/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        objectMapper.writeValueAsString(
                            Map.of("comment", "Updated!")
                        )
                    )
            )
            .andExpect(status().isOk());
    }

    @Test
    void updateComment_returns404_whenNotFound() throws Exception {
        when(activityCommentService.updateComment(99L, "Updated!")).thenThrow(
            new ResourceNotFoundException("ActivityComment", 99L)
        );

        mockMvc
            .perform(
                put("/api/activity-comments/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        objectMapper.writeValueAsString(
                            Map.of("comment", "Updated!")
                        )
                    )
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteComment_returns204() throws Exception {
        doNothing().when(activityCommentService).deleteComment(1L);

        mockMvc
            .perform(delete("/api/activity-comments/1"))
            .andExpect(status().isNoContent());

        verify(activityCommentService, times(1)).deleteComment(1L);
    }

    @Test
    void deleteComment_returns404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("ActivityComment", 99L))
            .when(activityCommentService)
            .deleteComment(99L);

        mockMvc
            .perform(delete("/api/activity-comments/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getReactionsByActivity_returns200() throws Exception {
        when(activityReactionService.getReactionsByActivity(1L)).thenReturn(
            List.of(testReaction)
        );

        mockMvc
            .perform(get("/api/activity-reactions/activity/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].reaction").value("👍"));
    }

    @Test
    void getReactionsByProfile_returns200() throws Exception {
        when(activityReactionService.getReactionsByProfile(1L)).thenReturn(
            List.of(testReaction)
        );

        mockMvc
            .perform(get("/api/activity-reactions/profile/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].reaction").value("👍"));
    }

    @Test
    void getReactionById_returns200_whenFound() throws Exception {
        when(activityReactionService.getReactionById(1L)).thenReturn(
            testReaction
        );

        mockMvc
            .perform(get("/api/activity-reactions/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.reaction").value("👍"));
    }

    @Test
    void getReactionById_returns404_whenNotFound() throws Exception {
        when(activityReactionService.getReactionById(99L)).thenThrow(
            new ResourceNotFoundException("ActivityReaction", 99L)
        );

        mockMvc
            .perform(get("/api/activity-reactions/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void addReaction_returns201() throws Exception {
        when(activityReactionService.addReaction(1L, 1L, "👍")).thenReturn(
            testReaction
        );

        Map<String, Object> payload = Map.of(
            "profileId",
            1,
            "activityId",
            1,
            "reaction",
            "👍"
        );

        mockMvc
            .perform(
                post("/api/activity-reactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.reaction").value("👍"));
    }

    @Test
    void addReaction_returns404_whenProfileNotFound() throws Exception {
        when(activityReactionService.addReaction(99L, 1L, "👍")).thenThrow(
            new ResourceNotFoundException("Profile", 99L)
        );

        Map<String, Object> payload = Map.of(
            "profileId",
            99,
            "activityId",
            1,
            "reaction",
            "👍"
        );

        mockMvc
            .perform(
                post("/api/activity-reactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void updateReaction_returns200() throws Exception {
        when(activityReactionService.updateReaction(1L, "❤️")).thenReturn(
            testReaction
        );

        mockMvc
            .perform(
                put("/api/activity-reactions/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        objectMapper.writeValueAsString(
                            Map.of("reaction", "❤️")
                        )
                    )
            )
            .andExpect(status().isOk());
    }

    @Test
    void updateReaction_returns404_whenNotFound() throws Exception {
        when(activityReactionService.updateReaction(99L, "❤️")).thenThrow(
            new ResourceNotFoundException("ActivityReaction", 99L)
        );

        mockMvc
            .perform(
                put("/api/activity-reactions/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        objectMapper.writeValueAsString(
                            Map.of("reaction", "❤️")
                        )
                    )
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteReaction_returns204() throws Exception {
        doNothing().when(activityReactionService).deleteReaction(1L);

        mockMvc
            .perform(delete("/api/activity-reactions/1"))
            .andExpect(status().isNoContent());

        verify(activityReactionService, times(1)).deleteReaction(1L);
    }

    @Test
    void deleteReaction_returns404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("ActivityReaction", 99L))
            .when(activityReactionService)
            .deleteReaction(99L);

        mockMvc
            .perform(delete("/api/activity-reactions/99"))
            .andExpect(status().isNotFound());
    }
}
